package com.northconcepts.datapipeline.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.northconcepts.datapipeline.internal.lang.Util;

public class FieldList implements Cloneable
{
    private final ArrayList<String> list;
    
    public FieldList() {
        super();
        this.list = new ArrayList<String>();
    }
    
    public FieldList(final String firstFieldName, final String... otherFieldNames) {
        super();
        this.list = new ArrayList<String>();
        this.add(firstFieldName);
        this.add(otherFieldNames);
    }
    
    private FieldList(final FieldList fieldList) {
        super();
        (this.list = new ArrayList<String>()).addAll(fieldList.list);
    }
    
    @Override
	public FieldList clone() {
        return new FieldList(this);
    }
    
    public FieldList add(final String... fieldNames) {
        for (int i = 0; i < fieldNames.length; ++i) {
            this.list.add(fieldNames[i]);
        }
        return this;
    }
    
    public FieldList add(final Collection<String> fieldNames) {
        this.list.addAll(fieldNames);
        return this;
    }
    
    public FieldList remove(final String... fieldNames) {
        this.list.removeAll(Arrays.asList(fieldNames));
        return this;
    }
    
    public FieldList remove(final FieldList fieldList) {
        this.list.removeAll(fieldList.list);
        return this;
    }
    
    public boolean contains(final String... fieldNames) {
        for (int i = 0; i < fieldNames.length; ++i) {
            if (!this.list.contains(fieldNames[i])) {
                return false;
            }
        }
        return true;
    }
    
    public int size() {
        return this.list.size();
    }
    
    public String get(final int index) {
        return this.list.get(index);
    }
    
    public List<String> getList() {
        return this.list;
    }
    
    @Override
	public boolean equals(final Object o) {
        return this == o || (o != null && this.list.equals(((FieldList)o).list));
    }
    
    @Override
	public int hashCode() {
        return this.list.hashCode();
    }
    
    public String toString(final String elementFormat, final String fieldSeparator) {
        return Util.collectionToString(this.list, elementFormat, fieldSeparator);
    }
    
    public String toString(final String fieldSeparator) {
        return Util.collectionToString(this.list, fieldSeparator);
    }
    
    @Override
	public String toString() {
        return Util.collectionToString(this.list, ", ");
    }
}
