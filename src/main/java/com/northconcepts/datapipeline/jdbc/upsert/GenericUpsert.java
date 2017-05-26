package com.northconcepts.datapipeline.jdbc.upsert;

import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.jdbc.JdbcUpsertWriter;
import com.northconcepts.datapipeline.jdbc.sql.insert.Insert;
import com.northconcepts.datapipeline.jdbc.sql.update.Update;

public class GenericUpsert implements IUpsert
{
    private String insertSql;
    private PreparedStatement insertStatement;
    private String updateSql;
    private PreparedStatement updateStatement;
    private FieldList keyFields;
    private FieldList nonKeyFields;
    
    public void prepare(final JdbcUpsertWriter writer, final Record record) throws Throwable {
        final Insert insert = new Insert(writer.getTableName());
        for (int i = 0; i < record.getFieldCount(); ++i) {
            insert.add(record.getField(i).getName());
        }
        this.insertSql = insert.getSqlFragment();
        this.insertStatement = writer.getConnection().prepareStatement(this.insertSql);
        final Update update = new Update(writer.getTableName());
        this.keyFields = writer.getKeyFields();
        this.nonKeyFields = record.getFieldNameList().remove(this.keyFields);
        for (int j = 0; j < this.nonKeyFields.size(); ++j) {
            update.set(this.nonKeyFields.get(j));
        }
        for (int j = 0; j < this.keyFields.size(); ++j) {
            update.where(this.keyFields.get(j) + "=?", new Object[0]);
        }
        this.updateSql = update.getSqlFragment();
        this.updateStatement = writer.getConnection().prepareStatement(this.updateSql);
    }
    
    public void setValues(final JdbcUpsertWriter writer, final Record record) throws Throwable {
        this.insertStatement.clearParameters();
        for (int i = 0; i < record.getFieldCount(); ++i) {
            final Field field = record.getField(i);
            writer.setParameterValue(field, i, this.insertStatement);
        }
        this.updateStatement.clearParameters();
        for (int i = 0; i < this.nonKeyFields.size(); ++i) {
            final String fieldName = this.nonKeyFields.get(i);
            final Field field2 = record.getField(fieldName);
            writer.setParameterValue(field2, i, this.updateStatement);
        }
        for (int i = 0; i < this.keyFields.size(); ++i) {
            final String fieldName = this.keyFields.get(i);
            final Field field2 = record.getField(fieldName);
            writer.setParameterValue(field2, i + this.nonKeyFields.size(), this.updateStatement);
        }
    }
    
    public void executeUpdate() throws Throwable {
        try {
            this.insertStatement.executeUpdate();
        }
        catch (SQLIntegrityConstraintViolationException e) {
            this.updateStatement.executeUpdate();
        }
    }
    
    public void addBatch() throws Throwable {
        throw new UnsupportedOperationException("batching is not supported");
    }
    
    public void executeBatch() throws Throwable {
        throw new UnsupportedOperationException("batching is not supported");
    }
    
    public void close() throws Throwable {
        if (this.insertStatement != null) {
            this.insertStatement.close();
        }
        if (this.updateStatement != null) {
            this.updateStatement.close();
        }
    }
    
    public void addExceptionProperties(final DataException exception) {
        exception.set("insertSql", this.insertSql);
        exception.set("updateSql", this.updateSql);
    }
}
