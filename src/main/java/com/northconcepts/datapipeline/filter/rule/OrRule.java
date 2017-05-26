package com.northconcepts.datapipeline.filter.rule;

import java.util.ArrayList;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.filter.FieldFilterRule;
import com.northconcepts.datapipeline.internal.lang.Util;

public class OrRule extends FieldFilterRule
{
    private final ArrayList<FieldFilterRule> rules;
    
    public OrRule() {
        super();
        this.rules = new ArrayList<FieldFilterRule>();
    }
    
    public OrRule(final FieldFilterRule... rule) {
        super();
        this.rules = new ArrayList<FieldFilterRule>();
        this.add(rule);
    }
    
    public OrRule add(final FieldFilterRule... rule) {
        for (int i = 0; i < rule.length; ++i) {
            this.rules.add(rule[i]);
        }
        return this;
    }
    
    public int getCount() {
        return this.rules.size();
    }
    
    public FieldFilterRule get(final int index) {
        return this.rules.get(index);
    }
    
    @Override
	public boolean allow(final Field field) {
        for (int i = 0; i < this.getCount(); ++i) {
            if (this.get(i).allow(field)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
	public String toString() {
        return "(" + Util.collectionToString(this.rules, ") or (") + ")";
    }
}
