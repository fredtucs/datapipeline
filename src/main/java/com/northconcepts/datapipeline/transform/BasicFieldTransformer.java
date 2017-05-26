package com.northconcepts.datapipeline.transform;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.internal.lang.Interval;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.transform.format.Rounder;

public class BasicFieldTransformer extends FieldTransformer {
	public abstract static class NullableStringOperation extends Operation {
		public NullableStringOperation(final String name) {
			super(name);
		}

		@Override
		protected final void apply(final Field field) throws Throwable {
			final String value = field.getValueAsString();
			field.setValue(this.applyImpl(value));
		}

		protected abstract String applyImpl(final String p0);
	}
	public abstract static class Operation {
		private final String name;

		public Operation(final String name) {
			super();
			this.name = name;
		}

		public DataException addExceptionProperties(final DataException exception) {
			return exception;
		}

		protected abstract void apply(final Field p0) throws Throwable;

		@Override
		public String toString() {
			return this.name;
		}
	}

	public abstract static class StringOperation extends Operation {
		public StringOperation(final String name) {
			super(name);
		}

		@Override
		protected final void apply(final Field field) throws Throwable {
			final String value = field.getValueAsString();
			if (value == null) {
				field.setNull(FieldType.STRING);
			} else {
				field.setValue(this.applyImpl(value));
			}
		}

		protected abstract String applyImpl(final String p0);
	}

	private ArrayList<Operation> operations;

	private Operation currentOperation;

	public BasicFieldTransformer(final String name) {
		super(name);
		this.operations = new ArrayList<Operation>();
	}

	public BasicFieldTransformer add(final Operation... operation) {
		for (int i = 0; i < operation.length; ++i) {
			this.operations.add(operation[i]);
		}
		return this;
	}

	@Override
	public DataException addExceptionProperties(final DataException exception) {
		if (this.currentOperation != null) {
			exception.set("BasicFieldTransformer.operation", this.currentOperation);
			this.currentOperation.addExceptionProperties(exception);
		}
		for (int i = 0; i < this.operations.size(); ++i) {
			exception.set("BasicFieldTransformer.operations[" + i + "]", this.operations.get(i));
		}
		return exception;
	}

	public BasicFieldTransformer append(final String suffix) {
		return this.add(new NullableStringOperation("append") {
			@Override
			protected String applyImpl(final String value) {
				return value + suffix;
			}
		});
	}

	public BasicFieldTransformer dateTimeToString(final DateFormat format) {
		return this.dateTimeToString("dateTimeToString", format);
	}

	public BasicFieldTransformer dateTimeToString(final String pattern) {
		return this.dateTimeToString(new SimpleDateFormat(pattern));
	}

	private BasicFieldTransformer dateTimeToString(final String name, final DateFormat format) {
		return this.add(new Operation(name) {
			@Override
			public DataException addExceptionProperties(final DataException exception) {
				if (format instanceof SimpleDateFormat) {
					exception.set("DateFormatter.pattern", ((SimpleDateFormat) format).toPattern());
				}
				return exception;
			}

			@Override
			protected void apply(final Field field) throws Throwable {
				final Date date = field.getValueAsDatetime();
				if (date == null) {
					field.setNull(FieldType.STRING);
				} else {
					field.setValue(format.format(date));
				}
			}
		});
	}

	public BasicFieldTransformer dateToString(final DateFormat format) {
		return this.dateTimeToString("dateToString", format);
	}

	public BasicFieldTransformer dateToString(final String pattern) {
		return this.dateToString(new SimpleDateFormat(pattern));
	}

	public BasicFieldTransformer daysToDate() {
		return this.numberToDate("daysToDate", 86400000L);
	}

	public BasicFieldTransformer delete(final int start, final int end) {
		return this.add(new StringOperation("delete") {
			@Override
			protected String applyImpl(final String value) {
				return new StringBuilder(value).delete(start, end).toString();
			}
		});
	}

	public BasicFieldTransformer hoursToDate() {
		return this.numberToDate("hoursToDate", 3600000L);
	}

	public BasicFieldTransformer insert(final int index, final String text) {
		return this.add(new StringOperation("insert") {
			@Override
			protected String applyImpl(final String value) {
				if (index >= value.length()) {
					return value + text;
				}
				return new StringBuilder(value).insert(index, text).toString();
			}
		});
	}

	public BasicFieldTransformer left(final int length) {
		return this.add(new NullableStringOperation("left") {
			@Override
			protected String applyImpl(final String value) {
				return Util.left(value, length);
			}
		});
	}

