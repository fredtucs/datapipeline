package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class LogicalCoercionExpression extends LogicalExpression
{
    private final Expression expression;
    
    public LogicalCoercionExpression(final Expression expression) {
        super();
        this.expression = expression;
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        try {
            final Boolean o = (Boolean)TypeUtil.convertToType(this.expression.evaluate(expressionContext), this.getType(expressionContext));
            return o != null && o;
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
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
