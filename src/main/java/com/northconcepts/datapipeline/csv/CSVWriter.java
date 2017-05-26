package com.northconcepts.datapipeline.csv;

import java.io.File;
import java.io.Writer;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.LinedTextWriter;
import com.northconcepts.datapipeline.core.Record;

public class CSVWriter extends LinedTextWriter
{
    private char fieldSeparator;
    private char quoteChar;
    private final char[] SPECIAL_CHARS;
    
    public CSVWriter(final File file) {
        super(file);
        this.fieldSeparator = ',';
        this.quoteChar = '\"';
        this.SPECIAL_CHARS = new char[] { this.quoteChar, this.fieldSeparator, '\n', '\r' };
    }
    
    public CSVWriter(final Writer writer) {
        super(writer);
        this.fieldSeparator = ',';
        this.quoteChar = '\"';
        this.SPECIAL_CHARS = new char[] { this.quoteChar, this.fieldSeparator, '\n', '\r' };
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("CSVWriter.fieldSeparator", this.fieldSeparator);
        exception.set("CSVWriter.quoteChar", this.quoteChar);
        return super.addExceptionProperties(exception);
    }
    
    private void setupSpecialChars() {
        this.SPECIAL_CHARS[0] = this.quoteChar;
        this.SPECIAL_CHARS[1] = this.fieldSeparator;
    }
    
    public char getFieldSeparator() {
        return this.fieldSeparator;
    }
    
    public CSVWriter setFieldSeparator(final char columnSeparator) {
        this.assertNotOpened();
        this.fieldSeparator = columnSeparator;
        this.setupSpecialChars();
        return this;
    }
    
    public char getQuoteChar() {
        return this.quoteChar;
    }
    
    public CSVWriter setQuoteChar(final char quoteChar) {
        this.assertNotOpened();
        this.quoteChar = quoteChar;
        this.setupSpecialChars();
        return this;
    }
    
    @Override
	protected void assembleLine(final Record record, final StringBuilder lineBuffer) throws Throwable {
        if (record.getFieldCount() > 0) {
            this.addField(record, 0, lineBuffer);
        }
        for (int i = 1; i < record.getFieldCount(); ++i) {
            lineBuffer.append(this.fieldSeparator);
            this.addField(record, i, lineBuffer);
        }
    }
    
    private void addField(final Record record, final int index, final StringBuilder lineBuffer) {
        final String value = record.getField(index).getValueAsString();
        if (value == null) {
            return;
        }
        final int length = value.length();
        if (length == 0) {
            lineBuffer.append(this.quoteChar).append(this.quoteChar);
        }
        else if (this.contains(value, this.SPECIAL_CHARS) || this.charIsWhitespace(value, 0) || this.charIsWhitespace(value, length - 1)) {
            lineBuffer.append(this.quoteChar).append(this.escape(value)).append(this.quoteChar);
        }
        else {
            lineBuffer.append(value);
        }
    }
    
    private String escape(final String s) {
        final int index = s.indexOf(this.quoteChar);
        if (index < 0) {
            return s;
        }
        final StringBuilder buffer = new StringBuilder(s.length() + 32);
        buffer.append(s.substring(0, index));
        buffer.append(this.quoteChar).append(this.quoteChar);
        for (int i = index + 1; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (c == this.quoteChar) {
                buffer.append(this.quoteChar).append(this.quoteChar);
            }
            else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }
    
    private boolean contains(final String s, final char[] c) {
        for (int i = 0; i < c.length; ++i) {
            if (s.indexOf(c[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean charIsWhitespace(final String s, final int index) {
        return Character.isWhitespace(s.charAt(index));
    }
}
