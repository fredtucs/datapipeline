package com.northconcepts.datapipeline.internal.expression.untyped;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public final class UntypedNullExpression extends Expression
{
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.OBJECT_TYPE;
    }
    
    @Override
	public Object evaluate(final ExpressionContext expressionContext) {
        return null;
    }
    
    @Override
	public String getSourceString() {
        return "null";
    }
    
    @Override
	public boolean isLiteral() {
        return true;
    }
    
    @Override
	public boolean isVariable() {
        return false;
    }
}
