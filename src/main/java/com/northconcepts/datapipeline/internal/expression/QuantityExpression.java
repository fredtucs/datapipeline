package com.northconcepts.datapipeline.internal.expression;

public class QuantityExpression extends Expression
{
    private final Expression expression;
    
    public QuantityExpression(final Expression expression) {
        super();
        this.expression = expression;
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        return this.expression.getType(expressionContext);
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    @Override
	public Object evaluate(final ExpressionContext expressionContext) {
        return this.expression.evaluate(expressionContext);
    }
    
    @Override
	public String getSourceString() {
        if (this.expression.isLiteral() || this.expression.isVariable()) {
            return this.expression.getSourceString();
        }
        return "(" + this.expression.getSourceString() + ")";
    }
    
    @Override
	public boolean isQuantity() {
        return true;
    }
}
