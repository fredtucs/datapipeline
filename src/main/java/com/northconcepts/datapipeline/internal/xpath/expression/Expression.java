package com.northconcepts.datapipeline.internal.xpath.expression;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.expression.ASTNode;
import com.northconcepts.datapipeline.internal.lang.Interval;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public abstract class Expression implements ASTNode
{
    protected static final Class<String> STRING_TYPE;
    protected static final Class<Interval> INTERVAL_TYPE;
    protected static final Class<Moment> MOMENT_TYPE;
    protected static final Class<Number> NUMBER_TYPE;
    protected static final Class<?> LONG_TYPE;
    protected static final Class<?> DOUBLE_TYPE;
    protected static final Class<Boolean> BOOLEAN_TYPE;
    protected static final Class<Object> OBJECT_TYPE;
    protected static final Class<Integer> INTEGER_TYPE;
    private static final ThreadLocal<String> threadLocalVariableName;
    private String originalSourceCode;
    
    protected static String getThreadLocalVariableName() {
        return Expression.threadLocalVariableName.get();
    }
    
    public static void setThreadLocalVariableName(final String threadLocalVariableName) {
        Expression.threadLocalVariableName.set(threadLocalVariableName);
    }
    
    public abstract Class<?> getType(final ExpressionContext p0);
    
    public abstract Object evaluate(final ExpressionContext p0);
    
    protected static Class<?> mapType(Class<?> type) {
        type = TypeUtil.primitiveToObjectType(type);
        if (type == Expression.INTEGER_TYPE) {
            type = Expression.LONG_TYPE;
        }
        return type;
    }
    
    protected static Class<?> getBinaryArithmeticType(final Class<?> c1, final Class<?> c2) {
        if (Expression.DOUBLE_TYPE.isAssignableFrom(c1) || Expression.DOUBLE_TYPE.isAssignableFrom(c2)) {
            return Expression.DOUBLE_TYPE;
        }
        if (Expression.LONG_TYPE.isAssignableFrom(c1) && Expression.LONG_TYPE.isAssignableFrom(c2)) {
            return Expression.LONG_TYPE;
        }
        throw new DataException("Unhandled arithmetic combination: [" + c1 + "] and [" + c2 + "]");
    }
    
    public abstract String getSourceString();
    
    public boolean isLiteral() {
        return false;
    }
    
    public boolean isVariable() {
        return false;
    }
    
    public boolean isQuantity() {
        return this.isLiteral() || this.isVariable();
    }
    
    protected String getOriginalSourceCode() {
        return this.originalSourceCode;
    }
    
    protected void setOriginalSourceCode(final String originalSourceCode) {
        this.originalSourceCode = originalSourceCode;
    }
    
    @Override
	public String toString() {
        return TypeUtil.getClassName(this.getClass()) + ":  " + this.getSourceString();
    }
    
    static {
        STRING_TYPE = String.class;
        INTERVAL_TYPE = Interval.class;
        MOMENT_TYPE = Moment.class;
        NUMBER_TYPE = Number.class;
        LONG_TYPE = Long.class;
        DOUBLE_TYPE = Double.class;
        BOOLEAN_TYPE = Boolean.class;
        OBJECT_TYPE = Object.class;
        INTEGER_TYPE = Integer.class;
        threadLocalVariableName = new ThreadLocal<String>();
    }
}
