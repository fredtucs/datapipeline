package com.northconcepts.datapipeline.internal.javabean.iterator;

public class RootIterator implements JavaIterator
{
    private final Object object;
    private Status status;
    private final String name;
    
    public RootIterator(final String name, final Object object) {
        super();
        this.status = Status.NEW;
        this.name = name;
        this.object = object;
    }
    
    public boolean next() {
        if (this.status == Status.NEW) {
            this.status = Status.OPENED;
            return true;
        }
        if (this.status == Status.OPENED) {
            this.status = Status.CLOSED;
            return false;
        }
        return false;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Object getValue() {
        if (this.status == Status.OPENED) {
            return this.object;
        }
        return null;
    }
    
    public enum Status
    {
        NEW, 
        OPENED, 
        CLOSED;
    }
}
