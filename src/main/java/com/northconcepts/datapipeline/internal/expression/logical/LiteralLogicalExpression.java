package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class LiteralLogicalExpression extends LogicalExpression
{
    public static final LiteralLogicalExpression TRUE;
    public static final LiteralLogicalExpression FALSE;
    private final boolean value;
    
    public LiteralLogicalExpression(final boolean value, final String originalSourceCode) {
        super();
        this.value = value;
        this.setOriginalSourceCode(originalSourceCode);
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        return this.value;
    }
    
    @Override
	public String getSourceString() {
        return this.getOriginalSourceCode();
    }
    
    @Override
	public boolean isLiteral() {
        return true;
    }
    
    @Override
	public boolean isVariable() {
        return false;
    }
    
    static {
        TRUE = new LiteralLogicalExpression(true, "true");
        FALSE = new LiteralLogicalExpression(false, "false");
    }
}
