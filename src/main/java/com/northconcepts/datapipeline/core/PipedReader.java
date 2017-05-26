package com.northconcepts.datapipeline.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PipedReader extends DataReader
{
    private final LinkedBlockingQueue<Record> queue;
    
    public PipedReader() {
        super();
        this.queue = new LinkedBlockingQueue<Record>();
    }
    
    public PipedReader(final int capacity) {
        super();
        this.queue = new LinkedBlockingQueue<Record>(capacity);
    }
    
    public BlockingQueue<Record> getQueue() {
        return this.queue;
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        final Record record = this.queue.take();
        if (record == PipedWriter.EOF) {
            this.close();
            return null;
        }
        return record;
    }
}
