package com.northconcepts.datapipeline.filter.rule;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.filter.FieldFilterRule;

public class IsInstanceOfJavaType extends FieldFilterRule
{
    private final Class<?> type;
    
    public IsInstanceOfJavaType(final Class<?> type) {
        super();
        this.type = type;
    }
    
    @Override
	public boolean allow(final Field field) {
        return this.type.isInstance(field.getValue());
    }
    
    @Override
	public String toString() {
        return "value is instance of " + this.type;
    }
}
