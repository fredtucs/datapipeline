package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Interval;

public abstract class IntervalExpression extends TemporalExpression
{
    @Override
	public final Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.INTERVAL_TYPE;
    }
    
    @Override
	public final Object evaluate(final ExpressionContext expressionContext) {
        return this.evaluateInterval(expressionContext);
    }
    
    public abstract Interval evaluateInterval(final ExpressionContext p0);
}
