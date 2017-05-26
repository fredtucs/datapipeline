package com.northconcepts.datapipeline.jdbc.sql.update;

import java.util.List;

import com.northconcepts.datapipeline.jdbc.sql.SqlPart;
import com.northconcepts.datapipeline.jdbc.sql.SqlValueContainer;

public final class UpdateValue extends SqlPart implements SqlValueContainer
{
    private final String columnName;
    private final Object value;
    
    public UpdateValue(final String columnName, final Object value) {
        super();
        this.columnName = columnName;
        this.value = value;
    }
    
    public UpdateValue(final String columnName) {
        this(columnName, null);
    }
    
    public String getColumnName() {
        return this.columnName;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void getPartParameterValues(final List<Object> values) {
        values.add(this.value);
    }
    
    @Override
	public String getSqlFragment() {
        return this.columnName + "=?";
    }
}
