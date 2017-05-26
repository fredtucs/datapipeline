package com.northconcepts.datapipeline.excel;

import com.northconcepts.datapipeline.core.AbstractReader;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;

public class ExcelReader extends AbstractReader
{
    private String sheetName;
    private int sheetIndex;
    private int currentRowIndex;
    private int startingColumn;
    private boolean evaluateExpressions;
    private boolean useSheetColumnCount;
    private final ExcelDocument document;
    
    public ExcelReader(final ExcelDocument document) {
        super();
        this.sheetIndex = 0;
        this.currentRowIndex = 0;
        this.startingColumn = 0;
        this.evaluateExpressions = true;
        if (document == null) {
            throw new NullPointerException("document is null");
        }
        this.document = document;
    }
    
    @Override
	public void open() {
        this.document.getProvider().setEvaluateExpressions(this.evaluateExpressions);
        if (this.sheetName != null) {
            this.document.getProvider().startReading(this.sheetName);
        }
        else {
            this.document.getProvider().startReading(this.sheetIndex);
        }
        if (this.lastRow < 0) {
            this.lastRow = this.document.getProvider().getLastRowIndex();
        }
        this.currentRowIndex = this.getStartingRow();
        super.open();
    }
    
    @Override
	public void close() throws DataException {
        this.document.getProvider().endReading();
        super.close();
    }
    
    public String getSheetName() {
        return this.sheetName;
    }
    
    public ExcelReader setSheetName(final String sheetName) {
        this.sheetName = sheetName;
        return this;
    }
    
    public int getSheetIndex() {
        return this.sheetIndex;
    }
   
    public ExcelReader setSheetIndex(final int sheetIndex) {
        this.sheetIndex = sheetIndex;
        return this;
    }
    
    public int getStartingColumn() {
        return this.startingColumn;
    }
    
    public ExcelReader setStartingColumn(final int startingColumn) {
        this.startingColumn = startingColumn;
        return this;
    }
    
    public boolean isEvaluateExpressions() {
        return this.evaluateExpressions;
    }
    
    public ExcelReader setEvaluateExpressions(final boolean evaluateExpressions) {
        this.evaluateExpressions = evaluateExpressions;
        return this;
    }
    
    public boolean isUseSheetColumnCount() {
        return this.useSheetColumnCount;
    }
    
    public ExcelReader setUseSheetColumnCount(final boolean useSheetColumnCount) {
        this.useSheetColumnCount = useSheetColumnCount;
        return this;
    }
    
    @Override
	protected boolean fillRecord(final Record record) throws Throwable {
        int fieldIndex = 0;
        try {
            int cellCount = 0;
            while (this.currentRowIndex <= this.lastRow && (cellCount = this.document.getProvider().getCellCount(this.currentRowIndex)) == 0) {
                ++this.currentRowIndex;
            }
            if (this.currentRowIndex > this.lastRow) {
                return false;
            }
            if (this.useSheetColumnCount) {
                this.currentRecord.getField(this.document.getProvider().getLastColumnIndex(), true);
            }
            for (fieldIndex = this.startingColumn; fieldIndex < cellCount; ++fieldIndex) {
                final Field field = this.currentRecord.getField(fieldIndex, true);
                this.document.getProvider().readField(this.currentRowIndex, fieldIndex, field);
            }
            ++this.currentRowIndex;
        }
        catch (Throwable e) {
            final DataException exception = this.exception(e).set("rowIndex", this.currentRowIndex).set("fieldIndex", fieldIndex);
            if (fieldIndex < this.currentRecord.getFieldCount()) {
                exception.setFieldName(this.currentRecord.getField(fieldIndex).getName());
            }
            throw exception;
        }
        return true;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("ExcelReader.startingRow", this.currentRowIndex);
        exception.set("ExcelReader.sheetName", this.sheetName);
        exception.set("ExcelReader.sheetNumber", this.sheetIndex);
        exception.set("ExcelReader.workingRow", this.currentRowIndex);
        exception.set("ExcelReader.evaluateExpressions", this.evaluateExpressions);
        exception.set("ExcelReader.useSheetColumnCount", this.useSheetColumnCount);
        exception.set("ExcelReader.startingColumn", this.startingColumn);
        exception.setRecord(this.currentRecord);
        return super.addExceptionProperties(exception);
    }
}
