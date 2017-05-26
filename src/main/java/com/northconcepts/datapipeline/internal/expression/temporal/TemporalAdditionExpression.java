package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class TemporalAdditionExpression extends BinaryTemporalExpression
{
    public TemporalAdditionExpression(final TemporalExpression temporalExpression1, final TemporalExpression temporalExpression2) {
        super(temporalExpression1, temporalExpression2);
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.OBJECT_TYPE;
    }
    
    @Override
	public Object evaluate(final ExpressionContext expressionContext) {
        if (this.expressionsAreIntervals()) {
            final IntervalExpression o1 = (IntervalExpression)this.getTemporalExpression1();
            final IntervalExpression o2 = (IntervalExpression)this.getTemporalExpression2();
            return o1.evaluateInterval(expressionContext).add(o2.evaluateInterval(expressionContext));
        }
        if (this.expressionsAreMomentAndInterval()) {
            final MomentExpression o3 = (MomentExpression)this.getTemporalExpression1();
            final IntervalExpression o2 = (IntervalExpression)this.getTemporalExpression2();
            return o3.evaluateMoment(expressionContext).add(o2.evaluateInterval(expressionContext));
        }
        this.throwTypeMismatchException("expected IntervalExpression + IntervalExpression or MomentExpression + IntervalExpression");
        return null;
    }
    
    @Override
	public String getSourceString() {
        return this.getTemporalExpression1().getSourceString() + " + " + this.getTemporalExpression2().getSourceString();
    }
}
