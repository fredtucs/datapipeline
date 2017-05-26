package com.northconcepts.datapipeline.transform;

import java.util.Collection;
import java.util.HashSet;

import com.northconcepts.datapipeline.core.Record;

public class ExcludeFields extends Transformer
{
    private final HashSet<String> fields;
    
    public ExcludeFields(final Collection<String> fieldNames) {
        super();
        this.fields = new HashSet<String>();
        this.add(fieldNames);
    }
    
    public ExcludeFields(final String... fieldNames) {
        super();
        this.fields = new HashSet<String>();
        this.add(fieldNames);
    }
    
    public ExcludeFields add(final Collection<String> fieldNames) {
        if (fieldNames != null) {
            for (final String fieldName : fieldNames) {
                this.fields.add(fieldName);
            }
        }
        return this;
    }
    
    public ExcludeFields add(final String... fieldNames) {
        for (int i = 0; i < fieldNames.length; ++i) {
            this.fields.add(fieldNames[i]);
        }
        return this;
    }
    
    @Override
	public boolean transform(final Record record) throws Throwable {
        record.excludeFields(this.fields);
        return true;
    }
    
    @Override
	public String toString() {
        return "exclude [" + this.fields + "]";
    }
}
