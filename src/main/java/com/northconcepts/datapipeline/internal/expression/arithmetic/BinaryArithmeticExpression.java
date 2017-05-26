package com.northconcepts.datapipeline.internal.expression.arithmetic;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public abstract class BinaryArithmeticExpression extends ArithmeticExpression
{
    private final ArithmeticExpression arithmeticExpression1;
    private final ArithmeticExpression arithmeticExpression2;
    
    public BinaryArithmeticExpression(final ArithmeticExpression arithmeticExpression1, final ArithmeticExpression arithmeticExpression2) {
        super();
        this.arithmeticExpression1 = arithmeticExpression1;
        this.arithmeticExpression2 = arithmeticExpression2;
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.getBinaryArithmeticType(this.arithmeticExpression1.getType(expressionContext), this.arithmeticExpression2.getType(expressionContext));
    }
    
    public ArithmeticExpression getArithmeticExpression1() {
        return this.arithmeticExpression1;
    }
    
    public ArithmeticExpression getArithmeticExpression2() {
        return this.arithmeticExpression2;
    }
}
