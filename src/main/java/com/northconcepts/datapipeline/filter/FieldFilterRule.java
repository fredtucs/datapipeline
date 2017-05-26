package com.northconcepts.datapipeline.filter;

import com.northconcepts.datapipeline.core.Field;

public abstract class FieldFilterRule
{
    private FieldFilter fieldFilter;
    
    public FieldFilter getFieldFilter() {
        return this.fieldFilter;
    }
    
    void setFieldFilter(final FieldFilter fieldFilter) {
        this.fieldFilter = fieldFilter;
    }
    
    public abstract boolean allow(final Field p0);
}
