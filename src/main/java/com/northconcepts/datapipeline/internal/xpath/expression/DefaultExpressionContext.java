package com.northconcepts.datapipeline.internal.xpath.expression;

import java.util.HashMap;
import java.util.Map;

import com.northconcepts.datapipeline.core.DataException;

public class DefaultExpressionContext extends ExpressionContext
{
    private Map<String, Variable> variables;
    
    public DefaultExpressionContext() {
        super();
        this.variables = new HashMap<String, Variable>();
    }
    
    @Override
	public Class<?> getType(final String name) {
        final Variable variable = this.variables.get(name);
        if (variable == null) {
            return null;
        }
        if (variable.type != null) {
            return variable.type;
        }
        if (variable.value == null) {
            throw new DataException("unknown type for null variable").set("variable", name);
        }
        return variable.value.getClass();
    }
    
    @Override
	public Object getValue(final String name) {
        final Variable variable = this.variables.get(name);
        if (variable == null) {
            return null;
        }
        return variable.value;
    }
    
    public void setValue(final String name, final Object value) {
        this.setValue(name, value, null);
    }
    
    public void setValue(final String name, final Object value, final Class<?> type) {
        this.variables.put(name, new Variable(value, type));
    }
    
    public Object removeValue(final String name) {
        return this.variables.remove(name);
    }
    
    public void removeAllValues() {
        this.variables.clear();
    }
    
    @Override
	public boolean containsKey(final String name) {
        return this.variables.containsKey(name);
    }
    
    public void copyFrom(final Map<String, Variable> map, final boolean overwrite) {
        if (overwrite) {
            this.variables.putAll(map);
        }
        else {
            final Map<String, Variable> newMap = new HashMap<String, Variable>(map);
            newMap.putAll(this.variables);
            this.variables = newMap;
        }
    }
    
    private static class Variable
    {
        public final Object value;
        public final Class<?> type;
        
        public Variable(final Object value, final Class<?> type) {
            super();
            this.value = value;
            this.type = type;
        }
    }
}
