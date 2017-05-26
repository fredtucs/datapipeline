package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class NotEqualToExpression extends EqualToExpression
{
    public NotEqualToExpression(final Expression expression1, final Expression expression2, final String operator) {
        super(expression1, expression2, operator);
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        return !super.evaluateBoolean(expressionContext);
    }
    
    @Override
	public String getSourceString() {
        return this.getExpression1().getSourceString() + " " + this.getOriginalSourceCode() + " " + this.getExpression2().getSourceString();
    }
}
