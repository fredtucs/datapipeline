package com.northconcepts.datapipeline.internal.expression;

import java.util.ArrayList;
import java.util.List;

public final class ExpressionPair extends Expression
{
    private final Expression expression1;
    private final Expression expression2;
    
    public ExpressionPair(final Expression expression1, final Expression expression2) {
        super();
        this.expression1 = expression1;
        this.expression2 = expression2;
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.OBJECT_TYPE;
    }
    
    @Override
	public Object evaluate(final ExpressionContext expressionContext) {
        throw new UnsupportedOperationException(ExpressionPair.class.getName() + "s cannot be evaluated");
    }
    
    public Expression[] getExpressions() {
        final ArrayList<Expression> list = new ArrayList<Expression>();
        this.traverse(list);
        return list.toArray(new Expression[list.size()]);
    }
    
    private final List<Expression> traverse(final List<Expression> list) {
        addExpressionToList(this.expression1, list);
        addExpressionToList(this.expression2, list);
        return list;
    }
    
    private static final void addExpressionToList(final Expression expression, final List<Expression> list) {
        if (expression instanceof ExpressionPair) {
            ((ExpressionPair)expression).traverse(list);
        }
        else {
            list.add(expression);
        }
    }
    
    @Override
	public String getSourceString() {
        return this.expression1.getSourceString() + ", " + this.expression2.getSourceString();
    }
}
