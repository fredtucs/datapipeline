package com.northconcepts.datapipeline.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.ErrorCell;
import jxl.FormulaCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.biff.formula.FormulaException;
import jxl.format.CellFormat;
import jxl.write.Boolean;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;

class JxlProvider extends Provider {
	private Workbook workbook;
	private WritableWorkbook writableWorkbook;
	private ByteArrayOutputStream bytes;
	private Sheet sheet;
	private WritableSheet writableSheet;
	private static final DateFormat ddMMMyyyyhhmmss;
	private static final DateFormat ddMMMyyyy;
	private static final DateFormat hhmmss;

	public JxlProvider() {
		super();
		this.bytes = new ByteArrayOutputStream(65536);
	}

	@Override
	public void newWorkbook() {
		try {
			this.workbook = null;
			this.writableWorkbook = Workbook.createWorkbook(this.bytes);
			this.bytes.reset();
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}
	}

	@Override
	public void openWorkbook(final InputStream inputStream) {
		try {
			this.workbook = Workbook.getWorkbook(inputStream);
			this.writableWorkbook = null;
			this.bytes.reset();
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}
	}

	@Override
	public void saveWorkbook(final OutputStream outputStream) {
		try {
			if (this.workbook != null) {
				this.bytes.reset();
				this.writableWorkbook = Workbook.createWorkbook(this.bytes, this.workbook);
				this.workbook.close();
				this.workbook = null;
				this.sheet = null;
			}
			if (this.writableWorkbook != null) {
				this.writableWorkbook.write();
				this.writableWorkbook.close();
				this.writableWorkbook = null;
				this.writableSheet = null;
			}
			this.bytes.writeTo(outputStream);
			outputStream.flush();
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}
	}

