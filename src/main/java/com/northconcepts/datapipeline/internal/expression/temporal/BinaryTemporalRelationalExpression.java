package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.logical.RelationalExpression;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public abstract class BinaryTemporalRelationalExpression extends RelationalExpression
{
    private final TemporalExpression temporalExpression1;
    private final TemporalExpression temporalExpression2;
    
    public BinaryTemporalRelationalExpression(final TemporalExpression temporalExpression1, final TemporalExpression temporalExpression2) {
        super();
        this.temporalExpression1 = temporalExpression1;
        this.temporalExpression2 = temporalExpression2;
    }
    
    public TemporalExpression getTemporalExpression1() {
        return this.temporalExpression1;
    }
    
    public TemporalExpression getTemporalExpression2() {
        return this.temporalExpression2;
    }
    
    public boolean expressionsAreIntervals() {
        return this.temporalExpression1 instanceof IntervalExpression && this.temporalExpression2 instanceof IntervalExpression;
    }
    
    public boolean expressionsAreMoments() {
        return this.temporalExpression1 instanceof MomentExpression && this.temporalExpression2 instanceof MomentExpression;
    }
    
    protected void throwTypeMismatchException() {
        throw new DataException("expected both expressions to be either IntervalExpression or MomentExpression, but found [" + TypeUtil.getJavaName(this.getTemporalExpression1().getClass()) + "] and " + "[" + TypeUtil.getJavaName(this.getTemporalExpression2().getClass()) + "] instead");
    }
}
