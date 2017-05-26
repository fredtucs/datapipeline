package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Moment;

public abstract class MomentExpression extends TemporalExpression
{
    @Override
	public final Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.MOMENT_TYPE;
    }
    
    @Override
	public final Object evaluate(final ExpressionContext expressionContext) {
        return this.evaluateMoment(expressionContext);
    }
    
    public abstract Moment evaluateMoment(final ExpressionContext p0);
}
