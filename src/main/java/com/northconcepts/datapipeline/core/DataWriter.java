package com.northconcepts.datapipeline.core;

public abstract class DataWriter extends DataEndpoint
{
    public DataWriter getNestedWriter() {
        return null;
    }
    
    public DataWriter getRootWriter() {
        if (this.getNestedWriter() == null) {
            return this;
        }
        return this.getNestedWriter().getRootWriter();
    }
    
    public int available() throws DataException {
        return 0;
    }
    
    protected abstract void writeImpl(final Record p0) throws Throwable;
    
    public void write(final Record record) throws DataException {
        try {
            this.assertOpened();
            if (Product.allowWrite(this, record)) {
                this.writeImpl(record);
                this.incrementRecordCount();
            }
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("DataWriter.recordCount", this.getRecordCountAsString());
        return super.addExceptionProperties(exception);
    }
}
