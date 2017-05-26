package com.northconcepts.datapipeline.transform;

import com.northconcepts.datapipeline.core.Field;

class NullToValue extends FieldTransformer
{
    private final Object value;
    
    public NullToValue(final String name, final Object value) {
        super(name);
        this.value = value;
    }
    
    @Override
	protected void transformField(final Field field) throws Throwable {
        if (field.isNull()) {
            field.setValue(this.value);
        }
    }
    
    @Override
	public String toString() {
        return "convert " + this.getName() + " from null to [" + this.value + "]";
    }
}
