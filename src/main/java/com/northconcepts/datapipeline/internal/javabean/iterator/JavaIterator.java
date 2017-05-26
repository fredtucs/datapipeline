package com.northconcepts.datapipeline.internal.javabean.iterator;

public interface JavaIterator
{
    boolean next();
    
    String getName();
    
    Object getValue();
}
