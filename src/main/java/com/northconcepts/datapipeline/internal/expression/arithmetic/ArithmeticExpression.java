package com.northconcepts.datapipeline.internal.expression.arithmetic;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public abstract class ArithmeticExpression extends Expression
{
    @Override
	public final Object evaluate(final ExpressionContext expressionContext) {
        final Class<?> type = this.getType(expressionContext);
        if (type == Expression.LONG_TYPE) {
            return new Long(this.evaluateLong(expressionContext));
        }
        if (type == Expression.DOUBLE_TYPE) {
            return new Double(this.evaluateDouble(expressionContext));
        }
        throw new DataException("Unknown arithmetic type [" + type + "]");
    }
    
    public abstract long evaluateLong(final ExpressionContext p0);
    
    public abstract double evaluateDouble(final ExpressionContext p0);
}
