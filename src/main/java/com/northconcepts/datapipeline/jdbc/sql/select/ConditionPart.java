package com.northconcepts.datapipeline.jdbc.sql.select;

import java.util.List;

public final class ConditionPart extends QueryCriteriaPart
{
    private final String statement;
    private final Object[] values;
    private final boolean hasValue;
    
    public ConditionPart(final String statement, final Object... values) {
        super();
        this.statement = statement;
        this.values = values;
        this.hasValue = (values != null && values.length > 0);
    }
    
    @Override
	protected void getPartParameterValues(final List<Object> values) {
        if (this.hasValue) {
            for (int i = 0; i < this.values.length; ++i) {
                values.add(this.values[i]);
            }
        }
    }
    
    public String getStatement() {
        return this.statement;
    }
    
    public Object[] getValues() {
        return this.values;
    }
    
    public boolean getHasValue() {
        return this.hasValue;
    }
    
    @Override
	public boolean isApplicable() {
        return true;
    }
    
    @Override
	public int getConditionCount() {
        return 1;
    }
    
    @Override
	public String getSqlFragment() {
        return this.statement;
    }
}
