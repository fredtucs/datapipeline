package com.northconcepts.datapipeline.transform.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.transform.FieldTransformer;

class DateFormatter extends FieldTransformer
{
    private DateFormat format;
    
    public DateFormatter(final String name) {
        super(name);
    }
    
    public DateFormatter(final String name, final DateFormat format) {
        super(name);
        this.format = format;
    }
    
    public DateFormatter(final String name, final String pattern) {
        super(name);
        this.format = new SimpleDateFormat(pattern);
    }
    
    @Override
	protected void transformField(final Field field) throws Throwable {
        final Date date = field.getValueAsDatetime();
        if (date != null && this.format != null) {
            field.setValue(this.format.format(date));
        }
        else {
            field.setNull(FieldType.STRING);
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.format instanceof SimpleDateFormat) {
            exception.set("DateFormatter.pattern", ((SimpleDateFormat)this.format).toPattern());
        }
        return exception;
    }
    
    public DateFormat getFormat() {
        return this.format;
    }
    
    public DateFormatter setFormat(final DateFormat format) {
        this.format = format;
        return this;
    }
    
    public DateFormatter setFormat(final String pattern) {
        return this.setFormat(new SimpleDateFormat(pattern));
    }
}
