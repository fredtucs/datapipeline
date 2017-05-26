package com.northconcepts.datapipeline.internal.expression.character;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class LiteralCharacterExpression extends CharacterExpression
{
    private final String value;
    
    public LiteralCharacterExpression(final String value, final String quoteChar) {
        super();
        this.value = value;
        this.setOriginalSourceCode(quoteChar);
    }
    
    @Override
	public String evaluateString(final ExpressionContext expressionContext) {
        return this.value;
    }
    
    @Override
	public String getSourceString() {
        return this.getOriginalSourceCode() + this.value + this.getOriginalSourceCode();
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
