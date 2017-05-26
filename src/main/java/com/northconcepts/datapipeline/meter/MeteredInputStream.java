package com.northconcepts.datapipeline.meter;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MeteredInputStream extends FilterInputStream implements Metered
{
    private final Meter meter;
    
    public MeteredInputStream(final InputStream in) {
        super(in);
        this.meter = new Meter();
    }
    
    public Meter getMeter() {
        return this.meter;
    }
    
    @Override
	public int read() throws IOException {
        final int b = super.read();
        this.meter.add();
        return b;
    }
    
    @Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
        final int count = super.read(b, off, len);
        this.meter.add(count);
        return count;
    }
    
    @Override
	public long skip(final long n) throws IOException {
        final long count = super.skip(n);
        this.meter.add(count);
        return count;
    }
    
    @Override
	public synchronized void reset() throws IOException {
        super.reset();
        this.meter.reset();
    }
    
    @Override
	public void close() throws IOException {
        this.meter.stopTimer();
        super.close();
    }
}
