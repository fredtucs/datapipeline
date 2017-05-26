package com.northconcepts.datapipeline.core;

import com.northconcepts.datapipeline.internal.lang.Util;

public class ProxyWriter extends DataWriter
{
    private DataWriter nestedDataWriter;
    
    public ProxyWriter(final DataWriter nestedDataWriter) {
        super();
        this.nestedDataWriter = nestedDataWriter;
    }
    
    @Override
	public DataWriter getNestedWriter() {
        return this.nestedDataWriter;
    }
    
    protected void setNestedDataWriter(final DataWriter nestedDataWriter) {
        this.setNestedDataWriter(nestedDataWriter, false);
    }
    
    protected void setNestedDataWriter(final DataWriter nestedDataWriter, final boolean manageLifecycle) throws DataException {
        if (nestedDataWriter == null) {
            throw this.exception("nestedDataWriter is null");
        }
        if (manageLifecycle) {
            Util.close(this.nestedDataWriter);
            this.nestedDataWriter = nestedDataWriter;
            if (this.isOpen()) {
                Util.open(this.nestedDataWriter);
            }
        }
        else {
            this.nestedDataWriter = nestedDataWriter;
        }
    }
    
    @Override
	public int available() throws DataException {
        return this.nestedDataWriter.available() + super.available();
    }
    
    @Override
	public void open() throws DataException {
        super.open();
        this.nestedDataWriter.open();
    }
    
    @Override
	public void close() throws DataException {
        this.nestedDataWriter.close();
        super.close();
    }
    
    protected Record interceptRecord(final Record record) throws Throwable {
        return record;
    }
    
    @Override
	protected void writeImpl(Record record) throws Throwable {
        record = this.interceptRecord(record);
        if (record != null) {
            this.nestedDataWriter.write(record);
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        super.addExceptionProperties(exception);
        if (this.nestedDataWriter != null) {
            this.nestedDataWriter.addExceptionProperties(exception);
        }
        return exception;
    }
}
