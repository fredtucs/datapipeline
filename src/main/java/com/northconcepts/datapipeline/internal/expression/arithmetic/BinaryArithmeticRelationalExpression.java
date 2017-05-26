package com.northconcepts.datapipeline.internal.expression.arithmetic;

import com.northconcepts.datapipeline.internal.expression.logical.RelationalExpression;

public abstract class BinaryArithmeticRelationalExpression extends RelationalExpression
{
    private final ArithmeticExpression arithmeticExpression1;
    private final ArithmeticExpression arithmeticExpression2;
    
    public BinaryArithmeticRelationalExpression(final ArithmeticExpression arithmeticExpression1, final ArithmeticExpression arithmeticExpression2) {
        super();
        this.arithmeticExpression1 = arithmeticExpression1;
        this.arithmeticExpression2 = arithmeticExpression2;
    }
    
    public ArithmeticExpression getArithmeticExpression1() {
        return this.arithmeticExpression1;
    }
    
    public ArithmeticExpression getArithmeticExpression2() {
        return this.arithmeticExpression2;
    }
}
