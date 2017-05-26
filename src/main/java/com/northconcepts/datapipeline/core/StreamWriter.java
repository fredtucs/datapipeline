package com.northconcepts.datapipeline.core;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class StreamWriter extends DataWriter
{
    private final PrintWriter printWriter;
    private boolean closeStream;
    
    public StreamWriter(final File file) throws DataException {
        super();
        try {
            this.printWriter = new PrintWriter(new FileWriter(file));
            this.setCloseStream(true);
        }
        catch (Throwable e) {
            throw DataException.wrap(e).set("file", (file == null) ? null : file.getAbsolutePath());
        }
    }
    
    public StreamWriter(final Writer writer) {
        super();
        if (writer instanceof PrintWriter) {
            this.printWriter = (PrintWriter)writer;
        }
        else {
            this.printWriter = new PrintWriter(writer);
        }
    }
    
    public StreamWriter(final OutputStream outputStream) {
        super();
        this.printWriter = new PrintWriter(outputStream);
    }
    
    public boolean getCloseStream() {
        return this.closeStream;
    }
    
    public StreamWriter setCloseStream(final boolean closeStream) {
        this.closeStream = closeStream;
        return this;
    }
    
    @Override
	public void open() throws DataException {
        super.open();
        this.printSeparator();
    }
    
    @Override
	public void close() throws DataException {
        this.printWriter.println(this.getRecordCountAsString() + " records");
        this.printWriter.flush();
        if (this.closeStream) {
            this.printWriter.close();
        }
        super.close();
    }
    
    @Override
	protected void writeImpl(final Record record) throws Throwable {
        this.printWriter.println(this.getRecordCountAsString() + " - " + record);
        this.printSeparator();
    }
    
    private void printSeparator() {
        this.printWriter.println("-----------------------------------------------");
        this.printWriter.flush();
    }
}
