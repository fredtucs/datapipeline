package com.northconcepts.datapipeline.internal.expression.arithmetic;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class PositiveQuantityExpression extends UnaryArithmeticExpression
{
    public PositiveQuantityExpression(final ArithmeticExpression arithmeticExpression1) {
        super(arithmeticExpression1);
    }
    
    @Override
	public double evaluateDouble(final ExpressionContext expressionContext) {
        final double quantity = this.getArithmeticExpression1().evaluateDouble(expressionContext);
        return quantity;
    }
    
    @Override
	public long evaluateLong(final ExpressionContext expressionContext) {
        final long quantity = this.getArithmeticExpression1().evaluateLong(expressionContext);
        return quantity;
    }
    
    @Override
	public String getSourceString() {
        if (this.getArithmeticExpression1().isQuantity()) {
            return "+" + this.getArithmeticExpression1().getSourceString();
        }
        return "+(" + this.getArithmeticExpression1().getSourceString() + ")";
    }
}
