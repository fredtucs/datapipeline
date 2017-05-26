package com.northconcepts.datapipeline.throttle;

import java.io.IOException;
import java.io.OutputStream;

import com.northconcepts.datapipeline.meter.MeteredOutputStream;

public class ThrottledOutputStream extends MeteredOutputStream implements Throttled
{
    private final Throttle throttle;
    
    public ThrottledOutputStream(final OutputStream out, final int unitsPerSecond) {
        super(out);
        this.throttle = new Throttle(this.getMeter(), unitsPerSecond);
    }
    
    public Throttle getThrottle() {
        return this.throttle;
    }
    
    @Override
	public void write(final byte[] b, final int off, final int len) throws IOException {
        try {
            this.throttle.throttle();
        }
        catch (InterruptedException ex) {}
        super.write(b, off, len);
    }
    
    @Override
	public void write(final int b) throws IOException {
        try {
            this.throttle.throttle();
        }
        catch (InterruptedException ex) {}
        super.write(b);
    }
}
