package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class InstanceOfExpression extends BinaryRelationalExpression
{
    public InstanceOfExpression(final Expression expression1, final Expression expression2) {
        super(expression1, expression2);
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        final Object object = this.getExpression1().evaluate(expressionContext);
        final Class<?> clazz = (Class<?>)this.getExpression2().evaluate(expressionContext);
        return clazz != null && object != null && clazz.isAssignableFrom(object.getClass());
    }
    
    @Override
	public String getSourceString() {
        return this.getExpression1().getSourceString() + " instanceof " + this.getExpression2().getSourceString();
    }
}
