package com.northconcepts.datapipeline.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;
import com.northconcepts.datapipeline.jdbc.upsert.GenericUpsert;
import com.northconcepts.datapipeline.jdbc.upsert.IUpsert;

public class JdbcUpsertWriter extends DataWriter
{
    private final Connection connection;
    private boolean autoCloseConnection;
    private final String tableName;
    private final HashMap<String, Integer> jdbcTypes;
    private int batchSize;
    protected Record currentRecord;
    private boolean connectionWasAutoCommit;
    private int batchedUpdates;
    private final IUpsert upsert;
    private final FieldList keyFields;
    private boolean prepared;
    
    public JdbcUpsertWriter(final Connection connection, final String tableName, final IUpsert upsert, final String firstFieldName, final String... otherFieldNames) {
        super();
        this.jdbcTypes = new HashMap<String, Integer>();
        this.batchSize = -1;
        this.connection = connection;
        this.tableName = tableName;
        this.upsert = upsert;
        this.keyFields = new FieldList(firstFieldName, otherFieldNames);
    }
    
    public JdbcUpsertWriter(final Connection connection, final String tableName, final String firstFieldName, final String... otherFieldNames) {
        this(connection, tableName, new GenericUpsert(), firstFieldName, otherFieldNames);
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
    
    public JdbcUpsertWriter setAutoCloseConnection(final boolean closeConnection) {
        this.autoCloseConnection = closeConnection;
        return this;
    }
    
    public int getBatchSize() {
        return this.batchSize;
    }
    
    public JdbcUpsertWriter setBatchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }
    
    public boolean isBatchMode() {
        return this.batchSize > 1;
    }
    
    public FieldList getKeyFields() {
        return this.keyFields;
    }
    
    public IUpsert getUpsert() {
        return this.upsert;
    }
    
    public JdbcUpsertWriter setJdbcType(final String fieldName, final int jdbcType) {
        this.assertNotOpened();
        this.jdbcTypes.put(fieldName, jdbcType);
        return this;
    }
    
    public Integer getJdbcType(final String fieldName) {
        return this.jdbcTypes.get(fieldName);
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        this.upsert.addExceptionProperties(exception);
        exception.set("JdbcWriter.tableName", this.tableName);
        exception.set("JdbcWriter.JdbcTypes", this.jdbcTypes);
        exception.set("JdbcWriter.autoCloseConnection", this.autoCloseConnection);
        exception.set("JdbcWriter.isBatchMode", this.isBatchMode());
        exception.set("JdbcWriter.batchSize", this.batchSize);
        exception.set("JdbcWriter.batchedUpdates", this.batchedUpdates);
        exception.setRecord(this.currentRecord);
        return super.addExceptionProperties(exception);
    }
    
    private int getJdbcType(final int fieldIndex, final Field field) {
        Integer type = this.getJdbcType(field.getName());
        if (type == null) {
            type = TypeUtil.getJdbcType(field.getType());
        }
        return type;
    }
    
    public void setParameterValue(final Field field, final int fieldIndex, final PreparedStatement statement) throws SQLException {
        final int parameterIndex = fieldIndex + 1;
        final int jdbcType = this.getJdbcType(fieldIndex, field);
        try {
            if (field.isNull()) {
                statement.setNull(parameterIndex, jdbcType);
            }
            else {
                switch (field.getType()) {
                    case STRING: {
                        statement.setString(parameterIndex, field.getValueAsString());
                        break;
                    }
                    case INT: {
                        statement.setInt(parameterIndex, field.getValueAsInteger());
                        break;
                    }
                    case LONG: {
                        statement.setLong(parameterIndex, field.getValueAsLong());
                        break;
                    }
                    case DOUBLE: {
                        statement.setDouble(parameterIndex, field.getValueAsDouble());
                        break;
                    }
                    case DATETIME: {
                        statement.setTimestamp(parameterIndex, new Timestamp(field.getValueAsDatetime().getTime()));
                        break;
                    }
                    case BOOLEAN: {
                        statement.setBoolean(parameterIndex, field.getValueAsBoolean());
                        break;
                    }
                    case BYTE: {
                        statement.setByte(parameterIndex, field.getValueAsByte());
                        break;
                    }
                    case FLOAT: {
                        statement.setFloat(parameterIndex, field.getValueAsFloat());
                        break;
                    }
                    case SHORT: {
                        statement.setShort(parameterIndex, field.getValueAsShort());
                        break;
                    }
                    case CHAR: {
                        statement.setString(parameterIndex, field.getValueAsString());
                        break;
                    }
                    case DATE: {
                        statement.setDate(parameterIndex, field.getValueAsDate());
                        break;
                    }
                    case TIME: {
                        statement.setTime(parameterIndex, field.getValueAsTime());
                        break;
                    }
                    case BLOB: {
                        statement.setBytes(parameterIndex, field.getValueAsBytes());
                        break;
                    }
                    default: {
                        statement.setString(parameterIndex, field.getValueAsString());
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
        if (!this.prepared) {
            this.upsert.prepare(this, record);
            this.prepared = true;
        }
        this.upsert.setValues(this, record);
        if (this.isBatchMode()) {
            ++this.batchedUpdates;
            this.upsert.addBatch();
        }
        else {
            this.upsert.executeUpdate();
        }
        this.executeBatch(false);
    }
    
    private void executeBatch(final boolean finalBatch) throws Throwable {
        if (this.isBatchMode() && this.batchedUpdates > 0) {
            if (finalBatch || this.batchedUpdates % this.batchSize == 0) {
                this.upsert.executeBatch();
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
            this.upsert.close();
            if (this.autoCloseConnection) {
                this.connection.close();
            }
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
}
