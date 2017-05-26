package com.northconcepts.datapipeline.core;

import java.util.ArrayList;
import java.util.List;

public class MultiWriter extends DataWriter
{
    public static final WriteStrategy REPLICATE;
    private ArrayList<DataWriter> list;
    private WriteStrategy writeStrategy;
    
    public MultiWriter() {
        super();
        this.list = new ArrayList<DataWriter>();
        this.writeStrategy = MultiWriter.REPLICATE;
    }
    
    public MultiWriter(final WriteStrategy writeStrategy) {
        super();
        this.list = new ArrayList<DataWriter>();
        this.writeStrategy = MultiWriter.REPLICATE;
        this.setWriteStrategy(writeStrategy);
    }
    
    public MultiWriter(final DataWriter... sinks) {
        super();
        this.list = new ArrayList<DataWriter>();
        this.writeStrategy = MultiWriter.REPLICATE;
        this.add(sinks);
    }
    
    public WriteStrategy getWriteStrategy() {
        return this.writeStrategy;
    }
    
    public MultiWriter setWriteStrategy(final WriteStrategy writeStrategy) {
        if (writeStrategy == null) {
            throw new NullPointerException("strategy is null");
        }
        this.writeStrategy = writeStrategy;
        return this;
    }
    
    public MultiWriter add(final DataWriter... sinks) {
        this.assertNotOpened();
        for (int i = 0; i < sinks.length; ++i) {
            this.list.add(sinks[i]);
        }
        return this;
    }
    
    public int getCount() {
        return this.list.size();
    }
    
    public MultiWriter remove(final DataWriter sink) {
        this.assertNotOpened();
        this.list.remove(sink);
        return this;
    }
    
    public MultiWriter removeAllDataSink() {
        this.assertNotOpened();
        this.list.clear();
        return this;
    }
    
    public DataEndpoint get(final int index) {
        return this.list.get(index);
    }
    
    @Override
	public void open() throws DataException {
        super.open();
        for (int i = 0; i < this.list.size(); ++i) {
            this.get(i).open();
        }
    }
    
    @Override
	public void close() throws DataException {
        DataException exception = null;
        for (int i = 0; i < this.list.size(); ++i) {
            final DataEndpoint endpoint = this.get(i);
            try {
                if (endpoint.isOpen()) {
                    endpoint.close();
                }
            }
            catch (Throwable e) {
                if (exception == null) {
                    exception = endpoint.exception(e);
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
        super.close();
    }
    
    @Override
	protected void writeImpl(final Record record) throws Throwable {
        this.writeStrategy.write(record, this.list);
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("MultiWriter.writeStrategy", this.writeStrategy);
        exception.set("MultiWriter.writers", this.list.size());
        return super.addExceptionProperties(exception);
    }
    
    static {
        REPLICATE = new ReplicateWriteStrategy();
    }
    
    public abstract static class WriteStrategy
    {
        protected abstract void write(final Record p0, final List<DataWriter> p1) throws Throwable;
    }
    
    public static class ReplicateWriteStrategy extends WriteStrategy
    {
        @Override
		protected void write(final Record record, final List<DataWriter> list) throws Throwable {
            for (int i = 0; i < list.size(); ++i) {
                final DataWriter tempSink = list.get(i);
                tempSink.write(record);
            }
        }
    }
    
    public static class RoundRobinWriteStrategy extends WriteStrategy
    {
        private int lastWriterIndex;
        
        public RoundRobinWriteStrategy() {
            super();
            this.lastWriterIndex = -1;
        }
        
        @Override
		protected void write(final Record record, final List<DataWriter> list) throws Throwable {
            this.lastWriterIndex = (this.lastWriterIndex + 1) % list.size();
            list.get(this.lastWriterIndex).write(record);
        }
    }
    
    public static class AvailableCapacityWriteStrategy extends WriteStrategy
    {
        private int lastWriterIndex;
        
        public AvailableCapacityWriteStrategy() {
            super();
            this.lastWriterIndex = -1;
        }
        
        @Override
		protected void write(final Record record, final List<DataWriter> list) throws Throwable {
            this.lastWriterIndex = (this.lastWriterIndex + 1) % list.size();
            DataWriter writer = list.get(this.lastWriterIndex);
            int available = writer.available();
            for (int i = 0; i < list.size(); ++i) {
                if (i != this.lastWriterIndex) {
                    final DataWriter writer2 = list.get(i);
                    final int available2 = writer2.available();
                    if (available2 > available) {
                        available = available2;
                        writer = writer2;
                        this.lastWriterIndex = i;
                    }
                }
            }
            writer.write(record);
        }
    }
}
