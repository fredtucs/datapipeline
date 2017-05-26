package com.northconcepts.datapipeline.internal.expression.arithmetic;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public abstract class UnaryArithmeticExpression extends ArithmeticExpression
{
    private final ArithmeticExpression arithmeticExpression1;
    
    public UnaryArithmeticExpression(final ArithmeticExpression arithmeticExpression1) {
        super();
        this.arithmeticExpression1 = arithmeticExpression1;
    }
    
    public ArithmeticExpression getArithmeticExpression1() {
        return this.arithmeticExpression1;
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        return this.arithmeticExpression1.getType(expressionContext);
    }
}
