package com.northconcepts.datapipeline.fixedwidth;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.TextReader;
import com.northconcepts.datapipeline.internal.lang.Util;

public class FixedWidthReader extends TextReader
{
    private char fillChar;
    private String lineText;
    private final ArrayList<FixedWidthField> fields;
    private boolean emptyStringToNull;
    private int nextFieldStart;
    private boolean lastFieldConsumesRemaining;
    
    public FixedWidthReader(final File file) {
        super(file);
        this.fillChar = ' ';
        this.fields = new ArrayList<FixedWidthField>();
        this.emptyStringToNull = true;
    }
    
    public FixedWidthReader(final Reader reader) {
        super(reader);
        this.fillChar = ' ';
        this.fields = new ArrayList<FixedWidthField>();
        this.emptyStringToNull = true;
    }
    
    public char getFillChar() {
        return this.fillChar;
    }
    
    public FixedWidthReader setFillChar(final char fillChar) {
        this.fillChar = fillChar;
        return this;
    }
    
    public boolean isLastFieldConsumesRemaining() {
        return this.lastFieldConsumesRemaining;
    }
    
    public FixedWidthReader setLastFieldConsumesRemaining(final boolean lastFieldConsumesRemaining) {
        this.lastFieldConsumesRemaining = lastFieldConsumesRemaining;
        return this;
    }
    
    public FixedWidthReader addFields(final int... width) {
        for (int i = 0; i < width.length; ++i) {
            this.fields.add(new FixedWidthField().setWidth(width[i]));
        }
        return this;
    }
    
    public FixedWidthReader addField(final String name, final int width) {
        this.fields.add(new FixedWidthField().setName(name).setWidth(width));
        return this;
    }
    
    public FixedWidthReader addField(final String name, final int width, final FixedWidthAlign align, final char fillChar) {
        this.fields.add(new FixedWidthField().setName(name).setWidth(width).setAlign(align).setFillChar(fillChar));
        return this;
    }
    
    public FixedWidthReader skipField(final int width) {
        this.fields.add(new FixedWidthField().setWidth(width).setSkip(true));
        return this;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("FixedWidthDataReader.lineText", this.lineText);
        exception.set("FixedWidthDataReader.lineLength", (this.lineText == null) ? 0 : this.lineText.length());
        exception.set("FixedWidthDataReader.column", this.nextFieldStart);
        exception.set("FixedWidthDataReader.fillChar", this.fillChar);
        exception.set("FixedWidthDataReader.lastFieldConsumesRemaining", this.lastFieldConsumesRemaining);
        if (this.fields != null) {
            exception.set("FixedWidthDataReader.fieldCount", this.fields.size());
            for (int i = 0; i < this.fields.size(); ++i) {
                exception.set("FixedWidthDataReader.field[" + i + "]", this.fields.get(i));
            }
        }
        exception.setRecord(this.currentRecord);
        return super.addExceptionProperties(exception);
    }
    
    @Override
	public void open() throws DataException {
        if (this.fields.size() < 1) {
            throw this.exception("no fields defined, call addField(...) or skipField(...) first");
        }
        super.open();
    }
    
    @Override
	protected boolean fillRecord(final Record record) throws Throwable {
        if (!this.readLineText()) {
            return false;
        }
        this.nextFieldStart = 0;
        final int length = this.lineText.length();
        int fieldIndex = 0;
        for (int i = 0; i < this.fields.size(); ++i) {
            final FixedWidthField fieldSpec = this.fields.get(i);
            int fieldEnd;
            if (this.lastFieldConsumesRemaining && i == this.fields.size() - 1) {
                fieldEnd = this.lineText.length();
            }
            else {
                fieldEnd = this.nextFieldStart + fieldSpec.getWidth();
            }
            if (fieldEnd > length) {
                throw this.exception("field " + i + "'s width (" + fieldSpec + ") exceeds line length (" + length + ") by " + (fieldEnd - length) + " character(s)");
            }
            if (!fieldSpec.isSkip()) {
                String value = this.lineText.substring(this.nextFieldStart, fieldEnd);
                value = this.trimField(fieldIndex, value, fieldSpec);
                if (this.emptyStringToNull && value != null && value.length() == 0) {
                    value = null;
                }
                final Field field = record.getField(fieldIndex, true).setValue(value);
                if (fieldSpec.getName() != null) {
                    field.setName(fieldSpec.getName());
                }
                ++fieldIndex;
            }
            this.nextFieldStart = fieldEnd;
        }
        return this.emptyStringToNull;
    }
    
    private String trimField(final int index, String value, final FixedWidthField fieldSpec) throws Throwable {
        if (value == null) {
            return null;
        }
        final char fillChar = (fieldSpec.getFillChar() != null) ? fieldSpec.getFillChar() : this.fillChar;
        if (fieldSpec.getAlign() == FixedWidthAlign.RIGHT) {
            value = Util.trimLeft(value, fillChar);
        }
        else {
            value = Util.trimRight(value, fillChar);
        }
        return value;
    }
    
    private boolean readLineText() throws IOException {
        this.lineText = this.reader.readLine();
        return this.lineText != null;
    }
}
