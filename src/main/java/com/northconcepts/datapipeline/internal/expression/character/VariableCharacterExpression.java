package com.northconcepts.datapipeline.internal.expression.character;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class VariableCharacterExpression extends CharacterExpression
{
    private final String name;
    
    public VariableCharacterExpression(final String name) {
        super();
        this.name = name;
    }
    
    @Override
	public String evaluateString(final ExpressionContext expressionContext) {
        final Object o = ExpressionContext.Helper.getValue(expressionContext, this.name);
        if (o == null) {
            return null;
        }
        return o.toString();
    }
    
    @Override
	public String getSourceString() {
        return this.name;
    }
    
    @Override
	public boolean isLiteral() {
        return false;
    }
    
    @Override
	public boolean isVariable() {
        return true;
    }
}
