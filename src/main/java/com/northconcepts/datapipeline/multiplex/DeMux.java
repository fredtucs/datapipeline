package com.northconcepts.datapipeline.multiplex;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;

public class DeMux
{
    public static final int MAX_QUEUE_SIZE = 100;
    private final IStrategy strategy;
    private final DataReader source;
    private final List<DeMuxReader> sink;
    
    public DeMux(final DataReader source, final IStrategy strategy) {
        super();
        this.sink = new ArrayList<DeMuxReader>();
        this.source = source;
        this.strategy = strategy;
    }
    
    protected IStrategy getStrategy() {
        return this.strategy;
    }
    
    private DataReader getSource() {
        return this.source;
    }
    
    private List<DeMuxReader> getSink() {
        return this.sink;
    }
    
    public DataReader createReader() {
        final DeMuxReader r = new DeMuxReader(this);
        this.sink.add(r);
        return r;
    }
    
    public void run() throws DataException {
        Record currentRecord = null;
        this.source.open();
        try {
            while ((currentRecord = this.source.read()) != null) {
                this.strategy.put(this, currentRecord);
                currentRecord = null;
            }
        }
        catch (Throwable e) {
            throw DataException.wrap(e).setRecord(currentRecord);
        }
        finally {
            this.source.close();
        }
    }
    
    public enum Strategy implements IStrategy
    {
        BROADCAST {
            public void put(final DeMux deMux, final Record currentRecord) throws Throwable {
                for (final DeMuxReader sink : deMux.getSink()) {
                    sink.getQueue().put(currentRecord);
                }
            }
            
            public Record take(final DeMuxReader reader) throws Throwable {
                return new Record(reader.getQueue().take());
            }
        }, 
        ROUND_ROBIN {
            public void put(final DeMux deMux, final Record currentRecord) throws Throwable {
                final int index = (int)(deMux.getSource().getRecordCount() % deMux.getSink().size());
                final DeMuxReader reader = deMux.getSink().get(index);
                reader.getQueue().put(currentRecord);
            }
            
            public Record take(final DeMuxReader reader) throws Throwable {
                return reader.getQueue().take();
            }
        };
    }
    
    public interface IStrategy
    {
        void put(DeMux p0, Record p1) throws Throwable;
        
        Record take(DeMuxReader p0) throws Throwable;
    }
}
