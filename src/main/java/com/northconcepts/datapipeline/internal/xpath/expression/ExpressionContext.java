package com.northconcepts.datapipeline.internal.xpath.expression;

public abstract class ExpressionContext
{
    public abstract Class<?> getType(final String p0);
    
    public abstract Object getValue(final String p0);
    
    public abstract boolean containsKey(final String p0);
}
