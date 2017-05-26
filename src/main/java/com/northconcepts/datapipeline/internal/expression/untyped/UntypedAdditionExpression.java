package com.northconcepts.datapipeline.internal.expression.untyped;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Interval;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class UntypedAdditionExpression extends UntypedBinaryExpression
{
    public UntypedAdditionExpression(final Expression expression1, final Expression expression2) {
        super(expression1, expression2);
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        final Class<?> type1 = this.getExpression1().getType(expressionContext);
        final Class<?> type2 = this.getExpression2().getType(expressionContext);
        if (type1 == Expression.STRING_TYPE || type2 == Expression.STRING_TYPE) {
            return Expression.STRING_TYPE;
        }
        if (type1 == Expression.INTERVAL_TYPE && type2 == Expression.INTERVAL_TYPE) {
            return Expression.INTERVAL_TYPE;
        }
        if (type1 == Expression.MOMENT_TYPE && type2 == Expression.INTERVAL_TYPE) {
            return Expression.MOMENT_TYPE;
        }
        if (type1 == Expression.INTERVAL_TYPE && type2 == Expression.MOMENT_TYPE) {
            return Expression.MOMENT_TYPE;
        }
        if (Expression.NUMBER_TYPE.isAssignableFrom(type1) && Expression.NUMBER_TYPE.isAssignableFrom(type2)) {
            return Expression.getBinaryArithmeticType(type1, type2);
        }
        throw new DataException("Cannot add a [" + TypeUtil.getJavaName(type1) + "] to a [" + TypeUtil.getJavaName(type2) + "]");
    }
    
    @Override
	public Object evaluate(final ExpressionContext expressionContext) {
        Object object1 = this.getExpression1().evaluate(expressionContext);
        Object object2 = this.getExpression2().evaluate(expressionContext);
        if (object1 == null) {
            return object2;
        }
        if (object2 == null) {
            return object1;
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
        if (type1 == Expression.STRING_TYPE || type2 == Expression.STRING_TYPE) {
            return object1.toString() + object2.toString();
        }
        if (type1 == Expression.INTERVAL_TYPE && type2 == Expression.INTERVAL_TYPE) {
            final Interval o1 = (Interval)object1;
            final Interval o2 = (Interval)object2;
            return o1.add(o2);
        }
        if (type1 == Expression.MOMENT_TYPE && type2 == Expression.INTERVAL_TYPE) {
            final Moment o3 = (Moment)object1;
            final Interval o2 = (Interval)object2;
            return o3.add(o2);
        }
        if (type1 == Expression.INTERVAL_TYPE && type2 == Expression.MOMENT_TYPE) {
            final Interval o1 = (Interval)object1;
            final Moment o4 = (Moment)object2;
            return o4.add(o1);
        }
        if (!(object1 instanceof Number) || !(object2 instanceof Number)) {
            throw new DataException("Cannot add a [" + TypeUtil.getJavaName(type1) + "] to a [" + TypeUtil.getJavaName(type2) + "]");
        }
        final Class<?> resultType = Expression.getBinaryArithmeticType(type1, type2);
        final Number n1 = (Number)object1;
        final Number n2 = (Number)object2;
        if (resultType == Expression.DOUBLE_TYPE) {
            return new Double(n1.doubleValue() + n2.doubleValue());
        }
        if (resultType == Expression.LONG_TYPE) {
            return new Long(n1.longValue() + n2.longValue());
        }
        throw new DataException("Unhandled arithmetic type: [" + resultType + "]");
    }
    
    @Override
	public String getSourceString() {
        return this.getExpression1().getSourceString() + " + " + this.getExpression2().getSourceString();
    }
}
