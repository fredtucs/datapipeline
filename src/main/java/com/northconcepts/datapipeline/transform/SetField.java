package com.northconcepts.datapipeline.transform;

import java.sql.Time;
import java.util.Date;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;

public class SetField extends Transformer {
	private final String name;
	private final Object value;
	private final FieldType fieldType;
	private final boolean overwrite;

	private SetField(final String name, final Object value, final FieldType fieldType, final boolean overwrite) {
		super();
		this.name = name;
		this.value = value;
		this.fieldType = fieldType;
		this.overwrite = overwrite;
	}

	private SetField(final String name, final Object value, final FieldType fieldType) {
		this(name, value, fieldType, true);
	}

	public SetField(final String name, final String value, final boolean overwrite) {
		this(name, value, FieldType.STRING, overwrite);
	}

	public SetField(final String name, final Date value, final boolean overwrite) {
		this(name, value, FieldType.DATETIME, overwrite);
	}

	public SetField(final String name, final java.sql.Date value, final boolean overwrite) {
		this(name, value, FieldType.DATE, overwrite);
	}

	public SetField(final String name, final Time value, final boolean overwrite) {
		this(name, value, FieldType.TIME, overwrite);
	}

	public SetField(final String name, final Integer value, final boolean overwrite) {
		this(name, value, FieldType.INT, overwrite);
	}

	public SetField(final String name, final Long value, final boolean overwrite) {
		this(name, value, FieldType.LONG, overwrite);
	}

	public SetField(final String name, final Short value, final boolean overwrite) {
		this(name, value, FieldType.SHORT, overwrite);
	}

	public SetField(final String name, final Byte value, final boolean overwrite) {
		this(name, value, FieldType.BYTE, overwrite);
	}

	public SetField(final String name, final Boolean value, final boolean overwrite) {
		this(name, value, FieldType.BOOLEAN, overwrite);
	}

	public SetField(final String name, final Character value, final boolean overwrite) {
		this(name, value, FieldType.CHAR, overwrite);
	}

	public SetField(final String name, final Double value, final boolean overwrite) {
		this(name, value, FieldType.DOUBLE, overwrite);
	}

	public SetField(final String name, final Float value, final boolean overwrite) {
		this(name, value, FieldType.FLOAT, overwrite);
	}

	public SetField(final String name, final byte[] value, final boolean overwrite) {
		this(name, value, FieldType.BLOB, overwrite);
	}

	public SetField(final String name, final Object value, final boolean overwrite) {
		this(name, value, FieldType.UNDEFINED, overwrite);
	}

	public SetField(final String name, final String value) {
		this(name, value, FieldType.STRING);
	}

	public SetField(final String name, final Date value) {
		this(name, value, FieldType.DATETIME);
	}

	public SetField(final String name, final java.sql.Date value) {
		this(name, value, FieldType.DATE);
	}

	public SetField(final String name, final Time value) {
		this(name, value, FieldType.TIME);
	}

	public SetField(final String name, final Integer value) {
		this(name, value, FieldType.INT);
	}

	public SetField(final String name, final Long value) {
		this(name, value, FieldType.LONG);
	}

	public SetField(final String name, final Short value) {
		this(name, value, FieldType.SHORT);
	}

	public SetField(final String name, final Byte value) {
		this(name, value, FieldType.BYTE);
	}

	public SetField(final String name, final Boolean value) {
		this(name, value, FieldType.BOOLEAN);
	}

	public SetField(final String name, final Character value) {
		this(name, value, FieldType.CHAR);
	}

	public SetField(final String name, final Double value) {
		this(name, value, FieldType.DOUBLE);
	}

	public SetField(final String name, final Float value) {
		this(name, value, FieldType.FLOAT);
	}

	public SetField(final String name, final byte[] value) {
		this(name, value, FieldType.BLOB);
	}

	public SetField(final String name, final Object value) {
		this(name, value, FieldType.UNDEFINED);
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public FieldType getFieldType() {
		return this.fieldType;
	}

	public boolean getOverwrite() {
		return this.overwrite;
	}

	@Override
	public boolean transform(final Record record) throws Throwable {
		if (!this.overwrite && record.containsField(this.name)) {
			return true;
		}
		final Field field = record.getField(this.name, true);
		if (this.value == null && this.fieldType != null) {
			field.setNull(this.fieldType);
		} else {
			field.setValue(this.value);
		}
		return true;
	}

	@Override
	public String toString() {
		return "set field \"" + this.name + "\" to " + this.value + ":" + this.fieldType + (this.overwrite ? " allow overwrite" : " no overwrite");
	}
}
