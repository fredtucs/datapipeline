package com.northconcepts.datapipeline.internal.expression.arithmetic;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class GreaterThanExpression extends BinaryArithmeticRelationalExpression
{
    public GreaterThanExpression(final ArithmeticExpression arithmeticExpression1, final ArithmeticExpression arithmeticExpression2) {
        super(arithmeticExpression1, arithmeticExpression2);
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        return this.getArithmeticExpression1().evaluateDouble(expressionContext) > this.getArithmeticExpression2().evaluateDouble(expressionContext);
    }
    
    @Override
	public String getSourceString() {
        return this.getArithmeticExpression1().getSourceString() + " > " + this.getArithmeticExpression2().getSourceString();
    }
}
