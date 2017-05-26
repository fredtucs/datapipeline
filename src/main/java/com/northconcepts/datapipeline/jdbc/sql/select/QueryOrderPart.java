package com.northconcepts.datapipeline.jdbc.sql.select;

public final class QueryOrderPart extends QueryPart
{
    private String field;
    private boolean ascending;
    
    public QueryOrderPart() {
        super();
    }
    
    public QueryOrderPart(final String field, final boolean ascending) {
        super();
        this.field = field;
        this.ascending = ascending;
    }
    
    public String getField() {
        return this.field;
    }
    
    public QueryOrderPart setField(final String field) {
        this.field = field;
        return this;
    }
    
    public boolean isAscending() {
        return this.ascending;
    }
    
    public QueryOrderPart setAscending(final boolean ascending) {
        this.ascending = ascending;
        return this;
    }
    
    @Override
	public String getSqlFragment() {
        if (this.ascending) {
            return this.field;
        }
        return this.field + " DESC";
    }
}
