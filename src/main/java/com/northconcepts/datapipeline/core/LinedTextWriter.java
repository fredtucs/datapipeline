package com.northconcepts.datapipeline.core;

import java.io.File;
import java.io.Writer;

public abstract class LinedTextWriter extends TextWriter
{
    private final StringBuilder lineBuffer;
    private String newLine;
    
    public LinedTextWriter(final File file) {
        super(file);
        this.lineBuffer = new StringBuilder(128);
        this.newLine = System.getProperty("line.separator", "\n");
    }
    
    public LinedTextWriter(final Writer writer) {
        super(writer);
        this.lineBuffer = new StringBuilder(128);
        this.newLine = System.getProperty("line.separator", "\n");
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("LinedTextWriter.newLine", this.newLine);
        return super.addExceptionProperties(exception);
    }
    
    public String getNewLine() {
        return this.newLine;
    }
    
    public LinedTextWriter setNewLine(final String newLine) {
        this.assertNotOpened();
        this.newLine = newLine;
        return this;
    }
    
    @Override
	protected final void writeRecord(final Record record) throws Throwable {
        this.assembleLine(record, this.lineBuffer);
        this.getWriter().write(this.lineBuffer.toString());
        this.getWriter().write(this.newLine);
        this.lineBuffer.setLength(0);
    }
    
    protected abstract void assembleLine(final Record p0, final StringBuilder p1) throws Throwable;
}
