package com.northconcepts.datapipeline.core;

import java.util.LinkedList;

public abstract class DataReader extends DataEndpoint
{
    private final LinkedList<Record> buffer;
    
    public DataReader() {
        super();
        this.buffer = new LinkedList<Record>();
    }
    
    public DataReader getNestedReader() {
        return null;
    }
    
    public DataReader getRootReader() {
        if (this.getNestedReader() == null) {
            return this;
        }
        return this.getNestedReader().getRootReader();
    }
    
    public int available() throws DataException {
        return this.buffer.size();
    }
    
    public final int getBufferSize() {
        return this.buffer.size();
    }
    
    public void push(final Record record) {
        this.buffer.addLast(record);
    }
    
    protected Record pop() {
        if (this.buffer.isEmpty()) {
            return null;
        }
        return this.buffer.removeFirst();
    }
    
    public final Record peek(final int index) {
        while (index >= this.buffer.size()) {
            final Record record = this.read();
            if (record == null) {
                return null;
            }
            this.push(record);
        }
        return this.buffer.get(index);
    }
    
    protected abstract Record readImpl() throws Throwable;
    
    public Record read() throws DataException {
        try {
            this.assertOpened();
            Record record;
            do {
                record = this.pop();
            } while (record != null && record.isDeleted());
            if (record != null) {
                this.incrementRecordCount();
                record = Product.onRead(this, record);
                return record;
            }
            do {
                record = this.readImpl();
            } while (record != null && record.isDeleted());
            if (record != null) {
                this.incrementRecordCount();
            }
            else if (this.getBufferSize() > 0) {
                return this.read();
            }
            record = Product.onRead(this, record);
            return record;
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public int skip(final int count) throws DataException {
        int skipped;
        for (skipped = 0; skipped < count && this.read() != null; ++skipped) {}
        return skipped;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("DataReader.bufferSize", this.buffer.size());
        exception.set("DataReader.recordCount", this.getRecordCountAsString());
        return super.addExceptionProperties(exception);
    }
}
