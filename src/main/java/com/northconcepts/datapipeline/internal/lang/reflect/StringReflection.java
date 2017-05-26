package com.northconcepts.datapipeline.internal.lang.reflect;

import java.lang.reflect.Array;

import com.northconcepts.datapipeline.internal.lang.Util;

public class StringReflection implements ReflectionHandler
{
    private static final Class<Object> OBJECT_CLASS;
    private final StringBuilder buffer;
    
    public StringReflection() {
        super();
        this.buffer = new StringBuilder(8192);
    }
    
    public StringBuilder getBuffer() {
        return this.buffer;
    }
    
    public void startReflection(final Object object) throws Throwable {
        if (this.buffer.length() > 0) {
            this.buffer.setLength(0);
        }
    }
    
    public void endReflection(final Object object) throws Throwable {
    }
    
    public void enterClass(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex) throws Throwable {
        this.buffer.append(Util.repeat("    ", objectDepth) + objectName + getArrayIndexPart(objectArrayIndex) + ":" + getIdentity(object, objectType.getName()) + "[").append("\n");
    }
    
    public void leaveClass(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex) throws Throwable {
        this.buffer.append(Util.repeat("    ", objectDepth) + "]").append("\n");
    }
    
    public void enterField(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final boolean previouslyVisited) throws Throwable {
        final String objectTypeName = (objectType == null) ? "" : objectType.getName();
        if (previouslyVisited) {
            this.buffer.append(Util.repeat("    ", objectDepth) + objectName + ":" + getIdentity(object, objectTypeName) + "=[...]").append("\n");
        }
        else if (object == null || objectType == StringReflection.OBJECT_CLASS || ObjectReflector.isPrimitive(objectType)) {
            final String parentTypeName = "";
            this.buffer.append(Util.repeat("    ", objectDepth) + objectName + getArrayIndexPart(objectArrayIndex) + ":" + getIdentity(object, objectTypeName) + "=[" + object + "]" + parentTypeName).append("\n");
        }
        else if (objectType.isArray() && Array.getLength(object) == 0) {
            this.buffer.append(Util.repeat("    ", objectDepth) + objectName + getArrayIndexPart(objectArrayIndex) + ":" + getIdentity(object, objectTypeName) + "={}").append("\n");
        }
    }
    
    public void leaveField(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final boolean previouslyVisited) throws Throwable {
    }
    
    public void enterArrayElement(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final int arrayLength, final Object array) throws Throwable {
    }
    
    public void leaveArrayElement(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final int arrayLength, final Object array) throws Throwable {
    }
    
    private static final String getArrayIndexPart(final int[] objectArrayIndex) throws Throwable {
        if (objectArrayIndex == null || objectArrayIndex.length == 0) {
            return "";
        }
        return "[" + Util.arrayToString(objectArrayIndex, "][") + "]";
    }
    
    private static final String getIdentity(final Object o, final String objectTypeName) {
        return objectTypeName + "@" + Util.padLeft(Integer.toHexString(System.identityHashCode(o)).toUpperCase(), 8, '0');
    }
    
    @Override
	public String toString() {
        return this.buffer.toString();
    }
    
    static {
        OBJECT_CLASS = Object.class;
    }
}
