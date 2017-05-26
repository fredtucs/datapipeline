package com.northconcepts.datapipeline.core;

import com.northconcepts.datapipeline.internal.lang.Util;

public class ProxyReader extends DataReader
{
    private DataReader nestedDataReader;
    
    public ProxyReader(final DataReader nestedDataReader) {
        super();
        this.nestedDataReader = nestedDataReader;
    }
    
    @Override
	public DataReader getNestedReader() {
        return this.nestedDataReader;
    }
    
    protected void setNestedDataReader(final DataReader nestedDataReader) {
        this.setNestedDataReader(nestedDataReader, false);
    }
    
    protected void setNestedDataReader(final DataReader nestedDataReader, final boolean manageLifecycle) throws DataException {
        if (nestedDataReader == null) {
            throw this.exception("nestedDataReader is null");
        }
        if (manageLifecycle) {
            Util.close(this.nestedDataReader);
            Util.open(this.nestedDataReader = nestedDataReader);
        }
        else {
            this.nestedDataReader = nestedDataReader;
        }
    }
    
    @Override
	public int available() throws DataException {
        return this.nestedDataReader.available() + super.available();
    }
    
    @Override
	public void open() throws DataException {
        super.open();
        this.nestedDataReader.open();
    }
    
    @Override
	public void close() throws DataException {
        this.nestedDataReader.close();
        super.close();
    }
    
    protected Record interceptRecord(final Record record) throws Throwable {
        return record;
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        Record record;
        do {
            record = null;
            record = this.nestedDataReader.read();
            if (record == null) {
                return null;
            }
            record = this.interceptRecord(record);
        } while (record == null || record.isDeleted());
        return record;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        super.addExceptionProperties(exception);
        if (this.nestedDataReader != null) {
            this.nestedDataReader.addExceptionProperties(exception);
        }
        return exception;
    }
}
