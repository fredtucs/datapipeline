package com.northconcepts.datapipeline.transform;

import java.util.Collection;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;

public class IncludeFields extends Transformer
{
    private final FieldList fields;
    private boolean lenient;
    
    public IncludeFields(final Collection<String> fieldNames) {
        super();
        this.fields = new FieldList();
        this.add(fieldNames);
    }
    
    public IncludeFields(final String... fieldNames) {
        super();
        this.fields = new FieldList();
        this.add(fieldNames);
    }
    
    public boolean isLenient() {
        return this.lenient;
    }
    
    public IncludeFields setLenient(final boolean lenient) {
        this.lenient = lenient;
        return this;
    }
    
    public FieldList getFields() {
        return this.fields;
    }
    
    public IncludeFields add(final Collection<String> fieldNames) {
        if (fieldNames != null) {
            for (final String fieldName : fieldNames) {
                this.fields.add(fieldName);
            }
        }
        return this;
    }
    
    public IncludeFields add(final String... fieldNames) {
        for (int i = 0; i < fieldNames.length; ++i) {
            this.fields.add(fieldNames[i]);
        }
        return this;
    }
    
    @Override
	public boolean transform(final Record record) throws Throwable {
        record.selectFields(this.fields, this.lenient);
        return true;
    }
    
    @Override
	public String toString() {
        return "include [" + this.fields + "]";
    }
}
