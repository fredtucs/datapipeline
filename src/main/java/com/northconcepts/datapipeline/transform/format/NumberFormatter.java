package com.northconcepts.datapipeline.transform.format;

import java.text.DecimalFormat;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.transform.FieldTransformer;

class NumberFormatter extends FieldTransformer
{
    private Rounder rounding;
    private DecimalFormat format;
    
    public NumberFormatter(final String name) {
        super(name);
    }
    
    @Override
	protected void transformField(final Field field) throws Throwable {
        final Number number = (Number)field.getValue();
        if (number != null) {
            double d = number.doubleValue();
            if (this.rounding != null) {
                d = this.rounding.apply(d);
            }
            if (this.format != null) {
                field.setValue(this.format.format(d));
                return;
            }
            field.setValue(d);
        }
        else if (this.format != null) {
            field.setNull(FieldType.STRING);
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.rounding != null) {
            exception.set("NumberFormatter.rounding", this.rounding);
        }
        if (this.format != null) {
            exception.set("NumberFormatter.pattern", this.format.toPattern());
        }
        return exception;
    }
    
    public DecimalFormat getFormat() {
        return this.format;
    }
    
    public NumberFormatter setFormat(final DecimalFormat format) {
        this.format = format;
        return this;
    }
    
    public NumberFormatter setFormat(final String pattern) {
        return this.setFormat(new DecimalFormat(pattern));
    }
    
    public Rounder getRounding() {
        return this.rounding;
    }
    
    public NumberFormatter setRounding(final Rounder rounding) {
        this.rounding = rounding;
        return this;
    }
}
