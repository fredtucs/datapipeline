package com.northconcepts.datapipeline.internal.javabean.iterator;

public class SingleObjectIterator implements JavaIterator
{
    private Object value;
    private String name;
    private int count;
    
    public SingleObjectIterator(final String name, final Object value) {
        super();
        this.count = 0;
        this.name = name;
        this.value = value;
    }
    
    public boolean next() {
        ++this.count;
        return this.count < 2;
    }
    
    public String getName() {
        String result = null;
        if (this.count == 1) {
            result = this.name;
        }
        return result;
    }
    
    public Object getValue() {
        Object result = null;
        if (this.count == 1) {
            result = this.value;
        }
        return result;
    }
}