	private void startReadingImpl(final String sheetName, final int sheetIndex) {
		try {
			if (this.writableWorkbook != null) {
				this.writableWorkbook.close();
				final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.bytes.toByteArray());
				this.openWorkbook(byteArrayInputStream);
			}
			if (this.workbook == null) {
				throw new DataException("workbook is null, call openWorkbook(...) first");
			}
			if (sheetName != null) {
				this.sheet = this.workbook.getSheet(sheetName);
			} else {
				this.sheet = this.workbook.getSheet(sheetIndex);
			}
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}
	}

	@Override
	public void startReading(final String sheetName) {
		this.startReadingImpl(sheetName, 0);
	}

	@Override
	public void startReading(final int sheetIndex) {
		this.startReadingImpl(null, sheetIndex);
	}

	@Override
	public void readField(final int rowIndex, final int fieldIndex, final Field field) {
		final Cell cell = this.sheet.getCell(fieldIndex, rowIndex);
		this.setFieldValue(cell, field);
	}

	@Override
	public void endReading() {
		this.sheet = null;
	}

	@Override
	public void startWriting(final String sheetName, int sheetIndex) {
		try {
			if (this.workbook != null) {
				this.writableWorkbook = Workbook.createWorkbook(this.bytes, this.workbook);
				this.workbook.close();
				this.workbook = null;
				this.sheet = null;
				this.bytes.reset();
			}
			if (this.writableWorkbook == null) {
				throw new DataException("writableWorkbook is null, call newWorkbook() first");
			}
			if (sheetName != null) {
				this.writableSheet = this.writableWorkbook.getSheet(sheetName);
				if (this.writableSheet != null) {
					return;
				}
				if (sheetIndex < 0) {
					sheetIndex = Integer.MAX_VALUE;
				}
				this.writableSheet = this.writableWorkbook.createSheet(sheetName, sheetIndex);
			} else if (sheetIndex >= 0 && sheetIndex < this.writableWorkbook.getSheetNames().length) {
				this.writableSheet = this.writableWorkbook.getSheet(sheetIndex);
			} else {
				if (sheetIndex < 0) {
					sheetIndex = Integer.MAX_VALUE;
				}
				this.writableSheet = this.writableWorkbook.createSheet("Sheet" + (this.writableWorkbook.getSheetNames().length + 1), sheetIndex);
			}
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}
	}

	@Override
	public void writeField(final int rowIndex, final int columnIndex, final int recordndex, final int fieldIndex, final Field field) {
		try {
			final WritableCell cell = this.createCell(rowIndex, columnIndex, recordndex, fieldIndex, field);
			if (cell != null) {
				this.writableSheet.addCell(cell);
			}
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}
	}

	@Override
	public void setColumnWidths(final int firstColumnIndex, final int[] columnCharacterCount) {
		for (int i = 0; i < columnCharacterCount.length; ++i) {
			final int chracterMargin = 7;
			int width = columnCharacterCount[i] + chracterMargin;
			if (width == 0) {
				width = 1;
			}
			this.writableSheet.setColumnView(firstColumnIndex + i, width);
		}
	}

	@Override
	public void endWriting() {
		this.writableSheet = null;
	}

	@Override
	public int getCellCount(final int rowIndex) {
		if (this.sheet != null) {
			return this.sheet.getRow(rowIndex).length;
		}
		if (this.writableSheet != null) {
			return this.writableSheet.getRow(rowIndex).length;
		}
		return 0;
	}

	@Override
	public int getLastRowIndex() {
		if (this.sheet != null) {
			return this.sheet.getRows() - 1;
		}
		if (this.writableSheet != null) {
			return this.writableSheet.getRows() - 1;
		}
		return 0;
	}

	@Override
	public int getLastColumnIndex() {
		if (this.sheet != null) {
			return this.sheet.getColumns() - 1;
		}
		if (this.writableSheet != null) {
			return this.writableSheet.getColumns() - 1;
		}
		return 0;
	}

	private void setFieldValue(final Cell cell, final Field field) {
		try {
			if (!this.isEvaluateExpressions()
					&& (cell.getType() == CellType.BOOLEAN_FORMULA || cell.getType() == CellType.DATE_FORMULA
							|| cell.getType() == CellType.FORMULA_ERROR || cell.getType() == CellType.NUMBER_FORMULA || cell.getType() == CellType.STRING_FORMULA)) {
				field.setValue(((FormulaCell) cell).getFormula());
			} else if (cell.getType() == CellType.BOOLEAN || cell.getType() == CellType.BOOLEAN_FORMULA) {
				field.setValue(((BooleanCell) cell).getValue());
			} else if (cell.getType() == CellType.LABEL || cell.getType() == CellType.STRING_FORMULA) {
				field.setValue(((LabelCell) cell).getString());
			} else if (cell.getType() == CellType.EMPTY) {
				field.setValue((Object) null);
			} else if (cell.getType() == CellType.NUMBER || cell.getType() == CellType.NUMBER_FORMULA) {
				final NumberCell numberCell = (NumberCell) cell;
				this.setNumericFieldValue(numberCell, field);
			} else if (cell.getType() == CellType.DATE || cell.getType() == CellType.DATE_FORMULA) {
				final DateCell dateCell = (DateCell) cell;
				this.setDateFieldValue(dateCell, field);
			} else if (cell.getType() == CellType.ERROR || cell.getType() == CellType.FORMULA_ERROR) {
				field.setValue(((ErrorCell) cell).getErrorCode());
			}
		} catch (FormulaException e) {
			throw DataException.wrap(e).set("Formula error", e.getMessage());
		}
	}

	private void setNumericFieldValue(final NumberCell numberCell, final Field field) {
		final NumberFormat numberFormat = numberCell.getNumberFormat();
		if (numberFormat instanceof DecimalFormat) {
			final DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
			final String pattern = decimalFormat.toPattern();
			if (pattern != null && pattern.lastIndexOf(46) < 0) {
				field.setValue((int) numberCell.getValue());
			} else {
				field.setValue(numberCell.getValue());
			}
		} else {
			field.setValue(numberCell.getValue());
		}
	}

	private void setDateFieldValue(final DateCell dateCell, final Field field) {
		final java.text.DateFormat dateFormat = dateCell.getDateFormat();
		final Date date = dateCell.getDate();
		final TimeZone timeZone = TimeZone.getDefault();
		final int timeZoneOffset = timeZone.getOffset(date.getTime());
		date.setTime(date.getTime() - timeZoneOffset);
		if (dateFormat instanceof SimpleDateFormat) {
			final SimpleDateFormat simpleDateFormat = (SimpleDateFormat) dateFormat;
			final String pattern = simpleDateFormat.toPattern();
			if (pattern == null) {
				field.setValue(date);
			} else if ((pattern.indexOf("H:mm") >= 0 || pattern.indexOf("h:mm") >= 0)
					&& (pattern.indexOf("yy") > 0 || pattern.indexOf("d") > 0 || pattern.indexOf("M") > 0)) {
				field.setValue(date);
			} else if (pattern.indexOf("H:mm") >= 0 || pattern.indexOf("h:mm") >= 0) {
				field.setValue(new Time(date.getTime()));
			} else {
				field.setValue(date);
			}
		} else {
			field.setValue(date);
		}
	}

	public WritableCell createCell(final int rowIndex, final int columnIndex, final int recordIndex, final int fieldIndex, final Field field) {
		if (field.isNull()) {
			return null;
		}
		switch (field.getType()) {
		case BOOLEAN: {
			return new Boolean(columnIndex, rowIndex, field.getValueAsBoolean());
		}
		case UNDEFINED:
		case STRING: {
			return new Label(columnIndex, rowIndex, field.getValueAsString());
		}
		case BYTE: {
			return new Number(columnIndex, rowIndex, field.getValueAsByte());
		}
		case CHAR: {
			return new Label(columnIndex, rowIndex, field.getValueAsString());
		}
		case DATE: {
			return new DateTime(columnIndex, rowIndex, field.getValueAsDate(), this.getDateFormat());
		}
		case DATETIME: {
			return new DateTime(columnIndex, rowIndex, field.getValueAsDatetime(), this.getDateTimeFormat());
		}
		case TIME: {
			return new DateTime(columnIndex, rowIndex, field.getValueAsTime(), this.getTimeFormat());
		}
		case INT: {
			return new Number(columnIndex, rowIndex, field.getValueAsInteger());
		}
		case LONG: {
			return new Number(columnIndex, rowIndex, field.getValueAsLong());
		}
		case DOUBLE: {
			return new Number(columnIndex, rowIndex, field.getValueAsDouble());
		}
		case FLOAT: {
			return new Number(columnIndex, rowIndex, field.getValueAsFloat());
		}
		case SHORT: {
			return new Number(columnIndex, rowIndex, field.getValueAsShort());
		}
		case BLOB: {
			return null;
		}
		default: {
			return null;
		}
		}
	}

	private WritableCellFormat getDateTimeFormat() {
		final WritableCellFormat dateFormat = new WritableCellFormat(JxlProvider.ddMMMyyyyhhmmss);
		return dateFormat;
	}

	private WritableCellFormat getDateFormat() {
		final WritableCellFormat dateFormat = new WritableCellFormat(JxlProvider.ddMMMyyyy);
		return dateFormat;
	}

	private WritableCellFormat getTimeFormat() {
		final WritableCellFormat dateFormat = new WritableCellFormat(JxlProvider.hhmmss);
		return dateFormat;
	}

	static {
		ddMMMyyyyhhmmss = new DateFormat("dd MMM yyyy hh:mm:ss");
		ddMMMyyyy = new DateFormat("dd MMM yyyy");
		hhmmss = new DateFormat("hh:mm:ss");
	}
}
