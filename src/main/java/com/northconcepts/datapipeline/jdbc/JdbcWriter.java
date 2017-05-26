package com.northconcepts.datapipeline.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;
import com.northconcepts.datapipeline.internal.lang.Util;

public class JdbcWriter extends DataWriter
{
    private final Connection connection;
    private boolean autoCloseConnection;
    private final String tableName;
    private String queryString;
    private final HashMap<String, Integer> jdbcTypes;
    private Integer[] jdbcTypesArray;
    private int batchSize;
    private PreparedStatement statement;
    protected Record currentRecord;
    private boolean connectionWasAutoCommit;
    private int batchedUpdates;
    
    public JdbcWriter(final Connection connection, final String tableName) {
        super();
        this.queryString = null;
        this.jdbcTypes = new HashMap<String, Integer>();
        this.batchSize = -1;
        this.statement = null;
        this.connection = connection;
        this.tableName = tableName;
    }
    
    public Connection getConnection() {
        return this.connection;
    }
    
    public String getTableName() {
        return this.tableName;
    }
    
    public boolean getAutoCloseConnection() {
        return this.autoCloseConnection;
    }
    
    public JdbcWriter setAutoCloseConnection(final boolean closeConnection) {
        this.autoCloseConnection = closeConnection;
        return this;
    }
    
    public int getBatchSize() {
        return this.batchSize;
    }
    
