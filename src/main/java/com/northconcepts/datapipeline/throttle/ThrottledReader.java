package com.northconcepts.datapipeline.throttle;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.meter.MeteredReader;

public class ThrottledReader extends MeteredReader implements Throttled
{
    private final Throttle throttle;
    
    public ThrottledReader(final DataReader reader, final int bytesPerSecond) {
        super(reader);
        this.throttle = new Throttle(this.getMeter(), bytesPerSecond);
    }
    
    public Throttle getThrottle() {
        return this.throttle;
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        final Record record = super.readImpl();
        this.throttle.throttle();
        return record;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.throttle != null) {
            this.throttle.addExceptionProperties(exception);
        }
        return super.addExceptionProperties(exception);
    }
}
