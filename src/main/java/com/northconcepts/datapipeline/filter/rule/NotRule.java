package com.northconcepts.datapipeline.filter.rule;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.filter.FieldFilterRule;

public class NotRule extends FieldFilterRule
{
    private final FieldFilterRule rule;
    
    public NotRule(final FieldFilterRule rule) {
        super();
        this.rule = rule;
    }
    
    @Override
	public boolean allow(final Field field) {
        return !this.rule.allow(field);
    }
    
    @Override
	public String toString() {
        return "not (" + this.rule + ")";
    }
}
