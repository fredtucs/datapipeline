package com.northconcepts.datapipeline.internal.javabean.iterator;

import java.lang.reflect.Array;

public class ArrayIterator implements JavaIterator
{
    private final Object array;
    private final int length;
    private final Class<?> componentType;
    private int nextIndex;
    private Object currentElement;
    
    public ArrayIterator(final Object array) {
        super();
        this.nextIndex = -1;
        this.array = array;
        this.length = Array.getLength(array);
        this.componentType = array.getClass().getComponentType();
    }
    
    public boolean next() {
        if (this.nextIndex + 1 >= this.length) {
            return false;
        }
        ++this.nextIndex;
        this.currentElement = Array.get(this.array, this.nextIndex);
        return true;
    }
    
    public String getName() {
        if (this.currentElement != null) {
            return this.currentElement.getClass().getSimpleName();
        }
        return this.componentType.getSimpleName();
    }
    
    public Object getValue() {
        return this.currentElement;
    }
}