	public BasicFieldTransformer lowerCase() {
		return this.add(new StringOperation("lowerCase") {
			@Override
			protected String applyImpl(final String value) {
				return value.toLowerCase();
			}
		});
	}

	public BasicFieldTransformer lowerCaseFirstChar() {
		return this.add(new StringOperation("lowerCaseFirstChar") {
			@Override
			protected String applyImpl(final String value) {
				if (value.length() == 0) {
					return value;
				}
				return Character.toLowerCase(value.charAt(0)) + value.substring(1);
			}
		});
	}

	public BasicFieldTransformer minutesToDate() {
		return this.numberToDate("minutesToDate", 60000L);
	}

	public BasicFieldTransformer nullToValue(final boolean value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final byte value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final byte[] value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final char value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final Date value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final double value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final float value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final int value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final java.sql.Date value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final long value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final short value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final String value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer nullToValue(final Time value) {
		return this.add(new Operation("nullToValue") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer numberToBoolean() {
		return this.add(new Operation("numberToBoolean") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.BOOLEAN);
				} else {
					field.setValue(field.getValueAsInteger() != 0);
				}
			}
		});
	}

	public BasicFieldTransformer numberToDate(final long multiplier) {
		return this.numberToDate("numberToDate", multiplier);
	}

	protected BasicFieldTransformer numberToDate(final String operationName, final long multiplier) {
		return this.add(new Operation(operationName) {
			@Override
			public DataException addExceptionProperties(final DataException exception) {
				exception.set("multiplier", multiplier);
				return exception;
			}

			@Override
			protected void apply(final Field field) throws Throwable {
				if (field.isNull()) {
					field.setNull(FieldType.DATE);
				} else if (field.getValue() instanceof Number) {
					final Number n = (Number) field.getValue();
					long milliseconds = n.longValue() * multiplier;
					final Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(milliseconds);
					calendar.set(11, 0);
					calendar.set(12, 0);
					calendar.set(13, 0);
					calendar.set(14, 0);
					milliseconds = calendar.getTimeInMillis();
					final java.sql.Date value = new java.sql.Date(milliseconds);
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer numberToDays() {
		return this.numberToInterval("numberToDays", "Days");
	}

	public BasicFieldTransformer numberToDouble() {
		return this.add(new Operation("numberToDouble") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.DOUBLE);
				} else {
					field.setValue(field.getValueAsDouble());
				}
			}
		});
	}

	public BasicFieldTransformer numberToHours() {
		return this.numberToInterval("numberToHours", "Hours");
	}

	public BasicFieldTransformer numberToInt() {
		return this.add(new Operation("numberToInt") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.INT);
				} else {
					field.setValue(field.getValueAsInteger());
				}
			}
		});
	}

	public BasicFieldTransformer numberToInterval(final String unitName) {
		return this.numberToInterval("numberToInterval", unitName);
	}

	protected BasicFieldTransformer numberToInterval(final String operationName, final String unitName) {
		return this.add(new Operation(operationName) {
			@Override
			public DataException addExceptionProperties(final DataException exception) {
				exception.set("multiplier", unitName);
				return exception;
			}

			@Override
			protected void apply(final Field field) throws Throwable {
				if (field.isNull()) {
					field.setNull(FieldType.UNDEFINED);
				} else if (field.getValue() instanceof Number) {
					final Number n = (Number) field.getValue();
					final long quantity = n.longValue();
					final Interval value = Interval.parseInterval(quantity, unitName);
					field.setValue(value);
				}
			}
		});
	}

	public BasicFieldTransformer numberToLong() {
		return this.add(new Operation("numberToLong") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.LONG);
				} else {
					field.setValue(field.getValueAsLong());
				}
			}
		});
	}

	public BasicFieldTransformer numberToMillisecond() {
		return this.numberToInterval("numberToMilliseconds", "Milliseconds");
	}

	public BasicFieldTransformer numberToMinutes() {
		return this.numberToInterval("numberToMinutes", "Minutes");
	}

	public BasicFieldTransformer numberToMonths() {
		return this.numberToInterval("numberToMonths", "Months");
	}

	public BasicFieldTransformer numberToSeconds() {
		return this.numberToInterval("numberToSeconds", "Seconds");
	}

	public BasicFieldTransformer numberToString(final DecimalFormat format) {
		return this.add(new Operation("numberToString") {
			@Override
			public DataException addExceptionProperties(final DataException exception) {
				exception.set("NumberFormatter.pattern", format.toPattern());
				return exception;
			}

			@Override
			protected void apply(final Field field) throws Throwable {
				if (field.isNull()) {
					field.setNull(FieldType.STRING);
				} else {
					field.setValue(format.format(field.getValueAsDouble()));
				}
			}
		});
	}

	public BasicFieldTransformer numberToString(final String pattern) {
		return this.numberToString(new DecimalFormat(pattern));
	}

	public BasicFieldTransformer numberToYears() {
		return this.numberToInterval("numberToYears", "Years");
	}

	public BasicFieldTransformer padLeft(final int length, final char padChar) {
		return this.add(new NullableStringOperation("padLeft") {
			@Override
			protected String applyImpl(final String value) {
				return Util.padLeft(value, length, padChar);
			}
		});
	}

	public BasicFieldTransformer padRight(final int length, final char padChar) {
		return this.add(new NullableStringOperation("padRight") {
			@Override
			protected String applyImpl(final String value) {
				return Util.padRight(value, length, padChar);
			}
		});
	}

	public BasicFieldTransformer prepend(final String prefix) {
		return this.add(new NullableStringOperation("prepend") {
			@Override
			protected String applyImpl(final String value) {
				return prefix + value;
			}
		});
	}

	public BasicFieldTransformer replace(final int start, final int end, final String string) {
		return this.add(new StringOperation("replace") {
			@Override
			protected String applyImpl(final String value) {
				return new StringBuilder(value).replace(start, end, string).toString();
			}
		});
	}

	public BasicFieldTransformer replaceChar(final char oldChar, final char newChar) {
		return this.add(new StringOperation("replace") {
			@Override
			protected String applyImpl(final String value) {
				return value.replace(oldChar, newChar);
			}
		});
	}

	public BasicFieldTransformer replaceString(final CharSequence oldString, final CharSequence newString) {
		return this.add(new StringOperation("replace") {
			@Override
			protected String applyImpl(final String value) {
				return value.replace(oldString, newString);
			}
		});
	}

	public BasicFieldTransformer right(final int length) {
		return this.add(new NullableStringOperation("right") {
			@Override
			protected String applyImpl(final String value) {
				return Util.right(value, length);
			}
		});
	}

	public BasicFieldTransformer round(final Rounder rounding) {
		return this.add(new Operation("round") {
			@Override
			public DataException addExceptionProperties(final DataException exception) {
				exception.set("NumberFormatter.rounding", rounding);
				return exception;
			}

			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.DOUBLE);
				} else {
					field.setValue(rounding.apply(field.getValueAsDouble()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToBoolean() {
		return this.add(new Operation("stringToBoolean") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.BOOLEAN);
				} else {
					field.setValue((boolean) Boolean.valueOf(field.getValueAsString()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToByte() {
		return this.add(new Operation("stringToByte") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.BYTE);
				} else {
					field.setValue((byte) Byte.valueOf(field.getValueAsString()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToChar() {
		return this.add(new Operation("stringToChar") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.CHAR);
				} else {
					final String string = field.getValueAsString();
					field.setValue((string.length() == 0) ? '\0' : string.charAt(0));
				}
			}
		});
	}

	public BasicFieldTransformer stringToDate(final String pattern) {
		return this.add(new Operation("stringToDate") {
			private final SimpleDateFormat format = new SimpleDateFormat(pattern);

			@Override
			protected void apply(final Field field) throws Throwable {
				if (field.isNull()) {
					field.setNull(FieldType.DATE);
				} else {
					final Date datetime = this.format.parse(field.getValueAsString());
					field.setValue(new java.sql.Date(datetime.getTime()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToDateTime(final String pattern) {
		return this.add(new Operation("stringToDateTime") {
			private final SimpleDateFormat format = new SimpleDateFormat(pattern);

			@Override
			protected void apply(final Field field) throws Throwable {
				if (field.isNull()) {
					field.setNull(FieldType.DATETIME);
				} else {
					field.setValue(this.format.parse(field.getValueAsString()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToDouble() {
		return this.add(new Operation("stringToDouble") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.DOUBLE);
				} else {
					field.setValue((double) Double.valueOf(field.getValueAsString()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToDouble(final DecimalFormat format) {
		return this.add(new Operation("stringToDouble") {
			@Override
			public DataException addExceptionProperties(final DataException exception) {
				exception.set("NumberFormatter.pattern", format.toPattern());
				return exception;
			}

			@Override
			protected void apply(final Field field) throws Throwable {
				if (field.isNull()) {
					field.setNull(FieldType.DOUBLE);
				} else {
					field.setValue(format.parse(field.getValueAsString()).doubleValue());
				}
			}
		});
	}

	public BasicFieldTransformer stringToDouble(final String pattern) {
		return this.stringToDouble(new DecimalFormat(pattern));
	}

	public BasicFieldTransformer stringToFloat() {
		return this.add(new Operation("stringToFloat") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.FLOAT);
				} else {
					field.setValue((float) Float.valueOf(field.getValueAsString()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToInt() {
		return this.add(new Operation("stringToInt") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.INT);
				} else {
					field.setValue((int) Integer.valueOf(field.getValueAsString()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToLong() {
		return this.add(new Operation("stringToLong") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.LONG);
				} else {
					field.setValue((long) Long.valueOf(field.getValueAsString()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToShort() {
		return this.add(new Operation("stringToShort") {
			@Override
			protected void apply(final Field field) {
				if (field.isNull()) {
					field.setNull(FieldType.SHORT);
				} else {
					field.setValue((short) Short.valueOf(field.getValueAsString()));
				}
			}
		});
	}

	public BasicFieldTransformer stringToTime(final String pattern) {
		return this.add(new Operation("stringToTime") {
			private final SimpleDateFormat format = new SimpleDateFormat(pattern);

			@Override
			protected void apply(final Field field) throws Throwable {
				if (field.isNull()) {
					field.setNull(FieldType.TIME);
				} else {
					final Date datetime = this.format.parse(field.getValueAsString());
					field.setValue(new Time(datetime.getTime()));
				}
			}
		});
	}

	public BasicFieldTransformer substring(final int begin) {
		return this.add(new StringOperation("substring") {
			@Override
			protected String applyImpl(final String value) {
				if (begin > value.length()) {
					return "";
				}
				return value.substring(begin);
			}
		});
	}

	public BasicFieldTransformer substring(final int begin, final int end) {
		return this.add(new StringOperation("substring") {
			@Override
			protected String applyImpl(final String value) {
				if (begin > value.length() || end > value.length()) {
					return "";
				}
				return value.substring(begin, end);
			}
		});
	}

	public BasicFieldTransformer timeToString(final DateFormat format) {
		return this.dateTimeToString("timeToString", format);
	}

	public BasicFieldTransformer timeToString(final String pattern) {
		return this.timeToString(new SimpleDateFormat(pattern));
	}

	@Override
	protected void transformField(final Field field) throws Throwable {
		for (int i = 0; i < this.operations.size(); ++i) {
			(this.currentOperation = this.operations.get(i)).apply(field);
		}
		this.currentOperation = null;
	}

	public BasicFieldTransformer trim() {
		return this.add(new StringOperation("trim") {
			@Override
			protected String applyImpl(final String value) {
				return value.trim();
			}
		});
	}

	public BasicFieldTransformer trimLeft() {
		return this.add(new NullableStringOperation("trimLeft") {
			@Override
			protected String applyImpl(final String value) {
				return Util.trimLeft(value);
			}
		});
	}

	public BasicFieldTransformer trimLeft(final char padChar) {
		return this.add(new NullableStringOperation("trimLeft") {
			@Override
			protected String applyImpl(final String value) {
				return Util.trimLeft(value, padChar);
			}
		});
	}

	public BasicFieldTransformer trimRight() {
		return this.add(new NullableStringOperation("trimRight") {
			@Override
			protected String applyImpl(final String value) {
				return Util.trimRight(value);
			}
		});
	}

	public BasicFieldTransformer trimRight(final char padChar) {
		return this.add(new NullableStringOperation("trimRight") {
			@Override
			protected String applyImpl(final String value) {
				return Util.trimRight(value, padChar);
			}
		});
	}

	public BasicFieldTransformer upperCase() {
		return this.add(new StringOperation("upperCase") {
			@Override
			protected String applyImpl(final String value) {
				return value.toUpperCase();
			}
		});
	}

	public BasicFieldTransformer upperCaseFirstChar() {
		return this.add(new StringOperation("upperCaseFirstChar") {
			@Override
			protected String applyImpl(final String value) {
				if (value.length() == 0) {
					return value;
				}
				return Character.toUpperCase(value.charAt(0)) + value.substring(1);
			}
		});
	}

	public BasicFieldTransformer valueToString() {
		return this.add(new Operation("valueToString") {
			@Override
			protected void apply(final Field field) {
				field.setValue(field.getValueAsString());
			}
		});
	}
}
