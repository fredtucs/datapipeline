package com.northconcepts.datapipeline.jdbc.sql.select;

import java.util.ArrayList;
import java.util.List;

public final class QuerySelection extends QueryPart
{
    private final List<String> fields;
    private boolean distinct;
    
    public QuerySelection() {
        super();
        this.fields = new ArrayList<String>();
    }
    
    public QuerySelection(final String... fields) {
        super();
        this.fields = new ArrayList<String>();
        this.add(fields);
    }
    
    public QuerySelection add(final String... fields) {
        if (fields != null) {
            for (final String field : fields) {
                this.fields.add(field);
            }
        }
        return this;
    }
    
    public boolean isDistinct() {
        return this.distinct;
    }
    
    public QuerySelection setDistinct(final boolean distinct) {
        this.distinct = distinct;
        return this;
    }
    
    public List<String> getFields() {
        return this.fields;
    }
    
    @Override
	public String getSqlFragment() {
        String s = "SELECT ";
        if (this.distinct) {
            s += "DISTINCT ";
        }
        if (this.fields.size() == 0) {
            s += "*";
        }
        else {
            s += this.fields.get(0);
            for (int i = 1; i < this.fields.size(); ++i) {
                s = s + ", " + this.fields.get(i);
            }
        }
        return s;
    }
}
