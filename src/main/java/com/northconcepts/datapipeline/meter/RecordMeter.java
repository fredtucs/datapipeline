package com.northconcepts.datapipeline.meter;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;

public class RecordMeter extends Meter
{
    private MeterUnit measure;
    
    public RecordMeter() {
        super();
        this.measure = MeterUnit.BYTES;
    }
    
    public MeterUnit getMeasure() {
        return this.measure;
    }
    
    public RecordMeter setMeasure(final MeterUnit measure) {
        this.measure = measure;
        return this;
    }
    
    @Override
	public String getUnitsPerSecondAsString() {
        return this.getUnitsPerSecondAsString(this.measure.toString().toLowerCase());
    }
    
    public Record add(final Record record) {
        if (record != null) {
            if (this.measure == MeterUnit.BYTES) {
                this.add(record.getSizeInBytes());
            }
            else {
                this.add();
            }
        }
        return record;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("RecordMeter.measure", this.getMeasure());
        return super.addExceptionProperties(exception);
    }
    
    public enum MeterUnit
    {
        BYTES, 
        RECORDS;
    }
}
