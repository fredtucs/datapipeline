package com.northconcepts.datapipeline.filter.rule;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.filter.FieldFilterRule;

public class IsNotNull extends FieldFilterRule
{
    @Override
	public boolean allow(final Field field) {
        return field.isNotNull();
    }
    
    @Override
	public String toString() {
        return "is not null";
    }
}
