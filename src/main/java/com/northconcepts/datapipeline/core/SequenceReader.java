package com.northconcepts.datapipeline.core;

import java.util.ArrayList;

public class SequenceReader extends DataReader
{
    private ArrayList<DataReader> list;
    private int index;
    
    public SequenceReader() {
        super();
        this.list = new ArrayList<DataReader>();
        this.index = 0;
    }
    
    public SequenceReader add(final DataReader reader) {
        this.assertNotOpened();
        this.list.add(reader);
        return this;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("SequenceReader.count", this.getCount());
        exception.set("SequenceReader.index", this.index);
        if (this.isOpen() && this.index < this.getCount()) {
            final DataReader reader = this.get(this.index);
            if (reader != null) {
                exception.set("SequenceReader.currentReaderClass", reader.getClass());
                reader.addExceptionProperties(exception);
            }
        }
        return super.addExceptionProperties(exception);
    }
    
    public int getCount() {
        return this.list.size();
    }
    
    public SequenceReader remove(final DataReader reader) {
        this.assertNotOpened();
        this.list.remove(reader);
        return this;
    }
    
    public SequenceReader removeAllDataReader() {
        this.assertNotOpened();
        this.list.clear();
        return this;
    }
    
    public DataReader get(final int index) {
        return this.list.get(index);
    }
    
    @Override
	public void open() throws DataException {
        if (this.list.size() == 0) {
            throw this.exception("No DataReaders have been added to this SequenceReader");
        }
        super.open();
        for (int i = 0; i < this.list.size(); ++i) {
            this.get(i).open();
        }
    }
    
    @Override
	public int available() {
        int available = super.available();
        for (int i = 0; i < this.list.size(); ++i) {
            available += this.get(i).available();
        }
        return available;
    }
    
    @Override
	public void close() throws DataException {
        DataException exception = null;
        for (int i = 0; i < this.list.size(); ++i) {
            final DataEndpoint endpoint = this.get(i);
            try {
                if (endpoint.isOpen()) {
                    endpoint.close();
                }
            }
            catch (Throwable e) {
                if (exception == null) {
                    exception = endpoint.exception(e);
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
        super.close();
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        Record record;
        for (record = this.get(this.index).read(); record == null && this.index + 1 < this.list.size(); record = this.readImpl()) {
            ++this.index;
        }
        return record;
    }
}
