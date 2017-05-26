package com.northconcepts.datapipeline.transform.parse;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.transform.FieldTransformer;

class StringToInt extends FieldTransformer
{
    public StringToInt(final String name) {
        super(name);
    }
    
    @Override
	protected void transformField(final Field field) throws Throwable {
        final String source = field.getValueAsString();
        if (source == null || source.length() == 0) {
            field.setNull(FieldType.INT);
        }
        else {
            final Integer target = Integer.valueOf(source);
            field.setValue((int)target);
        }
    }
    
    @Override
	public String toString() {
        return "convert " + this.getName() + " from String to int";
    }
}
