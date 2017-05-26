package com.northconcepts.datapipeline.transform.lookup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;

public class JdbcLookup extends Lookup
{
    private final Connection connection;
    private final String query;
    private final int[] JdbcTypes;
    private PreparedStatement statement;
    
    public JdbcLookup(final Connection connection, final String query, final int... JdbcTypes) {
        super();
        this.connection = connection;
        this.query = query;
        this.JdbcTypes = JdbcTypes;
    }
    
    public JdbcLookup(final Connection connection, final String query) {
        this(connection, query, (int[])null);
    }
    
    private void prepare() throws SQLException {
        if (this.statement == null) {
            synchronized (this) {
                if (this.statement == null) {
                    this.statement = this.connection.prepareStatement(this.query);
                }
            }
        }
    }
    
    @Override
	public RecordList get(final Object... args) {
        ResultSet result = null;
        try {
            try {
                this.prepare();
                if (this.JdbcTypes == null || this.JdbcTypes.length == 0) {
                    for (int i = 0; i < args.length; ++i) {
                        this.statement.setObject(i + 1, args[i]);
                    }
                }
                else {
                    if (this.JdbcTypes.length != args.length) {
                        throw new DataException("# of JdbcTypes does not match arguments").set("JdbcTypes.length", this.JdbcTypes.length).set("args.length", args.length);
                    }
                    for (int i = 0; i < args.length; ++i) {
                        final Object arg = args[i];
                        if (arg == null) {
                            this.statement.setNull(i + 1, this.JdbcTypes[i]);
                        }
                        else {
                            this.statement.setObject(i + 1, arg, this.JdbcTypes[i]);
                        }
                    }
                }
                result = this.statement.executeQuery();
                final ResultSetMetaData metaData = result.getMetaData();
                final String[] columnNames = new String[metaData.getColumnCount()];
                for (int j = 0; j < columnNames.length; ++j) {
                    columnNames[j] = metaData.getColumnName(j + 1);
                }
                final RecordList list = new RecordList(1);
                while (result.next()) {
                    final Record record = new Record(columnNames);
                    for (int k = 0; k < columnNames.length; ++k) {
                        record.getField(k).setValue(result.getObject(k + 1));
                    }
                    list.add(record);
                }
                return list;
            }
            finally {
                if (result != null) {
                    result.close();
                    result = null;
                }
            }
        }
        catch (SQLException e) {
            try {
                if (this.statement != null) {
                    this.statement.close();
                    this.statement = null;
                }
            }
            finally {
                throw DataException.wrap(e);
            }
        }
    }
    
    @Override
	public String toString() {
        return "JdbcLookup \"" + this.query + "\"";
    }
}
