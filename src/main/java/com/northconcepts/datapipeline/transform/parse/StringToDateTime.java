package com.northconcepts.datapipeline.transform.parse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.transform.FieldTransformer;

class StringToDateTime extends FieldTransformer {
	final SimpleDateFormat format;

	public StringToDateTime(final String name, final String pattern) {
		super(name);
		this.format = new SimpleDateFormat(pattern);
	}

	protected Date parseField(final Field field, final FieldType type) throws Throwable {
		final String source = field.getValueAsString();
		if (source == null || source.length() == 0) {
			field.setNull(type);
			return null;
		}
		return this.format.parse(source);
	}

	@Override
	public String toString() {
		return "convert " + this.getName() + " from String to datetime using pattern " + this.format.toPattern();
	}

	@Override
	protected void transformField(final Field field) throws Throwable {
		final Date datetime = this.parseField(field, FieldType.DATETIME);
		if (datetime != null) {
			field.setValue(new Timestamp(datetime.getTime()));
		}
	}
}
