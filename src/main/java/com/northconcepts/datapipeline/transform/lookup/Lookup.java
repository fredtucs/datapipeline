package com.northconcepts.datapipeline.transform.lookup;

import java.util.List;

import com.northconcepts.datapipeline.core.RecordList;

public abstract class Lookup
{
    public abstract RecordList get(final Object... p0);
    
    public RecordList get(final List<?> keys) {
        if (keys == null || keys.size() == 0) {
            return this.get((Object[])null);
        }
        return this.get(keys.toArray());
    }
}
