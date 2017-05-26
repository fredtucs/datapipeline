package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class TemporalSubtractionExpression extends BinaryTemporalExpression
{
    public TemporalSubtractionExpression(final TemporalExpression temporalExpression1, final TemporalExpression temporalExpression2) {
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
            return o1.evaluateInterval(expressionContext).subtract(o2.evaluateInterval(expressionContext));
        }
        if (this.expressionsAreMomentAndInterval()) {
            final MomentExpression o3 = (MomentExpression)this.getTemporalExpression1();
            final IntervalExpression o2 = (IntervalExpression)this.getTemporalExpression2();
            return o3.evaluateMoment(expressionContext).subtract(o2.evaluateInterval(expressionContext));
        }
        if (this.expressionsAreMoments()) {
            final MomentExpression o3 = (MomentExpression)this.getTemporalExpression1();
            final MomentExpression o4 = (MomentExpression)this.getTemporalExpression2();
            return o3.evaluateMoment(expressionContext).subtract(o4.evaluateMoment(expressionContext));
        }
        this.throwTypeMismatchException("expected IntervalExpression - IntervalExpression, MomentExpression - IntervalExpression, or MomentExpression - MomentExpression");
        return null;
    }
    
    @Override
	public String getSourceString() {
        return this.getTemporalExpression1().getSourceString() + " - " + this.getTemporalExpression2().getSourceString();
    }
}
