package com.northconcepts.datapipeline.transform.parse;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.transform.FieldTransformer;

class StringToLong extends FieldTransformer
{
    public StringToLong(final String name) {
        super(name);
    }
    
    @Override
	protected void transformField(final Field field) throws Throwable {
        final String source = field.getValueAsString();
        if (source == null || source.length() == 0) {
            field.setNull(FieldType.LONG);
        }
        else {
            final Long target = Long.valueOf(source);
            field.setValue((long)target);
        }
    }
    
    @Override
	public String toString() {
        return "convert " + this.getName() + " from String to long";
    }
}
