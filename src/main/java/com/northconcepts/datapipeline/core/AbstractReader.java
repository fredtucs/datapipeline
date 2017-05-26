package com.northconcepts.datapipeline.core;

import com.northconcepts.datapipeline.internal.lang.Util;

public abstract class AbstractReader extends DataReader
{
    private boolean fieldNamesInFirstRow;
    protected String[] fieldNames;
    protected Record currentRecord;
    protected int startingRow;
    protected int lastRow;
    private boolean firstRow;
    
    public AbstractReader() {
        super();
        this.startingRow = 0;
        this.lastRow = -1;
        this.firstRow = true;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("AbstractReader.fieldNamesInFirstRow", this.fieldNamesInFirstRow);
        exception.set("AbstractReader.fieldNames", Util.arrayToString(this.fieldNames, ","));
        exception.set("AbstractReader.startingRow", this.startingRow);
        exception.set("AbstractReader.firstRow", this.firstRow);
        exception.set("AbstractReader.lastRow", this.lastRow);
        exception.setRecord(this.currentRecord);
        return super.addExceptionProperties(exception);
    }
    
    public boolean isFieldNamesInFirstRow() {
        return this.fieldNamesInFirstRow;
    }
    
    public AbstractReader setFieldNamesInFirstRow(final boolean fieldNamesInFirstRow) {
        this.assertNotOpened();
        this.fieldNamesInFirstRow = fieldNamesInFirstRow;
        return this;
    }
    
    public String[] getFieldNames() {
        if (this.fieldNames == null) {
            return new String[0];
        }
        return this.fieldNames.clone();
    }
    
    public AbstractReader setFieldNames(final String... fieldNames) {
        this.assertNotOpened();
        this.fieldNames = fieldNames;
        return this;
    }
    
    public AbstractReader setStartingRow(final int startingRow) {
        this.startingRow = startingRow;
        return this;
    }
    
    public int getStartingRow() {
        return this.startingRow;
    }
    
    public AbstractReader setLastRow(final int lastRow) {
        this.lastRow = lastRow;
        return this;
    }
    
    public int getLastRow() {
        return this.lastRow;
    }
    
    @Override
	public void open() throws DataException {
        this.firstRow = true;
        super.open();
    }
    
    @Override
	public Record read() throws DataException {
        this.currentRecord = null;
        return super.read();
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        try {
            if (this.fieldNames != null) {
                this.currentRecord = new Record(this.fieldNames);
            }
            else {
                this.currentRecord = new Record();
            }
            if (!this.fillRecord(this.currentRecord)) {
                return null;
            }
            if (this.firstRow) {
                this.firstRow = false;
                if (this.fieldNamesInFirstRow) {
                    if (this.fieldNames == null) {
                        this.fieldNames = new String[this.currentRecord.getFieldCount()];
                        for (int i = 0; i < this.fieldNames.length; ++i) {
                            this.fieldNames[i] = this.currentRecord.getField(i).getValueAsString();
                        }
                    }
                    this.currentRecord = this.readImpl();
                    this.decrementRecordCount();
                    return this.currentRecord;
                }
            }
            this.currentRecord.setAlive();
            return this.currentRecord;
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    protected abstract boolean fillRecord(final Record p0) throws Throwable;
}
