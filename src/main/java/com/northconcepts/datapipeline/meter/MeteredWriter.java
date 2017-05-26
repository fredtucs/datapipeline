package com.northconcepts.datapipeline.meter;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.ProxyWriter;
import com.northconcepts.datapipeline.core.Record;

public class MeteredWriter extends ProxyWriter implements Metered
{
    private final RecordMeter meter;
    
    public MeteredWriter(final DataWriter writer) {
        super(writer);
        this.meter = new RecordMeter();
    }
    
    public RecordMeter getMeter() {
        return this.meter;
    }
    
    RecordMeter getMeterAsRecordMeter() {
        return this.meter;
    }
    
    @Override
	protected Record interceptRecord(final Record record) throws Throwable {
        this.meter.add(record);
        return super.interceptRecord(record);
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
