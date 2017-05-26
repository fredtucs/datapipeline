package com.northconcepts.datapipeline.transform.lookup;

import com.northconcepts.datapipeline.core.RecordList;

public class ProxyLookup extends Lookup
{
    private Lookup nestedLookup;
    
    public ProxyLookup(final Lookup nestedLookup) {
        super();
        this.nestedLookup = nestedLookup;
    }
    
    public Lookup getNestedLookup() {
        return this.nestedLookup;
    }
    
    public ProxyLookup setNestedLookup(final Lookup nestedLookup) {
        this.nestedLookup = nestedLookup;
        return this;
    }
    
    @Override
	public RecordList get(final Object... args) {
        return this.nestedLookup.get(args);
    }
}
