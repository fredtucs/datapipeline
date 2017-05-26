package com.northconcepts.datapipeline.transform.lookup;

import java.util.HashMap;

import com.northconcepts.datapipeline.core.CompositeValue;
import com.northconcepts.datapipeline.core.RecordList;

public class CachedLookup extends ProxyLookup
{
    private final HashMap<CompositeValue, RecordList> cache;
    
    public CachedLookup(final Lookup nestedLookup) {
        super(nestedLookup);
        this.cache = new HashMap<CompositeValue, RecordList>();
    }
    
    public void clear() {
        this.cache.clear();
    }
    
    @Override
	public RecordList get(final Object... args) {
        final CompositeValue key = new CompositeValue(args);
        RecordList list = this.cache.get(key);
        if (list != null) {
            return list;
        }
        list = super.get(args);
        this.put(key, list);
        return list;
    }
    
    private CachedLookup put(final CompositeValue key, final RecordList list) {
        this.cache.put(key, list);
        return this;
    }
}
