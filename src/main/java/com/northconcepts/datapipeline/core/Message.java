package com.northconcepts.datapipeline.core;

import java.util.Date;

import com.northconcepts.datapipeline.internal.lang.Util;

public class Message
{
    private final Date timestamp;
    private final Level level;
    private final String thread;
    private final String message;
    private final Throwable exception;
    private final Throwable stackTrace;
    
    public Message(final Level level, final String message, final Throwable exception) {
        super();
        this.timestamp = new Date();
        this.level = level;
        this.thread = Thread.currentThread().getName();
        this.message = message;
        this.exception = exception;
        this.stackTrace = exception;
    }
    
    public Message(final Level level, final Throwable exception) {
        this(level, exception.getMessage(), exception);
    }
    
    public Message(final Level level, final String message, final boolean recordStackTrace) {
        super();
        this.timestamp = new Date();
        this.level = level;
        this.thread = Thread.currentThread().getName();
        this.message = message;
        this.exception = null;
        this.stackTrace = (recordStackTrace ? new Throwable("stackTrace") : null);
    }
    
    public Message(final Level level, final String message) {
        this(level, message, false);
    }
    
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public String getThread() {
        return this.thread;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public Throwable getStackTrace() {
        return this.stackTrace;
    }
    
    public Throwable getException() {
        return this.exception;
    }
    
    @Override
	public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append(Util.yyyyMMddHHmmssSSS.format(this.timestamp)).append(" ");
        s.append(this.getLevel()).append(" ");
        s.append(this.thread).append(" ");
        s.append(this.message);
        if (this.exception != null) {
            s.append(" ").append(this.exception.getMessage());
        }
        if (this.stackTrace != null) {
            final StackTraceElement[] elements = this.stackTrace.getStackTrace();
            for (int i = 0; i < elements.length; ++i) {
                if (i == 0) {
                    s.append(Util.LINE_SEPARATOR);
                }
                s.append("    at ").append(elements[i]).append(Util.LINE_SEPARATOR);
            }
        }
        return s.toString();
    }
    
    public enum Level
    {
        DEBUG, 
        INFO, 
        WARNING, 
        ERROR, 
        CRITICAL;
    }
}
