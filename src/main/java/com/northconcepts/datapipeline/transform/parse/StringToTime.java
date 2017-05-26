package com.northconcepts.datapipeline.transform.parse;

import java.sql.Time;
import java.util.Date;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;

class StringToTime extends StringToDateTime {
	public StringToTime(final String name, final String pattern) {
		super(name, pattern);
	}

	@Override
	protected void transformField(final Field field) throws Throwable {
		final Date datetime = this.parseField(field, FieldType.TIME);
		if (datetime != null) {
			field.setValue(new Time(datetime.getTime()));
		}
	}

	@Override
	public String toString() {
		return "convert " + this.getName() + " from String to time using pattern " + this.format.toPattern();
	}
}
