package com.northconcepts.datapipeline.expression;

public interface ASTNode
{
    String getSourceString();
    
    boolean isQuantity();
}
