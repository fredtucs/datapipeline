package com.northconcepts.datapipeline.internal.expression;

public abstract class HierarchicalExpressionContext extends ExpressionContext
{
    private ExpressionContext parent;
    
    public HierarchicalExpressionContext() {
        super();
        this.parent = ExpressionContext.NULL;
    }
    
    public ExpressionContext getParent() {
        return this.parent;
    }
    
    public HierarchicalExpressionContext setParent(ExpressionContext parent) {
        if (parent == null) {
            parent = ExpressionContext.NULL;
        }
        this.parent = parent;
        return this;
    }
    
    @Override
	public final Class<?> getType(final String name) {
        Class<?> type = this.getTypeImpl(name);
        if (type == null) {
            type = this.parent.getType(name);
        }
        return type;
    }
    
    @Override
	public final Object getValue(final String name) {
        Object value = this.getValueImpl(name);
        if (value == null) {
            value = this.parent.getValue(name);
        }
        return value;
    }
    
    @Override
	public final boolean containsKey(final String name) {
        boolean containsKey = this.containsKeyImpl(name);
        if (!containsKey) {
            containsKey = this.parent.containsKey(name);
        }
        return containsKey;
    }
    
    protected abstract Class<?> getTypeImpl(final String p0);
    
    protected abstract Object getValueImpl(final String p0);
    
    protected abstract boolean containsKeyImpl(final String p0);
}
