package com.northconcepts.datapipeline.throttle;

import java.io.IOException;
import java.io.InputStream;

import com.northconcepts.datapipeline.meter.MeteredInputStream;

public class ThrottledInputStream extends MeteredInputStream implements Throttled
{
    private final Throttle throttle;
    
    public Throttle getThrottle() {
        return this.throttle;
    }
    
    public ThrottledInputStream(final InputStream in, final int unitsPerSecond) {
        super(in);
        this.throttle = new Throttle(this.getMeter(), unitsPerSecond);
    }
    
    @Override
	public int read() throws IOException {
        final int result = super.read();
        try {
            this.throttle.throttle();
        }
        catch (InterruptedException ex) {}
        return result;
    }
    
    @Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
        final int result = super.read(b, off, len);
        try {
            this.throttle.throttle();
        }
        catch (InterruptedException ex) {}
        return result;
    }
}
