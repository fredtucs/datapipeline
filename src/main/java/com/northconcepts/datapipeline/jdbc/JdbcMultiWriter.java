package com.northconcepts.datapipeline.jdbc;

import java.sql.Connection;
import java.util.HashMap;

import javax.sql.DataSource;

import com.northconcepts.datapipeline.core.AsyncWriter;
import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.MultiWriter;
import com.northconcepts.datapipeline.core.ProxyWriter;

public class JdbcMultiWriter extends ProxyWriter
{
    private final MultiWriter multiWriter;
    private final JdbcWriter[] jdbcWriters;
    private final AsyncWriter[] asyncWriters;
    private final DataSource dataSource;
    private final int connections;
    private final int maxQueuedRecordsPerConnection;
    private final String tableName;
    private final HashMap<String, Integer> jdbcTypes;
    private int batchSize;
    
    public JdbcMultiWriter(final DataSource dataSource, final int connections, final int maxQueuedRecordsPerConnection, final String tableName) {
        super(new MultiWriter(new MultiWriter.AvailableCapacityWriteStrategy()));
        this.jdbcTypes = new HashMap<String, Integer>();
        this.batchSize = -1;
        this.dataSource = dataSource;
        this.connections = connections;
        this.maxQueuedRecordsPerConnection = maxQueuedRecordsPerConnection;
        this.tableName = tableName;
        this.multiWriter = (MultiWriter)this.getNestedWriter();
        this.jdbcWriters = new JdbcWriter[connections];
        this.asyncWriters = new AsyncWriter[connections];
    }
    
    public int getConnections() {
        return this.connections;
    }
    
    public JdbcWriter getJdbcWriter(final int index) {
        return this.jdbcWriters[index];
    }
    
    public AsyncWriter getAsyncWriter(final int index) {
        return this.asyncWriters[index];
    }
    
    public int getBatchSize() {
        return this.batchSize;
    }
    
    public JdbcMultiWriter setBatchSize(final int batchSize) {
        this.batchSize = batchSize;
        return this;
    }
    
    private boolean isBatchMode() {
        return this.batchSize > 1;
    }
    
    public JdbcMultiWriter setJdbcType(final String fieldName, final int jdbcType) {
        this.assertNotOpened();
        this.jdbcTypes.put(fieldName, jdbcType);
        return this;
    }
    
    @Override
	public void open() throws DataException {
        for (int i = 0; i < this.connections; ++i) {
            try {
                DataEndpoint.log.debug("creating connection " + i + "...");
                final Connection c = this.dataSource.getConnection();
                final JdbcWriter jdbcWriter = new JdbcWriter(c, this.tableName).setAutoCloseConnection(true).setBatchSize(this.getBatchSize());
                for (final String fieldName : this.jdbcTypes.keySet()) {
                    jdbcWriter.setJdbcType(fieldName, this.jdbcTypes.get(fieldName));
                }
                this.jdbcWriters[i] = jdbcWriter;
                this.asyncWriters[i] = new AsyncWriter(jdbcWriter, this.maxQueuedRecordsPerConnection);
                this.multiWriter.add(this.asyncWriters[i]);
            }
            catch (Throwable e) {
                throw this.exception(e).set("connectionIndex", i);
            }
        }
        super.open();
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("JdbcMultiWriter.dataSource", this.dataSource);
        exception.set("JdbcMultiWriter.connections", this.connections);
        exception.set("JdbcMultiWriter.maxQueuedRecordsPerConnection", this.maxQueuedRecordsPerConnection);
        exception.set("JdbcMultiWriter.tableName", this.tableName);
        exception.set("JdbcMultiWriter.JdbcTypes", this.jdbcTypes);
        exception.set("JdbcMultiWriter.isBatchMode", this.isBatchMode());
        exception.set("JdbcMultiWriter.batchSize", this.batchSize);
        return super.addExceptionProperties(exception);
    }
}
