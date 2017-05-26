package com.northconcepts.datapipeline.internal.lang;

public class Pointer<T>
{
    public T value;
    
    public Pointer(final T value) {
        super();
        this.value = value;
    }
    
    @Override
	public String toString() {
        return (this.value == null) ? "null" : this.value.toString();
    }
}
