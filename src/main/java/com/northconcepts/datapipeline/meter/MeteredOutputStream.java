package com.northconcepts.datapipeline.meter;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MeteredOutputStream extends FilterOutputStream implements Metered
{
    private final Meter meter;
    
    public MeteredOutputStream(final OutputStream out) {
        super(out);
        this.meter = new Meter();
    }
    
    public Meter getMeter() {
        return this.meter;
    }
    
    @Override
	public void write(final int b) throws IOException {
        super.write(b);
        this.meter.add();
    }
    
    @Override
	public void write(final byte[] b, final int off, final int len) throws IOException {
        super.write(b, off, len);
        this.meter.add(len);
    }
    
    @Override
	public void close() throws IOException {
        this.meter.stopTimer();
        super.close();
    }
}
