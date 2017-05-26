package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public abstract class LogicalExpression extends Expression implements Cloneable
{
    @Override
	public final Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.BOOLEAN_TYPE;
    }
    
    @Override
	public final Object evaluate(final ExpressionContext expressionContext) {
        return new Boolean(this.evaluateBoolean(expressionContext));
    }
    
    public abstract boolean evaluateBoolean(final ExpressionContext p0);
    
    @Override
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
