package com.northconcepts.datapipeline.core;

import java.util.HashMap;

public class FieldDeclarations implements Cloneable
{
    private final HashMap<String, Class<?>> fields;
    
    public FieldDeclarations() {
        super();
        this.fields = new HashMap<String, Class<?>>();
    }
    
    private FieldDeclarations(final FieldDeclarations declarations) {
        super();
        (this.fields = new HashMap<String, Class<?>>()).putAll(declarations.fields);
    }
    
    @Override
	public Object clone() {
        return new FieldDeclarations(this);
    }
    
    public FieldDeclarations set(final String fieldName, final Class<?> type) {
        this.fields.put(fieldName, type);
        return this;
    }
    
    public int getCount() {
        return this.fields.size();
    }
    
    public Class<?> get(final String fieldName) {
        return this.fields.get(fieldName);
    }
    
    public HashMap<String, Class<?>> getFields() {
        return this.fields;
    }
    
    @Override
	public boolean equals(final Object o) {
        return this == o || (o != null && this.fields.equals(((FieldDeclarations)o).fields));
    }
    
    @Override
	public int hashCode() {
        return this.fields.hashCode();
    }
}
