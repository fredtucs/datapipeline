package com.northconcepts.datapipeline.internal.expression.logical;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class VariableLogicalExpression extends LogicalExpression
{
    private final String name;
    
    public VariableLogicalExpression(final String name) {
        super();
        this.name = name;
    }
    
    @Override
	public boolean evaluateBoolean(final ExpressionContext expressionContext) {
        try {
            final Boolean o = (Boolean)TypeUtil.convertToType(ExpressionContext.Helper.getValue(expressionContext, this.name), this.getType(expressionContext));
            return o != null && o;
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
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
