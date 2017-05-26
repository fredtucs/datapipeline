package com.northconcepts.datapipeline.internal.expression.untyped;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Interval;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public abstract class UntypedRelationalExpression extends UntypedBinaryExpression
{
    public UntypedRelationalExpression(final Expression expression1, final Expression expression2) {
        super(expression1, expression2);
    }
    
    @Override
	public final Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.BOOLEAN_TYPE;
    }
    
    @Override
	public final Object evaluate(final ExpressionContext expressionContext) {
        return new Boolean(this.evaluateBoolean(expressionContext));
    }
    
    private final boolean evaluateBoolean(final ExpressionContext expressionContext) {
        Object object1 = this.getExpression1().evaluate(expressionContext);
        Object object2 = this.getExpression2().evaluate(expressionContext);
        if (object1 == object2) {
            return this.evaluateBoolean_Object1EqualsObject2();
        }
        if (object1 == null) {
            return this.evaluateBoolean_Object1IsNull();
        }
        if (object2 == null) {
            return this.evaluateBoolean_Object2IsNull();
        }
        final Class<?> type1 = this.getExpression1().getType(expressionContext);
        final Class<?> type2 = this.getExpression2().getType(expressionContext);
        try {
            object1 = TypeUtil.convertToType(object1, type1);
            object2 = TypeUtil.convertToType(object2, type2);
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
        if (object1 instanceof Number && object2 instanceof Number) {
            final Number o1 = (Number)object1;
            final Number o2 = (Number)object2;
            final Class<?> arithmeticType = Expression.getBinaryArithmeticType(type1, type2);
            return this.evaluateBoolean(o1, o2, arithmeticType);
        }
        if (type1 == Expression.INTERVAL_TYPE && type2 == Expression.INTERVAL_TYPE) {
            final Interval o3 = (Interval)object1;
            final Interval o4 = (Interval)object2;
            return this.evaluateBoolean(o3, o4);
        }
        if (type1 == Expression.MOMENT_TYPE && type2 == Expression.MOMENT_TYPE) {
            final Moment o5 = (Moment)object1;
            final Moment o6 = (Moment)object2;
            return this.evaluateBoolean(o5, o6);
        }
        if (type1 == Expression.STRING_TYPE && type2 == Expression.STRING_TYPE) {
            final String o7 = (String)object1;
            final String o8 = (String)object2;
            return this.evaluateBoolean(o7, o8);
        }
        throw new DataException("Cannot compare a [" + TypeUtil.getJavaName(type1) + "] with a [" + TypeUtil.getJavaName(type2) + "]; value1=[" + object1 + "]; value2=[" + object2 + "]");
    }
    
    public abstract boolean evaluateBoolean_Object1EqualsObject2();
    
    public abstract boolean evaluateBoolean_Object1IsNull();
    
    public abstract boolean evaluateBoolean_Object2IsNull();
    
    public abstract boolean evaluateBoolean(final Number p0, final Number p1, final Class<?> p2);
    
    public abstract boolean evaluateBoolean(final Interval p0, final Interval p1);
    
    public abstract boolean evaluateBoolean(final Moment p0, final Moment p1);
    
    public abstract boolean evaluateBoolean(final String p0, final String p1);
}
