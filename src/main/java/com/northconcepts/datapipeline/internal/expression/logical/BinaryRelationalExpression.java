package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.internal.expression.Expression;

public abstract class BinaryRelationalExpression extends RelationalExpression
{
    private final Expression expression1;
    private final Expression expression2;
    
    public BinaryRelationalExpression(final Expression expression1, final Expression expression2) {
        super();
        this.expression1 = expression1;
        this.expression2 = expression2;
    }
    
    public Expression getExpression1() {
        return this.expression1;
    }
    
    public Expression getExpression2() {
        return this.expression2;
    }
}
