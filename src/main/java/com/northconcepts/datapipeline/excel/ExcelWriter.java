package com.northconcepts.datapipeline.excel;

import com.northconcepts.datapipeline.core.AbstractWriter;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;

public class ExcelWriter extends AbstractWriter
{
    private String sheetName;
    private int sheetIndex;
    private int currentRowIndex;
    private int firstRowIndex;
    private int firstColumnIndex;
    private boolean autofitColumns;
    private int[] columnCharacterCount;
    private final ExcelDocument document;
    
    public ExcelWriter(final ExcelDocument document) {
        super();
        this.sheetIndex = -1;
        this.currentRowIndex = 0;
        this.firstRowIndex = 0;
        this.firstColumnIndex = 0;
        if (document == null) {
            throw new NullPointerException("document is null");
        }
        this.document = document;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("ExcelWriter.currentRowNumber", this.firstRowIndex);
        exception.set("ExcelWriter.sheetName", this.sheetName);
        exception.set("ExcelWriter.sheetIndex", this.sheetIndex);
        exception.set("ExcelWriter.firstFieldIndex", this.firstColumnIndex);
        return super.addExceptionProperties(exception);
    }
    
    public boolean isAutofitColumns() {
        return this.autofitColumns;
    }
    
    public ExcelWriter setAutofitColumns(final boolean autofitColumns) {
        this.autofitColumns = autofitColumns;
        return this;
    }
    
    public String getSheetName() {
        return this.sheetName;
    }
    
    public ExcelWriter setSheetName(final String sheetName) {
        this.sheetName = sheetName;
        return this;
    }
    
    public int getSheetIndex() {
        return this.sheetIndex;
    }
    
    public ExcelWriter setSheetIndex(final int sheetIndex) {
        this.sheetIndex = sheetIndex;
        return this;
    }
    
    public int getFirstColumnIndex() {
        return this.firstColumnIndex;
    }
    
    public ExcelWriter setFirstColumnIndex(final int firstColumnIndex) {
        this.firstColumnIndex = firstColumnIndex;
        return this;
    }
    
    public int getFirstRowIndex() {
        return this.firstRowIndex;
    }
    
    public ExcelWriter setFirstRowIndex(final int firstRowIndex) {
        this.firstRowIndex = firstRowIndex;
        return this;
    }
    
    @Override
	public void open() {
        super.open();
        this.columnCharacterCount = null;
        this.document.getProvider().startWriting(this.sheetName, this.sheetIndex);
    }
    
    @Override
	public void close() {
        if (this.autofitColumns) {
            this.document.getProvider().setColumnWidths(this.firstColumnIndex, this.columnCharacterCount);
        }
        this.document.getProvider().endWriting();
        super.close();
    }
    
    @Override
	protected void writeRecord(final Record record) {
        for (int fieldCount = record.getFieldCount(), fieldIndex = 0; fieldIndex < fieldCount; ++fieldIndex) {
            final Field field = record.getField(fieldIndex);
            this.document.getProvider().writeField(this.firstRowIndex + this.currentRowIndex, this.firstColumnIndex + fieldIndex, this.currentRowIndex, fieldIndex, field);
            if (this.autofitColumns) {
                if (this.columnCharacterCount == null) {
                    this.columnCharacterCount = new int[fieldCount];
                }
                else if (this.columnCharacterCount.length < fieldCount) {
                    final int[] temp = new int[fieldCount];
                    System.arraycopy(this.columnCharacterCount, 0, temp, 0, this.columnCharacterCount.length);
                    this.columnCharacterCount = temp;
                }
                this.columnCharacterCount[fieldIndex] = (field.isNull() ? 0 : field.getValueAsString().length());
            }
        }
        ++this.currentRowIndex;
    }
}
