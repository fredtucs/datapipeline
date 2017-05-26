package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class NotExpression extends UnaryLogicalExpression
{
    public NotExpression(final LogicalExpression logicalExpression1, final String operator) {
        super(logicalExpression1);
        this.setOriginalSourceCode(operator);
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        return !this.getLogicalExpression1().evaluateBoolean(expressionContext);
    }
    
    private String getOperatorSourceCode() {
        return "!".equals(this.getOriginalSourceCode()) ? "!" : "not ";
    }
    
    @Override
	public String getSourceString() {
        if (this.getLogicalExpression1().isQuantity()) {
            return this.getOperatorSourceCode() + this.getLogicalExpression1().getSourceString();
        }
        return this.getOperatorSourceCode() + "(" + this.getLogicalExpression1().getSourceString() + ")";
    }
}
