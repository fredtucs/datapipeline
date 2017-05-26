package com.northconcepts.datapipeline.jdbc.sql.select;

public class InnerJoin extends QueryPart
{
    private final String table;
    private final String condition;
    
    public InnerJoin(final String table, final String condition) {
        super();
        this.table = table;
        this.condition = condition;
    }
    
    public String getTable() {
        return this.table;
    }
    
    public String getCondition() {
        return this.condition;
    }
    
    @Override
	public String getSqlFragment() {
        return "INNER JOIN " + this.table + " ON (" + this.condition + ")";
    }
}
