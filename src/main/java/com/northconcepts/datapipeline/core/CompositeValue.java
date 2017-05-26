package com.northconcepts.datapipeline.core;

import java.util.Arrays;
import java.util.List;

import com.northconcepts.datapipeline.internal.lang.Util;

public class CompositeValue
{
    private final Object[] values;
    private int hashCode;
    
    public CompositeValue(final int valueCount) {
        super();
        this.values = new Object[valueCount];
    }
    
    public CompositeValue(final List<?> values) {
        super();
        this.values = values.toArray();
    }
    
    public CompositeValue(final Object... values) {
        super();
        this.values = values;
    }
    
    public CompositeValue set(final int index, final Object value) {
        this.values[index] = value;
        this.hashCode = 0;
        return this;
    }
    
    public Object get(final int index) {
        return this.values[index];
    }
    
    public Object[] getAll() {
        return this.values;
    }
    
    public int size() {
        return this.values.length;
    }
    
    @Override
	public int hashCode() {
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        if (this.values.length == 1) {
            this.hashCode = this.values[0].hashCode();
        }
        else {
            this.hashCode = 1;
            for (int i = 0; i < this.values.length; ++i) {
                final Object k = this.values[i];
                this.hashCode = 31 * this.hashCode + ((k == null) ? 0 : k.hashCode());
            }
        }
        return this.hashCode;
    }
    
    @Override
	public boolean equals(final Object o) {
        if (!(o instanceof CompositeValue)) {
            return this.values.length == 1 && Util.equals(o, this.values[0]);
        }
        final CompositeValue compositeKey = (CompositeValue)o;
        return Arrays.equals(this.values, compositeKey.values);
    }
}
