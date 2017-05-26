package com.northconcepts.datapipeline.jdbc.sql.select;

import java.util.List;

public abstract class ConjunctivePart extends QueryCriteriaPart
{
    private final String operator;
    
    public ConjunctivePart(final String operator) {
        super();
        this.operator = operator;
    }
    
    @Override
	public boolean isApplicable() {
        for (int i = 0; i < this.getParts().size(); ++i) {
            if (this.getParts().get(i).isApplicable()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
	public String getSqlFragment() {
        int applicableParts = 0;
        String s = "";
        for (int i = 0; i < this.getParts().size(); ++i) {
            final QueryCriteriaPart part = this.getParts().get(i);
            if (part.isApplicable()) {
                if (applicableParts > 0) {
                    s = s + " " + this.operator + " ";
                }
                ++applicableParts;
                if (this.getParts().size() == 1 || part.getConditionCount() == 1 || this.getConditionCount() <= 2) {
                    s += part.getSqlFragment();
                }
                else {
                    s = s + "(" + part.getSqlFragment() + ")";
                }
            }
        }
        return s;
    }
    
    @Override
	protected void getPartParameterValues(final List<Object> values) {
    }
    
    @Override
	public List<QueryCriteriaPart> getParts() {
        return super.getParts();
    }
    
    @Override
	public ConjunctivePart add(final String statement, final Object... values) {
        super.add(statement, values);
        return this;
    }
    
    @Override
	public ConjunctivePart addFirst(final String statement, final Object... values) {
        super.addFirst(statement, values);
        return this;
    }
    
    @Override
	public ConjunctivePart add(final QueryCriteriaPart part) {
        super.add(part);
        return this;
    }
    
    @Override
	public ConjunctivePart addFirst(final QueryCriteriaPart part) {
        super.addFirst(part);
        return this;
    }
}
