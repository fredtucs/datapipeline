package com.northconcepts.datapipeline.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;

class PoiXssfProvider extends Provider
{
    private static final int JAN_1_1970 = 25569;
    private Workbook workbook;
    private CreationHelper helper;
    private DataFormat format;
    private Sheet sheet;
    private Row row;
    private FormulaEvaluator formulaEvaluator;
    private int lastColumnIndex;
    private final Map<FieldType, CellStyle> styleCache;
    
    public PoiXssfProvider() {
        super();
        this.styleCache = new HashMap<FieldType, CellStyle>();
    }
    
    private void init() {
        this.helper = this.workbook.getCreationHelper();
        this.format = this.helper.createDataFormat();
        this.initStyles();
    }
    
    @Override
	public void newWorkbook() {
        this.workbook = new XSSFWorkbook();
        this.init();
    }
    
    @Override
	public void openWorkbook(final InputStream inputStream) {
        try {
            this.workbook = WorkbookFactory.create(inputStream);
            this.init();
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    @Override
	public void saveWorkbook(final OutputStream outputStream) {
        try {
            this.workbook.write(outputStream);
            outputStream.flush();
        }
        catch (IOException e) {
            throw DataException.wrap(e);
        }
    }
    
    @Override
	public void startReading(final String sheetName) {
        this.sheet = this.workbook.getSheet(sheetName);
        this.startReadingImpl();
    }
    
    @Override
	public void startReading(final int sheetIndex) {
        this.sheet = this.workbook.getSheetAt(sheetIndex);
        this.startReadingImpl();
    }
    
    private void startReadingImpl() {
        if (this.sheet == null) {
            throw new DataException("Sheet cannot be found");
        }
    }
    
    private CellValue evaluate(final Cell cell) {
        if (this.formulaEvaluator == null) {
            this.formulaEvaluator = this.helper.createFormulaEvaluator();
        }
        final CellValue value = this.formulaEvaluator.evaluate(cell);
        return value;
    }
    
    @Override
	public void readField(final int rowIndex, final int fieldIndex, final Field field) {
        if (this.row == null || this.row.getRowNum() != rowIndex) {
            this.row = this.sheet.getRow(rowIndex);
        }
        final Cell cell = this.row.getCell(fieldIndex);
        if (cell != null) {
            this.setFieldValue(cell, field);
        }
    }
    
    @Override
	public void endReading() {
        this.sheet = null;
        this.row = null;
        this.formulaEvaluator = null;
    }
    
    @Override
	public void startWriting(String sheetName, final int sheetIndex) {
        if (sheetName != null) {
            this.sheet = this.workbook.getSheet(sheetName);
            if (this.sheet != null) {
                return;
            }
            this.sheet = this.workbook.createSheet(sheetName);
            if (sheetIndex >= 0) {
                this.workbook.setSheetOrder(sheetName, sheetIndex);
            }
        }
        else if (sheetIndex >= 0 && sheetIndex < this.workbook.getNumberOfSheets()) {
            this.sheet = this.workbook.getSheetAt(sheetIndex);
        }
        else {
            sheetName = "Sheet" + (this.workbook.getNumberOfSheets() + 1);
            this.sheet = this.workbook.createSheet(sheetName);
            if (sheetIndex >= 0) {
                this.workbook.setSheetOrder(sheetName, sheetIndex);
            }
        }
    }
    
    @Override
	public void writeField(final int rowIndex, final int columnIndex, final int recordndex, final int fieldIndex, final Field field) {
        this.createCell(rowIndex, columnIndex, recordndex, fieldIndex, field);
    }
    
    @Override
	public void setColumnWidths(final int firstColumnIndex, final int[] columnCharacterCount) {
        for (int i = 0; i < columnCharacterCount.length; ++i) {
            final int chracterMargin = 7;
            final int width = (columnCharacterCount[i] + chracterMargin) * 256;
            this.sheet.setColumnWidth((short)(firstColumnIndex + i), (short)width);
        }
    }
    
    @Override
	public void endWriting() {
        this.endReading();
    }
    
    @Override
	public int getCellCount(final int rowIndex) {
        final Row row = this.sheet.getRow(rowIndex);
        return (row == null) ? 0 : row.getLastCellNum();
    }
    
    @Override
	public int getLastRowIndex() {
        return this.sheet.getLastRowNum();
    }
    
    @Override
	public int getLastColumnIndex() {
        if (this.lastColumnIndex <= 0) {
            for (int i = 0; i <= this.getLastRowIndex(); ++i) {
                final int index = this.getCellCount(i) - 1;
                if (index > this.lastColumnIndex) {
                    this.lastColumnIndex = index;
                }
            }
        }
        return this.lastColumnIndex;
    }
    
    private void setFieldValue(final Cell cell, final Field field) {
        if (cell.getCellType() == 4) {
            field.setValue(cell.getBooleanCellValue());
        }
        else if (cell.getCellType() == 1) {
            field.setValue(cell.getRichStringCellValue().getString());
        }
        else if (cell.getCellType() == 5) {
            field.setValue(cell.getErrorCellValue());
        }
        else if (cell.getCellType() == 3) {
            field.setValue((Object)null);
        }
        else if (cell.getCellType() == 0) {
            final double value = cell.getNumericCellValue();
            this.setNumericField(cell, field, value);
        }
        else if (cell.getCellType() == 2) {
            if (this.isEvaluateExpressions()) {
                try {
                    final CellValue cellValue = this.evaluate(cell);
                    if (cellValue.getCellType() == 4) {
                        field.setValue(cellValue.getBooleanValue());
                    }
                    else if (cellValue.getCellType() == 1) {
                        field.setValue(cellValue.getStringValue());
                    }
                    else if (cellValue.getCellType() == 5) {
                        field.setValue(cellValue.getErrorValue());
                    }
                    else if (cellValue.getCellType() == 3) {
                        field.setValue((Object)null);
                    }
                    else if (cellValue.getCellType() == 0) {
                        final double value2 = cellValue.getNumberValue();
                        this.setNumericField(cell, field, value2);
                    }
                    return;
                }
                catch (Throwable e) {
                    final DataException exception = DataException.wrap(e);
                    if ("NO IDEA SHARED FORMULA EXP PTG".equalsIgnoreCase(cell.getCellFormula())) {
                        exception.set("Format", "Format the cells to Numeric type");
                    }
                    throw exception;
                }
            }
            field.setValue(cell.getCellFormula());
        }
        else {
            field.setValue(cell.getRichStringCellValue().getString());
        }
    }
    
    private void setNumericField(final Cell cell, final Field field, final double value) {
        if (DateUtil.isCellDateFormatted(cell)) {
            final java.util.Date date = DateUtil.getJavaDate(value);
            final double dayPart = Math.floor(value);
            if (value == dayPart) {
                field.setValue(new Date(date.getTime()));
            }
            else if (25569.0 == dayPart) {
                field.setValue(new Time(date.getTime()));
            }
            else {
                field.setValue(date);
            }
        }
        else {
            this.setNumericField(field, value);
        }
    }
    
    private Field setNumericField(final Field field, final double doubleValue) {
        final int integerValue = (int)doubleValue;
        if (integerValue == doubleValue) {
            return field.setValue(integerValue);
        }
        final long longValue = (long)doubleValue;
        if (longValue == doubleValue) {
            return field.setValue(longValue);
        }
        return field.setValue(doubleValue);
    }
    
    private void createCell(final int rowIndex, final int columnIndex, final int recordIndex, final int fieldIndex, final Field field) {
        if (this.row == null || this.row.getRowNum() != rowIndex) {
            this.row = this.sheet.createRow(rowIndex);
        }
        Cell cell = null;
        RichTextString str = null;
        if (field.isNull()) {
            cell = this.row.createCell(columnIndex, 1);
            str = this.helper.createRichTextString(field.getValueAsString());
            cell.setCellValue(str);
            return;
        }
        switch (field.getType()) {
            case BOOLEAN: {
                cell = this.row.createCell((short)columnIndex, 4);
                cell.setCellValue(field.getValueAsBoolean());
                break;
            }
            case UNDEFINED:
            case STRING: {
                cell = this.row.createCell((short)columnIndex, 1);
                str = this.helper.createRichTextString(field.getValueAsString());
                cell.setCellValue(str);
                break;
            }
            case BYTE: {
                cell = this.row.createCell((short)columnIndex, 1);
                cell.setCellValue(field.getValueAsInteger());
                this.setCellStyle(field, cell);
                break;
            }
            case CHAR: {
                cell = this.row.createCell((short)columnIndex, 1);
                str = this.helper.createRichTextString("" + field.getValueAsChar());
                cell.setCellValue(str);
                break;
            }
            case DATE: {
                cell = this.row.createCell((short)columnIndex, 0);
                cell.setCellValue(field.getValueAsDate());
                this.setCellStyle(field, cell);
                break;
            }
            case DATETIME: {
                cell = this.row.createCell((short)columnIndex, 0);
                cell.setCellValue(field.getValueAsDatetime());
                this.setCellStyle(field, cell);
                break;
            }
            case TIME: {
                cell = this.row.createCell((short)columnIndex, 0);
                cell.setCellValue(field.getValueAsTime());
                this.setCellStyle(field, cell);
                break;
            }
            case INT: {
                cell = this.row.createCell((short)columnIndex, 0);
                cell.setCellValue(field.getValueAsInteger());
                this.setCellStyle(field, cell);
                break;
            }
            case LONG: {
                cell = this.row.createCell((short)columnIndex, 0);
                cell.setCellValue(field.getValueAsLong());
                this.setCellStyle(field, cell);
                break;
            }
            case DOUBLE: {
                cell = this.row.createCell((short)columnIndex, 0);
                cell.setCellValue(field.getValueAsDouble());
                this.setCellStyle(field, cell);
                break;
            }
            case FLOAT: {
                cell = this.row.createCell((short)columnIndex, 0);
                cell.setCellValue(field.getValueAsDouble());
                this.setCellStyle(field, cell);
                break;
            }
            case SHORT: {
                cell = this.row.createCell((short)columnIndex, 0);
                cell.setCellValue(field.getValueAsLong());
                this.setCellStyle(field, cell);
                break;
            }
            case BLOB: {
                cell = this.row.createCell((short)columnIndex, 1);
                cell.setCellValue(field.getValueAsByte());
                this.setCellStyle(field, cell);
                break;
            }
            default: {
                cell = this.row.createCell((short)columnIndex, 1);
                str = this.helper.createRichTextString(field.getValueAsString());
                cell.setCellValue(str);
                break;
            }
        }
    }
    
    private void setCellStyle(final Field field, final Cell cell) {
        final CellStyle cellStyle = this.styleCache.get(field.getType());
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }
    }
    
    private void initStyles() {
        this.addStyle(FieldType.BYTE, "0");
        this.addStyle(FieldType.DATE, "m/d/yy");
        this.addStyle(FieldType.DATETIME, "m/d/yy h:mm");
        this.addStyle(FieldType.TIME, "h:mm:ss AM/PM");
        this.addStyle(FieldType.INT, "0");
        this.addStyle(FieldType.LONG, "0");
        this.addStyle(FieldType.DOUBLE, "0.00");
        this.addStyle(FieldType.FLOAT, "0.00");
        this.addStyle(FieldType.SHORT, "0");
        this.addStyle(FieldType.BLOB, "0");
    }
    
    private void addStyle(final FieldType fieldType, final String pattern) {
        final CellStyle cellStyle = this.workbook.createCellStyle();
        cellStyle.setDataFormat(this.format.getFormat(pattern));
        this.styleCache.put(fieldType, cellStyle);
    }
}
