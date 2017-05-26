package com.northconcepts.datapipeline.internal.expression.untyped;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.lang.Interval;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.internal.lang.comparator.StringComparator;

public class UntypedGreaterThanExpression extends UntypedRelationalExpression
{
    public UntypedGreaterThanExpression(final Expression expression1, final Expression expression2) {
        super(expression1, expression2);
    }
    
    @Override
	public boolean evaluateBoolean_Object1EqualsObject2() {
        return false;
    }
    
    @Override
	public boolean evaluateBoolean_Object1IsNull() {
        return false;
    }
    
    @Override
	public boolean evaluateBoolean_Object2IsNull() {
        return true;
    }
    
    @Override
	public boolean evaluateBoolean(final Interval o1, final Interval o2) {
        return o1.isGreaterThan(o2);
    }
    
    @Override
	public boolean evaluateBoolean(final Moment o1, final Moment o2) {
        return o1.isGreaterThan(o2);
    }
    
    @Override
	public boolean evaluateBoolean(final Number o1, final Number o2, final Class<?> arithmeticType) {
        if (arithmeticType == Expression.DOUBLE_TYPE) {
            return o1.doubleValue() > o2.doubleValue();
        }
        if (arithmeticType == Expression.LONG_TYPE) {
            return o1.longValue() > o2.longValue();
        }
        throw new DataException("Unhandled arithmetic type: [" + arithmeticType + "]");
    }
    
    @Override
	public boolean evaluateBoolean(final String o1, final String o2) {
        return StringComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    @Override
	public String getSourceString() {
        return this.getExpression1().getSourceString() + " > " + this.getExpression2().getSourceString();
    }
}
