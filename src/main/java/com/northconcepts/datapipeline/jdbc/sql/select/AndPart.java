package com.northconcepts.datapipeline.jdbc.sql.select;

public class AndPart extends ConjunctivePart
{
    public AndPart() {
        super("AND");
    }
    
    @Override
	public AndPart add(final String statement, final Object... values) {
        super.add(statement, values);
        return this;
    }
    
    @Override
	public AndPart addFirst(final String statement, final Object... values) {
        super.addFirst(statement, values);
        return this;
    }
    
    @Override
	public AndPart add(final QueryCriteriaPart part) {
        super.add(part);
        return this;
    }
    
    @Override
	public AndPart addFirst(final QueryCriteriaPart part) {
        super.addFirst(part);
        return this;
    }
}
