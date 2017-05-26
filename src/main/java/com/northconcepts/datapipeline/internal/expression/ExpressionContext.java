package com.northconcepts.datapipeline.internal.expression;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.lang.Util;

public abstract class ExpressionContext
{
    public static final ExpressionContext NULL;
    
    public abstract Class<?> getType(final String p0);
    
    public abstract Object getValue(final String p0);
    
    public abstract boolean containsKey(final String p0);
    
    private static Object getJavaBeanValue(final Object object, final String property) {
        if (object == null) {
            return null;
        }
        if (Util.isEmpty(property)) {
            throw new DataException("property is empty");
        }
        final int index = property.indexOf(46);
        final boolean isNestedProperty = index > 0;
        String propertyName;
        if (isNestedProperty) {
            propertyName = property.substring(0, index);
        }
        else {
            propertyName = property;
        }
        final Class<?> beanClass = object.getClass();
        Object value = null;
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            final PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
            int i = 0;
            while (i < properties.length) {
                final PropertyDescriptor descriptor = properties[i];
                if (Util.equals(propertyName, descriptor.getName())) {
                    final Method readMethod = descriptor.getReadMethod();
                    if (readMethod == null) {
                        throw new DataException("no getter for Java bean property");
                    }
                    value = readMethod.invoke(object, new Object[0]);
                    if (isNestedProperty) {
                        return getJavaBeanValue(value, property.substring(index + 1));
                    }
                    return value;
                }
                else {
                    ++i;
                }
            }
            throw new DataException("no such Java bean property");
        }
        catch (Throwable e) {
            throw DataException.wrap(e).set("class", beanClass).set("property", property).set("propertyName", propertyName);
        }
    }
    
    static {
        NULL = new ExpressionContext() {
            @Override
			public boolean containsKey(final String name) {
                return false;
            }
            
            @Override
			public Class<?> getType(final String name) {
                return null;
            }
            
            @Override
			public Object getValue(final String name) {
                return null;
            }
        };
    }
    
    public static class Helper
    {
        public static Class<?> getType(final ExpressionContext context, final String name) {
            if ("null".equalsIgnoreCase(name)) {
                return null;
            }
            final int index = name.indexOf(46);
            if (index > 0) {
                final Class<?> type = context.getType(name.substring(0, index));
                return getJavaBeanType(type, name.substring(index + 1));
            }
            return context.getType(name);
        }
        
        public static Object getValue(final ExpressionContext context, final String name) {
            if ("null".equalsIgnoreCase(name)) {
                return null;
            }
            final int index = name.indexOf(46);
            if (index > 0) {
                final Object object = context.getValue(name.substring(0, index));
                return getJavaBeanValue(object, name.substring(index + 1));
            }
            return context.getValue(name);
        }
        
        private static Class<?> getJavaBeanType(final Class<?> type, final String property) {
            if (type == null) {
                return null;
            }
            if (Util.isEmpty(property)) {
                throw new DataException("property is empty");
            }
            final int index = property.indexOf(46);
            final boolean isNestedProperty = index > 0;
            String propertyName;
            if (isNestedProperty) {
                propertyName = property.substring(0, index);
            }
            else {
                propertyName = property;
            }
            Class<?> value = null;
            try {
                final BeanInfo beanInfo = Introspector.getBeanInfo(type);
                final PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
                int i = 0;
                while (i < properties.length) {
                    final PropertyDescriptor descriptor = properties[i];
                    if (Util.equals(propertyName, descriptor.getName())) {
                        final Method readMethod = descriptor.getReadMethod();
                        if (readMethod == null) {
                            throw new DataException("no getter for Java bean property");
                        }
                        value = descriptor.getPropertyType();
                        if (isNestedProperty) {
                            return getJavaBeanType(value, property.substring(index + 1));
                        }
                        return value;
                    }
                    else {
                        ++i;
                    }
                }
                throw new DataException("no such Java bean property");
            }
            catch (Throwable e) {
                throw DataException.wrap(e).set("class", type).set("property", property).set("propertyName", propertyName);
            }
        }
    }
}
