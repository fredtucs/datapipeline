package com.northconcepts.datapipeline.core;

import java.io.IOException;

import com.northconcepts.datapipeline.internal.lang.Util;

public final class StringParser implements IParser
{
    private final char[] cache;
    private int cacheIndex;
    
    public StringParser(final String text) {
        super();
        this.cacheIndex = 0;
        this.cache = text.toCharArray();
    }
    
    public DataException exception(final Throwable exception) {
        final DataException e = DataException.wrap(exception);
        e.set("StringParser.cache", this.cache);
        e.set("StringParser.cacheIndex", this.cacheIndex);
        e.set("StringParser.column ", this.getColumn());
        return e;
    }
    
    public boolean ready() throws IOException {
        return this.cache.length > this.cacheIndex;
    }
    
    public int getColumn() {
        return this.cacheIndex;
    }
    
    public int peek(final int index) {
        if (this.cache.length - this.cacheIndex <= index) {
            return -1;
        }
        return this.cache[this.cacheIndex + index];
    }
    
    public int pop() {
        if (this.cache.length - this.cacheIndex <= 0) {
            return -1;
        }
        return this.cache[this.cacheIndex++];
    }
    
    public void match(final char expectedChar, String failureMessage) {
        final int c = this.pop();
        if (c != expectedChar) {
            if (failureMessage == null) {
                failureMessage = "";
            }
            else {
                failureMessage += ": ";
            }
            throw new DataException(failureMessage + "expected " + Util.toPrintableString(expectedChar) + ", found " + Util.toPrintableString(c)).set("char", (char)c).set("printable char", Util.toPrintableString(c));
        }
    }
    
    public void match(final char expectedChar) {
        this.match(expectedChar, null);
    }
    
    public void consume() {
        this.pop();
    }
    
    public void consume(final int count) {
        for (int i = 0; i < count; ++i) {
            this.pop();
        }
    }
    
    public void consumeWhitespace() {
        while (Util.isWhitespace(this.peek(0))) {
            this.consume();
        }
    }
    
    public void consumeWhitespace(final int exception) {
        while (Util.isWhitespace(this.peek(0), exception)) {
            this.consume();
        }
    }
    
    public void close() {
    }
}
