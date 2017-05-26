package com.northconcepts.datapipeline.internal.javabean.iterator;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import com.northconcepts.datapipeline.core.DataException;

public class PropertyIterator implements JavaIterator
{
    private final Object object;
    private final PropertyDescriptor[] properties;
    private int nextIndex;
    private PropertyDescriptor currentProperty;
    
    public PropertyIterator(final Object object) throws Throwable {
        super();
        this.nextIndex = -1;
        this.object = object;
        final BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        this.properties = beanInfo.getPropertyDescriptors();
    }
    
    public boolean next() {
        if (this.nextIndex + 1 >= this.properties.length) {
            return false;
        }
        ++this.nextIndex;
        this.currentProperty = this.properties[this.nextIndex];
        return (!"class".equals(this.currentProperty.getName()) && this.currentProperty.getReadMethod() != null) || this.next();
    }
    
    public String getName() {
        if (this.currentProperty == null) {
            return null;
        }
        return this.currentProperty.getName();
    }
    
    public Object getValue() {
        try {
            if (this.currentProperty == null) {
                return null;
            }
            return this.currentProperty.getReadMethod().invoke(this.object, new Object[0]);
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
}
