package com.northconcepts.datapipeline.internal.javabean.iterator;

import java.util.Iterator;
import java.util.Map;

public class MapIterator implements JavaIterator
{
    private Iterator<?> entryIterator;
    private Map.Entry<?, ?> currentEntry;
    
    public MapIterator(final Map<?, ?> map) {
        super();
        this.entryIterator = map.entrySet().iterator();
    }
    
    public boolean next() {
        if (this.entryIterator.hasNext()) {
            this.currentEntry = (Map.Entry<?, ?>)this.entryIterator.next();
            return true;
        }
        return false;
    }
    
    public String getName() {
        return "entry";
    }
    
    public Object getValue() {
        return this.currentEntry;
    }
}
