package com.northconcepts.datapipeline.core;

import java.util.concurrent.BlockingQueue;

public class PipedWriter extends DataWriter
{
    protected static final Record EOF;
    private final PipedReader reader;
    
    public PipedWriter(final PipedReader reader) {
        super();
        this.reader = reader;
    }
    
    public BlockingQueue<Record> getQueue() {
        return this.reader.getQueue();
    }
    
    @Override
	protected void writeImpl(final Record record) throws Throwable {
        this.reader.getQueue().put(record);
    }
    
    @Override
	public void close() throws DataException {
        try {
            this.reader.getQueue().put(PipedWriter.EOF);
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
        finally {
            super.close();
        }
    }
    
    static {
        EOF = new Record();
    }
}
