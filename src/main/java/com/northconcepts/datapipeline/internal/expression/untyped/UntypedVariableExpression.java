package com.northconcepts.datapipeline.internal.expression.untyped;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public final class UntypedVariableExpression extends Expression
{
    private final String name;
    
    public UntypedVariableExpression(String name) {
        super();
        if (name != null && name.equals("?")) {
            name = Expression.getThreadLocalVariableName();
        }
        this.setOriginalSourceCode(name);
        if (name.startsWith("${") && name.endsWith("}")) {
            name = name.substring(2, name.length() - 1);
        }
        this.name = name;
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        return ExpressionContext.Helper.getType(expressionContext, this.name);
    }
    
    @Override
	public Object evaluate(final ExpressionContext expressionContext) {
        try {
            Object value = ExpressionContext.Helper.getValue(expressionContext, this.name);
            value = TypeUtil.convertToType(value, this.getType(expressionContext));
            return value;
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    @Override
	public String getSourceString() {
        return this.getOriginalSourceCode();
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
