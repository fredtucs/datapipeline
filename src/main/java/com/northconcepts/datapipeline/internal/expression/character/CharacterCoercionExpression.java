package com.northconcepts.datapipeline.internal.expression.character;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class CharacterCoercionExpression extends CharacterExpression
{
    private final Expression expression;
    
    public CharacterCoercionExpression(final Expression expression) {
        super();
        this.expression = expression;
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    @Override
	public String evaluateString(final ExpressionContext expressionContext) {
        final Object o = this.expression.evaluate(expressionContext);
        if (o == null) {
            return null;
        }
        return o.toString();
    }
    
    @Override
	public String getSourceString() {
        return this.expression.getSourceString();
    }
    
    @Override
	public boolean isLiteral() {
        return this.expression.isLiteral();
    }
    
    @Override
	public boolean isVariable() {
        return this.expression.isVariable();
    }
    
    @Override
	public boolean isQuantity() {
        return this.expression.isQuantity();
    }
}
