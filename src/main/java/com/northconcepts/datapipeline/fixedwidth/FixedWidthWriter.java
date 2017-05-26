package com.northconcepts.datapipeline.fixedwidth;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.LinedTextWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.Util;

public class FixedWidthWriter extends LinedTextWriter
{
    private char fillChar;
    private final ArrayList<FixedWidthField> fields;
    private boolean truncateFields;
    
    public FixedWidthWriter(final File file) {
        super(file);
        this.fillChar = ' ';
        this.fields = new ArrayList<FixedWidthField>();
        this.truncateFields = true;
    }
    
    public FixedWidthWriter(final Writer writer) {
        super(writer);
        this.fillChar = ' ';
        this.fields = new ArrayList<FixedWidthField>();
        this.truncateFields = true;
    }
    
    public char getFillChar() {
        return this.fillChar;
    }
    
    public FixedWidthWriter setFillChar(final char fillChar) {
        this.fillChar = fillChar;
        return this;
    }
    
    public boolean isTruncateFields() {
        return this.truncateFields;
    }
    
    public FixedWidthWriter setTruncateFields(final boolean truncateFields) {
        this.truncateFields = truncateFields;
        return this;
    }
    
    public FixedWidthWriter addFields(final int... width) {
        for (int i = 0; i < width.length; ++i) {
            this.fields.add(new FixedWidthField().setWidth(width[i]));
        }
        return this;
    }
    
    public FixedWidthWriter addField(final int width, final FixedWidthAlign align, final char fillChar) {
        this.fields.add(new FixedWidthField().setWidth(width).setAlign(align).setFillChar(fillChar));
        return this;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("FixedWidthDataWriter.fillChar", this.fillChar);
        if (this.fields != null) {
            exception.set("FixedWidthDataWriter.fieldCount", this.fields.size());
            for (int i = 0; i < this.fields.size(); ++i) {
                exception.set("FixedWidthDataWriter.field[" + i + "]", this.fields.get(i));
            }
        }
        exception.setRecord(this.currentRecord);
        return super.addExceptionProperties(exception);
    }
    
    @Override
	protected void assembleLine(final Record record, final StringBuilder lineBuffer) throws Throwable {
        final int fieldCount = this.fields.size();
        final int recordFieldCount = record.getFieldCount();
        if (fieldCount == 0) {
            throw this.exception("no fields defined, call addField(...) first");
        }
        if (fieldCount < recordFieldCount) {
            throw this.exception("too few fields defined, expected " + recordFieldCount + " found " + fieldCount + ", call addField(...)");
        }
        if (fieldCount > recordFieldCount) {
            throw this.exception("too many fields defined, expected " + recordFieldCount + " found " + fieldCount + ", remove addField(...) call(s)");
        }
        for (int i = 0; i < this.fields.size(); ++i) {
            final FixedWidthField fieldSpec = this.fields.get(i);
            final String value = record.getField(i).getValueAsString();
            this.addFieldToLineBuffer(i, value, fieldSpec, lineBuffer);
        }
    }
    
    private void addFieldToLineBuffer(final int index, String value, final FixedWidthField fieldSpec, final StringBuilder lineBuffer) throws Throwable {
        if (value == null) {
            value = "";
        }
        final int padding = fieldSpec.getWidth() - value.length();
        if (padding < 0) {
            if (!this.truncateFields) {
                throw this.exception("field " + index + "'s length (" + value.length() + ") exceeds specified width (" + fieldSpec.getWidth() + ")");
            }
            value = value.substring(0, fieldSpec.getWidth());
        }
        final char fillChar = (fieldSpec.getFillChar() != null) ? fieldSpec.getFillChar() : this.fillChar;
        if (fieldSpec.getAlign() == FixedWidthAlign.RIGHT) {
            value = Util.padLeft(value, fieldSpec.getWidth(), fillChar);
        }
        else {
            value = Util.padRight(value, fieldSpec.getWidth(), fillChar);
        }
        lineBuffer.append(value);
    }
}
