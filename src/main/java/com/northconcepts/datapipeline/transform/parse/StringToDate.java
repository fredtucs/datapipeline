package com.northconcepts.datapipeline.transform.parse;

import java.sql.Date;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;

class StringToDate extends StringToDateTime {
	public StringToDate(final String name, final String pattern) {
		super(name, pattern);
	}

	@Override
	protected void transformField(final Field field) throws Throwable {
		final java.util.Date datetime = this.parseField(field, FieldType.DATE);
		if (datetime != null) {
			field.setValue(new Date(datetime.getTime()));
		}
	}

	@Override
	public String toString() {
		return "convert " + this.getName() + " from String to date using pattern " + this.format.toPattern();
	}
}
