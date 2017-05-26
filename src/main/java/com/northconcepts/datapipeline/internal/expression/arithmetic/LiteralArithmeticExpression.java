package com.northconcepts.datapipeline.internal.expression.arithmetic;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class LiteralArithmeticExpression extends ArithmeticExpression
{
    private final Number value;
    
    @Override
	public final Class<?> getType(final ExpressionContext expressionContext) {
        return this.value.getClass();
    }
    
    public LiteralArithmeticExpression(final long value, final String originalSourceCode) {
        super();
        this.value = new Long(value);
        this.setOriginalSourceCode(originalSourceCode);
    }
    
    public LiteralArithmeticExpression(final double value, final String originalSourceCode) {
        super();
        this.value = new Double(value);
        this.setOriginalSourceCode(originalSourceCode);
    }
    
    @Override
	public double evaluateDouble(final ExpressionContext expressionContext) {
        return this.value.doubleValue();
    }
    
    @Override
	public long evaluateLong(final ExpressionContext expressionContext) {
        return this.value.longValue();
    }
    
    @Override
	public String getSourceString() {
        return this.getOriginalSourceCode();
    }
    
    @Override
	public boolean isLiteral() {
        return true;
    }
    
    @Override
	public boolean isVariable() {
        return false;
    }
}
