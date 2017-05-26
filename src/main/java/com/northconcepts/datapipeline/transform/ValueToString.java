package com.northconcepts.datapipeline.transform;

import com.northconcepts.datapipeline.core.Field;

class ValueToString extends FieldTransformer
{
    public ValueToString(final String name) {
        super(name);
    }
    
    @Override
	protected void transformField(final Field field) throws Throwable {
        field.setValue(field.getValueAsString());
    }
    
    @Override
	public String toString() {
        return "convert " + this.getName() + " to string";
    }
}
