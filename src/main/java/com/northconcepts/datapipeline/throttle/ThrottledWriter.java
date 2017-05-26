package com.northconcepts.datapipeline.throttle;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.meter.MeteredWriter;

public class ThrottledWriter extends MeteredWriter implements Throttled
{
    private final Throttle throttle;
    
    public ThrottledWriter(final DataWriter writer, final int bytesPerSecond) {
        super(writer);
        this.throttle = new Throttle(this.getMeter(), bytesPerSecond);
    }
    
    public Throttle getThrottle() {
        return this.throttle;
    }
    
    @Override
	protected Record interceptRecord(final Record record) throws Throwable {
        this.throttle.throttle();
        return super.interceptRecord(record);
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.throttle != null) {
            this.throttle.addExceptionProperties(exception);
        }
        return super.addExceptionProperties(exception);
    }
}
