package com.northconcepts.datapipeline.jdbc.sql.select;

import java.util.ArrayList;
import java.util.List;

public final class QueryGrouping extends QueryPart
{
    private final List<String> fields;
    
    public QueryGrouping() {
        super();
        this.fields = new ArrayList<String>();
    }
    
    public QueryGrouping(final String... fields) {
        super();
        this.fields = new ArrayList<String>();
        this.add(fields);
    }
    
    public QueryGrouping add(final String... fields) {
        if (fields != null) {
            for (final String field : fields) {
                this.fields.add(field);
            }
        }
        return this;
    }
    
    public List<String> getFields() {
        return this.fields;
    }
    
    @Override
	public String getSqlFragment() {
        if (this.fields.size() > 0) {
            String s = "GROUP BY " + this.fields.get(0);
            for (int i = 1; i < this.fields.size(); ++i) {
                s = s + ", " + this.fields.get(i);
            }
            return s;
        }
        return "";
    }
}
