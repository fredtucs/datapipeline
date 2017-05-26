package com.northconcepts.datapipeline.filter.rule;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.filter.FieldFilterRule;

public class IsFieldType extends FieldFilterRule
{
    private final FieldType type;
    
    public IsFieldType(final FieldType type) {
        super();
        this.type = type;
    }
    
    @Override
	public boolean allow(final Field field) {
        return field.getType() == this.type;
    }
    
    @Override
	public String toString() {
        return "field is of type " + this.type;
    }
}
