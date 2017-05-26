package com.northconcepts.datapipeline.core;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

final class NullReader extends Reader
{
    public NullReader() {
        super();
    }
    
    public NullReader(final Object lock) {
        super(lock);
    }
    
    @Override
	public void close() throws IOException {
    }
    
    @Override
	public int read() throws IOException {
        return -1;
    }
    
    @Override
	public int read(final char[] cbuf, final int off, final int len) throws IOException {
        return -1;
    }
    
    @Override
	public int read(final char[] cbuf) throws IOException {
        return -1;
    }
    
    @Override
	public int read(final CharBuffer target) throws IOException {
        return -1;
    }
}
