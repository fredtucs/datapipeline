package com.northconcepts.datapipeline.jdbc.upsert;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.jdbc.JdbcUpsertWriter;

public interface IUpsert
{
    void prepare(JdbcUpsertWriter p0, Record p1) throws Throwable;
    
    void setValues(JdbcUpsertWriter p0, Record p1) throws Throwable;
    
    void executeUpdate() throws Throwable;
    
    void addBatch() throws Throwable;
    
    void executeBatch() throws Throwable;
    
    void close() throws Throwable;
    
    void addExceptionProperties(DataException p0);
}
