package com.northconcepts.datapipeline.jdbc;

import java.sql.ResultSet;

public interface JdbcValueReader
{
    public static final JdbcValueReader DEFAULT = new JdbcValueReader() {
        public Object readColumnValue(ResultSet result, int columnIndex, int columnType, String columnName) throws Throwable {
            if (columnType == 2004 || columnType == -2 || columnType == -4 || columnType == -3) {
                return result.getBytes(columnIndex);
            }
            if (columnType == 12 || columnType == 1 || columnType == -1 || columnType == -9 || columnType == -15 || columnType == -16 || columnType == 2005 || columnType == 2011) {
                return result.getString(columnIndex);
            }
            return result.getObject(columnIndex);
        }
    };
    
    Object readColumnValue(ResultSet p0, int p1, int p2, String p3) throws Throwable;
}
