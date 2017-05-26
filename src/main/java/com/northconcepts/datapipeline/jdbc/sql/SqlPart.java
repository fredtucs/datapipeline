package com.northconcepts.datapipeline.jdbc.sql;

public abstract class SqlPart
{
    public abstract String getSqlFragment();
    
    @Override
	public final String toString() {
        return this.getSqlFragment();
    }
}