    public JdbcWriter setBatchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }
    
    public boolean isBatchMode() {
        return this.batchSize > 1;
    }
    
    public JdbcWriter setJdbcType(final String fieldName, final int jdbcType) {
        this.assertNotOpened();
        this.jdbcTypes.put(fieldName, jdbcType);
        return this;
    }
    
    public Integer getJdbcType(final String fieldName) {
        return this.jdbcTypes.get(fieldName);
    }
    
    private String buildSql(final Record record) {
        this.jdbcTypesArray = new Integer[record.getFieldCount()];
        final StringBuilder fieldNames = new StringBuilder(record.getFieldCount() * 20);
        final StringBuilder fieldValues = new StringBuilder(record.getFieldCount() * 1);
        fieldNames.append("(");
        fieldValues.append("VALUES (");
        final String queryString = "INSERT INTO " + this.tableName + " ";
        for (int i = 0; i < record.getFieldCount(); ++i) {
            final String fieldName = record.getField(i).getName();
            fieldNames.append(fieldName + ", ");
            fieldValues.append("?, ");
            this.jdbcTypesArray[i] = this.jdbcTypes.get(fieldName);
        }
        fieldNames.delete(fieldNames.length() - 2, fieldNames.length());
        fieldNames.append(")").append(' ');
        fieldValues.delete(fieldValues.length() - 2, fieldValues.length());
        fieldValues.append(")");
        return queryString + fieldNames.toString() + fieldValues.toString();
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("JdbcWriter.queryString", this.queryString);
        exception.set("JdbcWriter.tableName", this.tableName);
        exception.set("JdbcWriter.JdbcTypes", this.jdbcTypes);
        exception.set("JdbcWriter.jdbcTypesArray", Util.arrayToString(this.jdbcTypesArray, ", "));
        exception.set("JdbcWriter.autoCloseConnection", this.autoCloseConnection);
        exception.set("JdbcWriter.isBatchMode", this.isBatchMode());
        exception.set("JdbcWriter.batchSize", this.batchSize);
        exception.set("JdbcWriter.batchedUpdates", this.batchedUpdates);
        exception.setRecord(this.currentRecord);
        return super.addExceptionProperties(exception);
    }
    
    private int getJdbcType(final int fieldIndex, final Field field) {
        int jdbcType;
        if (this.jdbcTypesArray[fieldIndex] != null) {
            jdbcType = this.jdbcTypesArray[fieldIndex];
        }
        else {
            jdbcType = TypeUtil.getJdbcType(field.getType());
        }
        return jdbcType;
    }
    
    private void setParameterValue(final Field field, final int fieldIndex) throws SQLException {
        final int parameterIndex = fieldIndex + 1;
        final int jdbcType = this.getJdbcType(fieldIndex, field);
        try {
            if (field.isNull()) {
                this.statement.setNull(parameterIndex, jdbcType);
            }
            else {
                switch (field.getType()) {
                    case STRING: {
                        this.statement.setString(parameterIndex, field.getValueAsString());
                        break;
                    }
                    case INT: {
                        this.statement.setInt(parameterIndex, field.getValueAsInteger());
                        break;
                    }
                    case LONG: {
                        this.statement.setLong(parameterIndex, field.getValueAsLong());
                        break;
                    }
                    case DOUBLE: {
                        this.statement.setDouble(parameterIndex, field.getValueAsDouble());
                        break;
                    }
                    case DATETIME: {
                        this.statement.setTimestamp(parameterIndex, new Timestamp(field.getValueAsDatetime().getTime()));
                        break;
                    }
                    case BOOLEAN: {
                        this.statement.setBoolean(parameterIndex, field.getValueAsBoolean());
                        break;
                    }
                    case BYTE: {
                        this.statement.setByte(parameterIndex, field.getValueAsByte());
                        break;
                    }
                    case FLOAT: {
                        this.statement.setFloat(parameterIndex, field.getValueAsFloat());
                        break;
                    }
                    case SHORT: {
                        this.statement.setShort(parameterIndex, field.getValueAsShort());
                        break;
                    }
                    case CHAR: {
                        this.statement.setString(parameterIndex, field.getValueAsString());
                        break;
                    }
                    case DATE: {
                        this.statement.setDate(parameterIndex, field.getValueAsDate());
                        break;
                    }
                    case TIME: {
                        this.statement.setTime(parameterIndex, field.getValueAsTime());
                        break;
                    }
                    case BLOB: {
                        this.statement.setBytes(parameterIndex, field.getValueAsBytes());
                        break;
                    }
                    default: {
                        this.statement.setString(parameterIndex, field.getValueAsString());
                        break;
                    }
                }
            }
        }
        catch (Throwable e) {
            final DataException exception = this.exception(e);
            exception.set("JdbcWriter.parameterFieldIndex", fieldIndex);
            exception.set("JdbcWriter.parameterIndex", parameterIndex);
            exception.set("JdbcWriter.parameterJdbcType", jdbcType);
            exception.set("JdbcWriter.parameterValue", field);
            throw exception;
        }
    }
    
    @Override
	public void write(final Record record) throws DataException {
        super.write(this.currentRecord = record);
    }
    
    @Override
	protected void writeImpl(final Record record) throws Throwable {
        this.currentRecord = record;
        if (this.queryString == null) {
            this.queryString = this.buildSql(record);
            this.statement = this.connection.prepareStatement(this.queryString);
        }
        for (int i = 0; i < record.getFieldCount(); ++i) {
            final Field field = record.getField(i);
            this.setParameterValue(field, i);
        }
        if (this.isBatchMode()) {
            ++this.batchedUpdates;
            this.statement.addBatch();
        }
        else {
            this.statement.executeUpdate();
        }
        this.executeBatch(false);
    }
    
    private void executeBatch(final boolean finalBatch) throws SQLException {
        if (this.isBatchMode() && this.batchedUpdates > 0) {
            if (finalBatch || this.batchedUpdates % this.batchSize == 0) {
                this.statement.executeBatch();
                this.batchedUpdates = 0;
            }
            if (finalBatch) {
                this.connection.commit();
            }
        }
    }
    
    @Override
	public void open() {
        try {
            this.connectionWasAutoCommit = this.connection.getAutoCommit();
            if (this.isBatchMode()) {
                this.connection.setAutoCommit(false);
            }
            super.open();
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public void close() {
        try {
            this.executeBatch(true);
            super.close();
            this.connection.setAutoCommit(this.connectionWasAutoCommit);
            if (this.statement != null) {
                this.statement.close();
            }
            if (this.autoCloseConnection) {
                this.connection.close();
            }
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
}
