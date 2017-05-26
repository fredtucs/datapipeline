package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class TemporalLessThanOrEqualToExpression extends BinaryTemporalRelationalExpression
{
    public TemporalLessThanOrEqualToExpression(final TemporalExpression temporalExpression1, final TemporalExpression temporalExpression2) {
        super(temporalExpression1, temporalExpression2);
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        if (this.expressionsAreIntervals()) {
            final IntervalExpression o1 = (IntervalExpression)this.getTemporalExpression1();
            final IntervalExpression o2 = (IntervalExpression)this.getTemporalExpression2();
            return o1.evaluateInterval(expressionContext).isLessThanOrEqualTo(o2.evaluateInterval(expressionContext));
        }
        if (this.expressionsAreMoments()) {
            final MomentExpression o3 = (MomentExpression)this.getTemporalExpression1();
            final MomentExpression o4 = (MomentExpression)this.getTemporalExpression2();
            return o3.evaluateMoment(expressionContext).isLessThanOrEqualTo(o4.evaluateMoment(expressionContext));
        }
        this.throwTypeMismatchException();
        return false;
    }
    
    @Override
	public String getSourceString() {
        return this.getTemporalExpression1().getSourceString() + " <= " + this.getTemporalExpression2().getSourceString();
    }
}
