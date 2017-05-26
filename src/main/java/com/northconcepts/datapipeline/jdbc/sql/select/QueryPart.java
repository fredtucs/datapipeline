package com.northconcepts.datapipeline.jdbc.sql.select;

public abstract class QueryPart
{
    public abstract String getSqlFragment();
    
    @Override
	public final String toString() {
        return this.getSqlFragment();
    }
}
