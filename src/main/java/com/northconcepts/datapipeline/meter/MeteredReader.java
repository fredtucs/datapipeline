package com.northconcepts.datapipeline.meter;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;

public class MeteredReader extends ProxyReader implements Metered
{
    private final RecordMeter meter;
    
    public MeteredReader(final DataReader reader) {
        super(reader);
        this.meter = new RecordMeter();
    }
    
    public RecordMeter getMeter() {
        return this.meter;
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        final Record record = this.meter.add(super.readImpl());
        return record;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.meter != null) {
            this.meter.addExceptionProperties(exception);
        }
        return super.addExceptionProperties(exception);
    }
    
    @Override
	public void close() throws DataException {
        this.meter.stopTimer();
        super.close();
    }
}
