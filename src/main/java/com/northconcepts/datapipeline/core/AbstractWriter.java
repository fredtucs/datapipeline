package com.northconcepts.datapipeline.core;

public abstract class AbstractWriter extends DataWriter
{
    private boolean fieldNamesInFirstRow;
    private boolean firstRow;
    protected Record currentRecord;
    
    public AbstractWriter() {
        super();
        this.fieldNamesInFirstRow = true;
        this.firstRow = true;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("AbstractWriter.fieldNamesInFirstRow", this.fieldNamesInFirstRow);
        exception.set("AbstractWriter.firstRow", this.firstRow);
        exception.setRecord(this.currentRecord);
        return super.addExceptionProperties(exception);
    }
    
    public AbstractWriter setFieldNamesInFirstRow(final boolean fieldNamesInFirstRow) {
        this.assertNotOpened();
        this.fieldNamesInFirstRow = fieldNamesInFirstRow;
        return this;
    }
    
    public boolean isFieldNamesInFirstRow() {
        return this.fieldNamesInFirstRow;
    }
    
    @Override
	public void open() {
        this.firstRow = true;
        super.open();
    }
    
    @Override
	public void write(final Record record) throws DataException {
        super.write(this.currentRecord = record);
    }
    
    @Override
	protected void writeImpl(final Record record) throws Throwable {
        if (this.firstRow) {
            this.firstRow = false;
            if (this.fieldNamesInFirstRow) {
                this.writeRecord(this.createHeaderRecord(record));
            }
        }
        this.writeRecord(record);
    }
    
    private Record createHeaderRecord(Record record) {
        record = (Record)record.clone();
        for (int i = 0; i < record.getFieldCount(); ++i) {
            final Field field = record.getField(i);
            field.setValue(field.getName());
        }
        return record;
    }
    
    protected abstract void writeRecord(final Record p0) throws Throwable;
}
