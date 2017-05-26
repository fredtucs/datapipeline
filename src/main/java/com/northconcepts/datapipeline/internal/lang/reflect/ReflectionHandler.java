package com.northconcepts.datapipeline.internal.lang.reflect;

public interface ReflectionHandler
{
    void startReflection(Object p0) throws Throwable;
    
    void endReflection(Object p0) throws Throwable;
    
    void enterClass(Object p0, String p1, Class<?> p2, int p3, Object p4, int[] p5) throws Throwable;
    
    void leaveClass(Object p0, String p1, Class<?> p2, int p3, Object p4, int[] p5) throws Throwable;
    
    void enterField(Object p0, String p1, Class<?> p2, int p3, Object p4, int[] p5, boolean p6) throws Throwable;
    
    void leaveField(Object p0, String p1, Class<?> p2, int p3, Object p4, int[] p5, boolean p6) throws Throwable;
    
    void enterArrayElement(Object p0, String p1, Class<?> p2, int p3, Object p4, int[] p5, int p6, Object p7) throws Throwable;
    
    void leaveArrayElement(Object p0, String p1, Class<?> p2, int p3, Object p4, int[] p5, int p6, Object p7) throws Throwable;
}
