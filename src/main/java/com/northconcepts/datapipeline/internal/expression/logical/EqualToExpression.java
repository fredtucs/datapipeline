package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class EqualToExpression extends BinaryRelationalExpression
{
    public EqualToExpression(final Expression expression1, final Expression expression2, final String operator) {
        super(expression1, expression2);
        this.setOriginalSourceCode(operator);
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        Object object1 = this.getExpression1().evaluate(expressionContext);
        Object object2 = this.getExpression2().evaluate(expressionContext);
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
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
        if (Expression.NUMBER_TYPE.isAssignableFrom(type1) && Expression.NUMBER_TYPE.isAssignableFrom(type2)) {
            final Class<?> resultType = Expression.getBinaryArithmeticType(type1, type2);
            final Number n1 = (Number)object1;
            final Number n2 = (Number)object2;
            if (resultType == Expression.DOUBLE_TYPE) {
                return n1.doubleValue() == n2.doubleValue();
            }
            if (resultType == Expression.LONG_TYPE) {
                return n1.longValue() == n2.longValue();
            }
            throw new DataException("Unhandled arithmetic type: [" + resultType + "]");
        }
        else {
            if (type1 == Expression.STRING_TYPE || type2 == Expression.STRING_TYPE) {
                return object1.toString().equals(object2.toString());
            }
            return object1.equals(object2);
        }
    }
    
    @Override
	public String getSourceString() {
        return this.getExpression1().getSourceString() + " " + this.getOriginalSourceCode() + " " + this.getExpression2().getSourceString();
    }
}
