package com.northconcepts.datapipeline.internal.expression.logical;

public abstract class UnaryLogicalExpression extends LogicalExpression
{
    private final LogicalExpression logicalExpression1;
    
    public UnaryLogicalExpression(final LogicalExpression logicalExpression1) {
        super();
        this.logicalExpression1 = logicalExpression1;
    }
    
    public LogicalExpression getLogicalExpression1() {
        return this.logicalExpression1;
    }
}
