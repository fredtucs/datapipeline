package com.northconcepts.datapipeline.jdbc.sql.select;

public final class QueryCriteria extends QueryPart
{
    private final Object[] EMPTY_ARRAY;
    private QueryCriteriaPart rootPart;
    
    public QueryCriteria() {
        super();
        this.EMPTY_ARRAY = new Object[0];
    }
    
    public QueryCriteria(final String statement, final Object... values) {
        super();
        this.EMPTY_ARRAY = new Object[0];
        this.and(statement, values);
    }
    
    public QueryCriteriaPart getRootPart() {
        return this.rootPart;
    }
    
    public QueryCriteria setRootPart(final QueryCriteriaPart rootPart) {
        this.rootPart = rootPart;
        return this;
    }
    
    public AndPart and(final boolean first, final QueryCriteriaPart part) {
        AndPart c;
        if (this.rootPart instanceof AndPart) {
            c = (AndPart)this.rootPart;
        }
        else {
            c = new AndPart();
            if (this.rootPart != null) {
                c.getParts().add(this.rootPart);
            }
        }
        if (first) {
            c.getParts().add(0, part);
        }
        else {
            c.getParts().add(part);
        }
        return (AndPart)(this.rootPart = c);
    }
    
    public AndPart and(final QueryCriteriaPart part) {
        return this.and(false, part);
    }
    
    public AndPart and(final String statement, final Object... values) {
        return this.and(false, statement, values);
    }
    
    public AndPart and(final boolean first, final String statement, final Object... values) {
        return this.and(first, new ConditionPart(statement, values));
    }
    
    public OrPart or(final boolean first, final QueryCriteriaPart part) {
        OrPart c;
        if (this.rootPart instanceof OrPart) {
            c = (OrPart)this.rootPart;
        }
        else {
            c = new OrPart();
            if (this.rootPart != null) {
                c.getParts().add(this.rootPart);
            }
        }
        if (first) {
            c.getParts().add(0, part);
        }
        else {
            c.getParts().add(part);
        }
        return (OrPart)(this.rootPart = c);
    }
    
    public OrPart or(final QueryCriteriaPart part) {
        return this.or(false, part);
    }
    
    public OrPart or(final String statement, final Object... values) {
        return this.or(false, statement, values);
    }
    
    public OrPart or(final boolean first, final String statement, final Object... values) {
        return this.or(first, new ConditionPart(statement, values));
    }
    
    @Override
	public String getSqlFragment() {
        String s = "";
        if (this.rootPart != null && this.rootPart.isApplicable()) {
            s = s + "WHERE " + this.rootPart.getSqlFragment();
        }
        return s;
    }
    
    public final Object[] getParameterValues() {
        if (this.rootPart != null) {
            return this.rootPart.getParameterValues();
        }
        return this.EMPTY_ARRAY;
    }
}
