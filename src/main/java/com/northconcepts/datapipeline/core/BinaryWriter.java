package com.northconcepts.datapipeline.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class BinaryWriter extends AbstractWriter
{
    private final BufferedOutputStream outputStream;
    private final File file;
    
    public BinaryWriter(final File file) {
        super();
        try {
            this.file = file;
            this.outputStream = new BufferedOutputStream(new FileOutputStream(file), 16384);
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    public BinaryWriter(final OutputStream outputStream) {
        super();
        this.file = null;
        if (outputStream instanceof BufferedOutputStream) {
            this.outputStream = (BufferedOutputStream)outputStream;
        }
        else {
            this.outputStream = new BufferedOutputStream(outputStream, 16384);
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.file != null) {
            exception.set("BinaryWriter.file", this.file);
        }
        return super.addExceptionProperties(exception);
    }
    
    @Override
	public void close() {
        try {
            this.outputStream.close();
        }
        catch (IOException e) {
            throw this.exception(e);
        }
        finally {
            super.close();
        }
    }
    
    public BufferedOutputStream getOutputStream() {
        return this.outputStream;
    }
}
