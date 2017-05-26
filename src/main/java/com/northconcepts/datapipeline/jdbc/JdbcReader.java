package com.northconcepts.datapipeline.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.Util;

public class JdbcReader extends DataReader
{
    private final Connection connection;
    private boolean autoCloseConnection;
    private String queryString;
    private int[] columnTypes;
    private String[] columnNames;
    private ResultSet result;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private Object[] parameters;
    private int[] parameterSqlTypes;
    protected Record currentRecord;
    private JdbcValueReader valueReader;
    private boolean useColumnLabel;
    
    public JdbcReader(final Connection connection, final String queryString) {
        super();
        this.queryString = null;
        this.result = null;
        this.statement = null;
        this.preparedStatement = null;
        this.parameters = null;
        this.parameterSqlTypes = null;
        this.valueReader = JdbcValueReader.DEFAULT;
        this.connection = connection;
        this.queryString = queryString;
    }
    
    public JdbcReader(final Connection connection, final String queryString, final Object... parameters) {
        this(connection, queryString);
        this.parameters = parameters;
    }
    
    public JdbcReader(final Connection connection, final String queryString, final Object[] parameters, final int[] parameterSqlTypes) {
        this(connection, queryString, parameters);
        this.parameterSqlTypes = parameterSqlTypes;
    }
    
    public boolean getAutoCloseConnection() {
        return this.autoCloseConnection;
    }
    
    public JdbcReader setAutoCloseConnection(final boolean closeConnection) {
        this.autoCloseConnection = closeConnection;
        return this;
    }
    
    public JdbcValueReader getValueReader() {
        return this.valueReader;
    }
    
    public JdbcReader setValueReader(JdbcValueReader valueReader) {
        if (valueReader == null) {
            valueReader = JdbcValueReader.DEFAULT;
        }
        this.valueReader = valueReader;
        return this;
    }
    
    public boolean getUseColumnLabel() {
        return this.useColumnLabel;
    }
    
    public JdbcReader setUseColumnLabel(final boolean useColumnLabel) {
        this.useColumnLabel = useColumnLabel;
        return this;
    }
    
    private void connectDatabase() {
        try {
            if (this.parameters != null) {
                this.preparedStatement = this.connection.prepareStatement(this.queryString);
                for (int i = 0; i < this.parameters.length; ++i) {
                    if (this.parameterSqlTypes != null) {
                        this.preparedStatement.setObject(i + 1, this.parameters[i], this.parameterSqlTypes[i]);
                    }
                    else {
                        this.preparedStatement.setObject(i + 1, this.parameters[i]);
                    }
                }
                this.result = this.preparedStatement.executeQuery();
            }
            else {
                this.statement = this.connection.createStatement();
                this.result = this.statement.executeQuery(this.queryString);
            }
            final ResultSetMetaData metaData = this.result.getMetaData();
            this.columnTypes = new int[metaData.getColumnCount()];
            this.columnNames = new String[metaData.getColumnCount()];
            for (int j = 0; j < this.columnTypes.length; ++j) {
                this.columnTypes[j] = metaData.getColumnType(j + 1);
                if (this.useColumnLabel) {
                    this.columnNames[j] = metaData.getColumnLabel(j + 1);
                }
                else {
                    this.columnNames[j] = metaData.getColumnName(j + 1);
                }
            }
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("JdbcReader.queryString", this.queryString);
        exception.set("JdbcReader.columnTypes", Util.arrayToString(this.columnTypes, ", "));
        exception.set("JdbcReader.columnNames", Util.arrayToString(this.columnNames, ", "));
        exception.set("JdbcReader.autoCloseConnection", this.autoCloseConnection);
        exception.set("JdbcReader.useColumnLabel", this.useColumnLabel);
        exception.setRecord(this.currentRecord);
        return super.addExceptionProperties(exception);
    }
    
    @Override
	public Record read() throws DataException {
        this.currentRecord = null;
        return super.read();
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        if (!this.result.next()) {
            return null;
        }
        this.currentRecord = new Record(this.columnNames);
        for (int i = 0; i < this.columnNames.length; ++i) {
            try {
                final Object value = this.valueReader.readColumnValue(this.result, i + 1, this.columnTypes[i], this.columnNames[i]);
                this.currentRecord.getField(i).setValue(value);
            }
            catch (Throwable e) {
                throw this.exception(e).set("JdbcReader.currentColumnIndex", i).set("JdbcReader.currentColumnName", this.columnNames[i]).set("JdbcReader.currentColumnType", this.columnTypes[i]);
            }
        }
        return this.currentRecord;
    }
    
    @Override
	public void open() {
        super.open();
        this.connectDatabase();
    }
    
    @Override
	public void close() {
        super.close();
        try {
            this.result.close();
            if (this.statement != null) {
                this.statement.close();
            }
            if (this.preparedStatement != null) {
                this.preparedStatement.close();
            }
            if (this.autoCloseConnection) {
                this.connection.close();
            }
        }
        catch (SQLException e) {
            throw this.exception(e);
        }
    }
}
