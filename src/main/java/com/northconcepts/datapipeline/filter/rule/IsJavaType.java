package com.northconcepts.datapipeline.filter.rule;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.filter.FieldFilterRule;

public class IsJavaType extends FieldFilterRule
{
    private final Class<?> type;
    
    public IsJavaType(final Class<?> type) {
        super();
        this.type = type;
    }
    
    @Override
	public boolean allow(final Field field) {
        return field.isNull() || field.getValue().getClass() == this.type;
    }
    
    @Override
	public String toString() {
        return "value is of " + this.type;
    }
}
