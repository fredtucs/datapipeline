package com.northconcepts.datapipeline.jdbc.sql.select;

import java.util.ArrayList;
import java.util.List;

public abstract class QueryCriteriaPart extends QueryPart
{
    private final List<QueryCriteriaPart> parts;
    
    public QueryCriteriaPart() {
        super();
        this.parts = new ArrayList<QueryCriteriaPart>(0);
    }
    
    public abstract boolean isApplicable();
    
    protected abstract void getPartParameterValues(final List<Object> p0);
    
    protected final void getParameterValues(final List<Object> values) {
        this.getPartParameterValues(values);
        for (int i = 0; i < this.parts.size(); ++i) {
            this.parts.get(i).getParameterValues(values);
        }
    }
    
    public final Object[] getParameterValues() {
        final List<Object> values = new ArrayList<Object>();
        this.getParameterValues(values);
        return values.toArray();
    }
    
    protected List<QueryCriteriaPart> getParts() {
        return this.parts;
    }
    
    protected QueryCriteriaPart add(final String statement, final Object... values) {
        this.parts.add(new ConditionPart(statement, values));
        return this;
    }
    
    protected QueryCriteriaPart addFirst(final String statement, final Object... values) {
        this.parts.add(0, new ConditionPart(statement, values));
        return this;
    }
    
    protected QueryCriteriaPart add(final QueryCriteriaPart part) {
        this.parts.add(part);
        return this;
    }
    
    protected QueryCriteriaPart addFirst(final QueryCriteriaPart part) {
        this.parts.add(0, part);
        return this;
    }
    
    public int getConditionCount() {
        int count = 0;
        for (int i = 0; i < this.parts.size(); ++i) {
            count += this.parts.get(i).getConditionCount();
        }
        return count;
    }
}
