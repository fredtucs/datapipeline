package com.northconcepts.datapipeline.internal.lang.reflect;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.internal.lang.Util;

public class DebugReflection implements ReflectionHandler
{
    public static final Logger log;
    private int depth;
    
    public DebugReflection() {
        super();
        this.depth = 0;
    }
    
    private static String getName(String objectName, final int[] objectArrayIndex, final Class<?> objectType) throws Throwable {
        final String objectTypeName = (objectType == null) ? "???" : objectType.getName();
        if (objectArrayIndex != null && objectArrayIndex.length > 0) {
            objectName = objectName + "[" + Util.arrayToString(objectArrayIndex, "][") + "]";
        }
        return objectName + ":" + objectTypeName;
    }
    
    private void enter(final String method, final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex) throws Throwable {
        DebugReflection.log.debug(Util.repeat("  ", this.depth) + method + "[\"" + getName(objectName, objectArrayIndex, objectType) + "\"]");
        ++this.depth;
    }
    
    private void leave(final String method, final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex) throws Throwable {
        --this.depth;
        DebugReflection.log.debug(Util.repeat("  ", this.depth) + method + "[\"" + getName(objectName, objectArrayIndex, objectType) + "\"]");
    }
    
    public void startReflection(final Object object) throws Throwable {
    }
    
    public void endReflection(final Object object) throws Throwable {
    }
    
    public void enterClass(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex) throws Throwable {
        this.enter("enterClass", objectParent, objectName, objectType, objectDepth, object, objectArrayIndex);
    }
    
    public void leaveClass(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex) throws Throwable {
        this.leave("leaveClass", objectParent, objectName, objectType, objectDepth, object, objectArrayIndex);
    }
    
    public void enterField(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final boolean previouslyVisited) throws Throwable {
        this.enter("enterField", objectParent, objectName, objectType, objectDepth, object, objectArrayIndex);
    }
    
    public void leaveField(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final boolean previouslyVisited) throws Throwable {
        this.leave("leaveField", objectParent, objectName, objectType, objectDepth, object, objectArrayIndex);
    }
    
    public void enterArrayElement(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final int arrayLength, final Object array) throws Throwable {
        this.enter("enterArrayElement", objectParent, objectName, objectType, objectDepth, object, objectArrayIndex);
    }
    
    public void leaveArrayElement(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final int arrayLength, final Object array) throws Throwable {
        this.leave("leaveArrayElement", objectParent, objectName, objectType, objectDepth, object, objectArrayIndex);
    }
    
    static {
        log = DataEndpoint.log;
    }
}
