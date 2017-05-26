package com.northconcepts.datapipeline.jdbc.sql.select;

import java.util.List;

public class NotPart extends QueryCriteriaPart
{
    private final QueryCriteriaPart part;
    
    public NotPart(final QueryCriteriaPart part) {
        super();
        this.part = part;
    }
    
    @Override
	public boolean isApplicable() {
        return this.part.isApplicable();
    }
    
    @Override
	public String getSqlFragment() {
        return "not (" + this.part.getSqlFragment() + ")";
    }
    
    @Override
	protected void getPartParameterValues(final List<Object> values) {
        this.part.getParameterValues(values);
    }
}
