package com.northconcepts.datapipeline.jdbc.sql.select;

public class OrPart extends ConjunctivePart
{
    public OrPart() {
        super("OR");
    }
    
    @Override
	public OrPart add(final String statement, final Object... values) {
        return (OrPart)super.add(statement, values);
    }
    
    @Override
	public OrPart addFirst(final String statement, final Object... values) {
        return (OrPart)super.addFirst(statement, values);
    }
    
    @Override
	public OrPart add(final QueryCriteriaPart part) {
        super.add(part);
        return this;
    }
    
    @Override
	public OrPart addFirst(final QueryCriteriaPart part) {
        super.addFirst(part);
        return this;
    }
}
