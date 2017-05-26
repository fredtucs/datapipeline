package com.northconcepts.datapipeline.internal.javabean.iterator;

public class JavaIteratorContext
{
    private final JavaIterator iterator;
    private final int depth;
    private final int child;
    
    public JavaIteratorContext(final JavaIterator iterator, final int depth, final int child) {
        super();
        this.iterator = iterator;
        this.depth = depth;
        this.child = child;
    }
    
    public JavaIterator getIterator() {
        return this.iterator;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public int getChild() {
        return this.child;
    }
}
