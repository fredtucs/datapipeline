package com.northconcepts.datapipeline.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class TextWriter extends AbstractWriter
{
    private final BufferedWriter writer;
    private final File file;
    
    public TextWriter(final File file) {
        super();
        try {
            this.file = file;
            this.writer = new BufferedWriter(new FileWriter(file), 16384);
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    public TextWriter(final Writer writer) {
        super();
        this.file = null;
        if (writer instanceof BufferedWriter) {
            this.writer = (BufferedWriter)writer;
        }
        else {
            this.writer = new BufferedWriter(writer, 16384);
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.file != null) {
            exception.set("TextWriter.file", this.file);
        }
        return super.addExceptionProperties(exception);
    }
    
    public BufferedWriter getWriter() {
        return this.writer;
    }
    
    public File getFile() {
        return this.file;
    }
    
    @Override
	public void close() {
        try {
            this.writer.close();
        }
        catch (IOException e) {
            throw this.exception(e);
        }
        finally {
            super.close();
        }
    }
}
