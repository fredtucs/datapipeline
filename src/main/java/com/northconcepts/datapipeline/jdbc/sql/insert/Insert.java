package com.northconcepts.datapipeline.jdbc.sql.insert;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.jdbc.sql.SqlPart;

public class Insert extends SqlPart
{
    private final String table;
    private final List<InsertValue> values;
    
    public Insert(final String table) {
        super();
        this.values = new ArrayList<InsertValue>();
        this.table = table;
    }
    
    public String getTable() {
        return this.table;
    }
    
    public Insert add(final String columnName, final Object value) {
        this.values.add(new InsertValue(columnName, value));
        return this;
    }
    
    public Insert add(final String... columnNames) {
        if (columnNames != null) {
            for (final String columnName : columnNames) {
                this.values.add(new InsertValue(columnName));
            }
        }
        return this;
    }
    
    @Override
	public String getSqlFragment() {
        final String s = "INSERT INTO " + this.table + " (" + Util.collectionToString(this.values, "%s", ", ") + ")" + " VALUES (" + Util.collectionToString(this.values, "?", ", ") + ")";
        return s;
    }
}
