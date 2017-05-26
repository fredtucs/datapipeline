package com.northconcepts.datapipeline.core;

import java.io.IOException;
import java.io.Reader;

import com.northconcepts.datapipeline.internal.lang.Util;

public class Parser implements IParser
{
    public static final int EOF = -1;
    private final Reader reader;
    private final StringBuilder cache;
    private int cacheIndex;
    private int column;
    
    public Parser(final Reader reader) {
        super();
        this.cacheIndex = 0;
        this.column = 0;
        this.reader = reader;
        this.cache = new StringBuilder();
    }
    
    public Parser(final String text) {
        super();
        this.cacheIndex = 0;
        this.column = 0;
        this.reader = new NullReader();
        this.cache = new StringBuilder(text);
    }
    
    public DataException exception(final Throwable exception) {
        final DataException e = DataException.wrap(exception);
        e.set("Parser.cache", this.cache);
        e.set("Parser.cacheIndex", this.cacheIndex);
        e.set("Parser.column ", this.column);
        return e;
    }
    
    public boolean ready() throws IOException {
        return this.getCacheLength() > 0 || this.reader.ready();
    }
    
    private final int getCacheLength() {
        return this.cache.length() - this.cacheIndex;
    }
    
    private final char getFromCache(final int index) {
        return this.cache.charAt(this.cacheIndex + index);
    }
    
    public int getColumn() {
        return this.column;
    }
    
    public void resetColumn() {
        this.column = 0;
        this.pruneCache();
    }
    
    public void pruneCache() {
        this.cache.delete(0, this.cacheIndex);
        this.cacheIndex = 0;
    }
    
    public int peek(final int index) {
        try {
            while (this.getCacheLength() <= index) {
                final int c = this.reader.read();
                if (c == -1) {
                    return -1;
                }
                this.cache.append((char)c);
            }
            return this.getFromCache(index);
        }
        catch (IOException e) {
            throw this.exception(e).set("peek index", index);
        }
    }
    
    public int pop() {
        ++this.column;
        try {
            if (this.getCacheLength() > 0) {
                final int c = this.cache.charAt(this.cacheIndex++);
                return c;
            }
            return this.reader.read();
        }
        catch (IOException e) {
            throw this.exception(e);
        }
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
        try {
            this.cache.setLength(0);
            this.cacheIndex = 0;
            this.reader.close();
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
}
