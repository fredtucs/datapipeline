package com.northconcepts.datapipeline.filter;

import java.util.ArrayList;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.Util;

public class FieldFilter extends Filter
{
    private final String name;
    private final ArrayList<FieldFilterRule> rules;
    private FieldFilterRule currentRule;
    private Field currentField;
    
    public FieldFilter(final String name, final FieldFilterRule... rules) {
        super();
        this.rules = new ArrayList<FieldFilterRule>();
        this.name = name;
        if (rules != null) {
            this.addRule(rules);
        }
    }
    
    public FieldFilterRule getCurrentRule() {
        return this.currentRule;
    }
    
    public Field getCurrentField() {
        return this.currentField;
    }
    
    public String getName() {
        return this.name;
    }
    
    public FieldFilter addRule(final FieldFilterRule... rules) {
        for (int i = 0; i < rules.length; ++i) {
            this.rules.add(rules[i]);
            rules[i].setFieldFilter(this);
        }
        return this;
    }
    
    public int getRuleCount() {
        return this.rules.size();
    }
    
    public FieldFilterRule getRule(final int index) {
        return this.rules.get(index);
    }
    
    @Override
	public boolean allow(final Record record) {
        this.currentField = record.getField(this.name);
        for (int i = 0; i < this.getRuleCount(); ++i) {
            this.currentRule = this.getRule(i);
            if (!this.currentRule.allow(this.currentField)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
	public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append(this.name + " field: ");
        if (this.rules.size() == 0) {
            s.append("<no filter rules>");
        }
        else {
            s.append("(" + Util.collectionToString(this.rules, ") and (") + ")");
        }
        return s.toString();
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.currentField != null) {
            exception.set("FieldFilter.field", this.currentField);
        }
        if (this.currentRule != null) {
            exception.set("FieldFilter.rule", this.currentRule);
        }
        for (int i = 0; i < this.getRuleCount(); ++i) {
            exception.set("FieldFilter.rules[" + i + "]", this.getRule(i));
        }
        exception.setFieldName(this.name);
        return super.addExceptionProperties(exception);
    }
}
