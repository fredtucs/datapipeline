package com.northconcepts.datapipeline.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public abstract class TextReader extends AbstractReader
{
    protected static final int EOF = -1;
    protected final LineNumberReader reader;
    private final File file;
    
    public TextReader(final File file) {
        super();
        try {
            this.file = file;
            this.reader = new LineNumberReader(new FileReader(file), 16384);
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    public TextReader(final Reader reader) {
        super();
        this.file = null;
        if (reader instanceof LineNumberReader) {
            this.reader = (LineNumberReader)reader;
        }
        else {
            this.reader = new LineNumberReader(reader, 16384);
        }
    }
    
    @Override
	public int available() {
        try {
            return super.available() + (this.reader.ready() ? 1 : 0);
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public void open() throws DataException {
        super.open();
        try {
            for (int i = 0; i < this.getStartingRow(); ++i) {
                this.reader.readLine();
            }
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public void close() {
        try {
            this.reader.close();
        }
        catch (IOException e) {
            throw this.exception(e);
        }
        finally {
            super.close();
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.reader != null) {
            exception.set("TextReader.line", this.reader.getLineNumber());
        }
        exception.setRecord(this.currentRecord);
        if (this.file != null) {
            exception.set("TextReader.file", this.file);
        }
        return super.addExceptionProperties(exception);
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        if (this.getLastRow() >= 0 && this.reader.getLineNumber() > this.getLastRow()) {
            return null;
        }
        return super.readImpl();
    }
}
