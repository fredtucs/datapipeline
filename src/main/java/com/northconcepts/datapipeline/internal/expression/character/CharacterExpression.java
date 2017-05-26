package com.northconcepts.datapipeline.internal.expression.character;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public abstract class CharacterExpression extends Expression
{
    @Override
	public final Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.STRING_TYPE;
    }
    
    @Override
	public final Object evaluate(final ExpressionContext expressionContext) {
        return this.evaluateString(expressionContext);
    }
    
    public abstract String evaluateString(final ExpressionContext p0);
}
