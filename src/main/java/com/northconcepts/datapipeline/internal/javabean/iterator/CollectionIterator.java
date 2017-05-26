package com.northconcepts.datapipeline.internal.javabean.iterator;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Iterator;

public class CollectionIterator implements JavaIterator
{
    private Iterator<?> iterator;
    private final Class<?> componentType;
    private Object currentElement;
    
    public CollectionIterator(final Iterable<?> collection) {
        super();
        this.iterator = collection.iterator();
        final TypeVariable<?>[] typeParameters = collection.getClass().getTypeParameters();
        if (typeParameters.length == 0) {
            this.componentType = Object.class;
        }
        else {
            final Type type = typeParameters[0].getBounds()[0];
            if (type instanceof Class) {
                this.componentType = (Class<?>)type;
            }
            else {
                this.componentType = Object.class;
            }
        }
    }
    
    public boolean next() {
        if (this.iterator.hasNext()) {
            this.currentElement = this.iterator.next();
            return true;
        }
        return false;
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
