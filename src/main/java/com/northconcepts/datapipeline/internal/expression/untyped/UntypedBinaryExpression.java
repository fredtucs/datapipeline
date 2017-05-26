package com.northconcepts.datapipeline.internal.expression.untyped;

import com.northconcepts.datapipeline.internal.expression.Expression;

public abstract class UntypedBinaryExpression extends Expression
{
    private final Expression expression1;
    private final Expression expression2;
    
    public UntypedBinaryExpression(final Expression expression1, final Expression expression2) {
        super();
        this.expression1 = expression1;
        this.expression2 = expression2;
    }
    
    public final Expression getExpression1() {
        return this.expression1;
    }
    
    public final Expression getExpression2() {
        return this.expression2;
    }
}
