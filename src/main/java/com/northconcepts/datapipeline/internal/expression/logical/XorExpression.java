package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class XorExpression extends BinaryLogicalExpression
{
    public XorExpression(final LogicalExpression logicalExpression1, final LogicalExpression logicalExpression2, final String conjunction) {
        super(logicalExpression1, logicalExpression2);
        this.setOriginalSourceCode(conjunction);
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        return this.getLogicalExpression1().evaluateBoolean(expressionContext) ^ this.getLogicalExpression2().evaluateBoolean(expressionContext);
    }
    
    @Override
	public String getSourceString() {
        return this.getLogicalExpression1().getSourceString() + " " + this.getOriginalSourceCode() + " " + this.getLogicalExpression2().getSourceString();
    }
}
