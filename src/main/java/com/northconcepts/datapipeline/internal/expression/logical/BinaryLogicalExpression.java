package com.northconcepts.datapipeline.internal.expression.logical;

public abstract class BinaryLogicalExpression extends LogicalExpression
{
    private final LogicalExpression logicalExpression1;
    private final LogicalExpression logicalExpression2;
    
    public BinaryLogicalExpression(final LogicalExpression logicalExpression1, final LogicalExpression logicalExpression2) {
        super();
        this.logicalExpression1 = logicalExpression1;
        this.logicalExpression2 = logicalExpression2;
    }
    
    public LogicalExpression getLogicalExpression1() {
        return this.logicalExpression1;
    }
    
    public LogicalExpression getLogicalExpression2() {
        return this.logicalExpression2;
    }
}
