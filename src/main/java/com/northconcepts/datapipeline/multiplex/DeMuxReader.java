package com.northconcepts.datapipeline.multiplex;

import java.util.concurrent.LinkedBlockingQueue;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;

class DeMuxReader extends DataReader
{
    private final DeMux source;
    private final LinkedBlockingQueue<Record> queue;
    
    public DeMuxReader(final DeMux source) {
        super();
        this.queue = new LinkedBlockingQueue<Record>(100);
        this.source = source;
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        return this.source.getStrategy().take(this);
    }
    
    protected LinkedBlockingQueue<Record> getQueue() {
        return this.queue;
    }
}
