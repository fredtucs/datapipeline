package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.expression.arithmetic.ArithmeticExpression;
import com.northconcepts.datapipeline.internal.lang.Interval;

public class LiteralIntervalExpression extends IntervalExpression
{
    private final ArithmeticExpression value;
    private final String unitName;
    
    public LiteralIntervalExpression(final ArithmeticExpression value, final String unitName) {
        super();
        this.value = value;
        this.unitName = unitName;
    }
    
    @Override
	public Interval evaluateInterval(final ExpressionContext expressionContext) {
        return Interval.parseInterval((long)this.value.evaluateDouble(expressionContext), this.unitName);
    }
    
    @Override
	public String getSourceString() {
        return this.value.getSourceString() + " " + this.unitName;
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
