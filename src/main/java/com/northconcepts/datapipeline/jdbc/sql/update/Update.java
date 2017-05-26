package com.northconcepts.datapipeline.jdbc.sql.update;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.jdbc.sql.SqlPart;
import com.northconcepts.datapipeline.jdbc.sql.select.QueryCriteria;

public class Update extends SqlPart
{
    private final String table;
    private final List<UpdateValue> values;
    private final QueryCriteria criteria;
    
    public Update(final String table) {
        super();
        this.values = new ArrayList<UpdateValue>();
        this.criteria = new QueryCriteria();
        this.table = table;
    }
    
    public String getTable() {
        return this.table;
    }
    
    public Update set(final String columnName, final Object value) {
        this.values.add(new UpdateValue(columnName, value));
        return this;
    }
    
    public Update set(final String columnName) {
        return this.set(columnName, null);
    }
    
    public QueryCriteria getCriteria() {
        return this.criteria;
    }
    
    public Update where(final String criteria, final Object... value) {
        this.criteria.and(criteria, value);
        return this;
    }
    
    @Override
	public String getSqlFragment() {
        final String s = "UPDATE " + this.table + " SET " + Util.collectionToString(this.values, "%s", ", ") + " " + this.criteria.getSqlFragment();
        return s;
    }
}
