package com.northconcepts.datapipeline.internal.expression.arithmetic;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class VariableArithmeticExpression extends ArithmeticExpression
{
    private final String name;
    
    public VariableArithmeticExpression(final String name) {
        super();
        this.name = name;
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        return ExpressionContext.Helper.getType(expressionContext, this.name);
    }
    
    @Override
	public long evaluateLong(final ExpressionContext expressionContext) {
        try {
            final Number o = (Number)TypeUtil.convertToType(ExpressionContext.Helper.getValue(expressionContext, this.name), this.getType(expressionContext));
            if (o == null) {
                return 0L;
            }
            return o.longValue();
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    @Override
	public double evaluateDouble(final ExpressionContext expressionContext) {
        try {
            final Number o = (Number)TypeUtil.convertToType(ExpressionContext.Helper.getValue(expressionContext, this.name), this.getType(expressionContext));
            if (o == null) {
                return 0.0;
            }
            return o.doubleValue();
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    @Override
	public String getSourceString() {
        return this.name;
    }
    
    @Override
	public boolean isLiteral() {
        return false;
    }
    
    @Override
	public boolean isVariable() {
        return true;
    }
}
