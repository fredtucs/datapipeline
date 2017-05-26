package com.northconcepts.datapipeline.internal.javabean.iterator;

import java.util.Map;

public class MapEntryIterator implements JavaIterator
{
    private Map.Entry<?, ?> entry;
    private Status status;
    
    public MapEntryIterator(final Map.Entry<?, ?> entry) {
        super();
        this.status = Status.NEW;
        this.entry = entry;
    }
    
    public boolean next() {
        if (this.status.ordinal() < Status.CLOSED.ordinal()) {
            this.status = this.status.next();
            return true;
        }
        return false;
    }
    
    public String getName() {
        return this.status.getName();
    }
    
    public Object getValue() {
        if (this.status == Status.KEY) {
            return this.entry.getKey();
        }
        if (this.status == Status.VALUE) {
            return this.entry.getValue();
        }
        return null;
    }
    
    public enum Status
    {
        NEW, 
        KEY, 
        VALUE, 
        CLOSED;
        
        public String getName() {
            return this.name().toLowerCase();
        }
        
        public Status next() {
            if (this.ordinal() + 1 < values().length) {
                return values()[this.ordinal() + 1];
            }
            return Status.CLOSED;
        }
    }
}
