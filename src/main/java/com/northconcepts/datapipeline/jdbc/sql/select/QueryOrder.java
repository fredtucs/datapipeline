package com.northconcepts.datapipeline.jdbc.sql.select;

import java.util.ArrayList;
import java.util.List;

public final class QueryOrder extends QueryPart
{
    private final List<QueryOrderPart> parts;
    
    public QueryOrder() {
        super();
        this.parts = new ArrayList<QueryOrderPart>();
    }
    
    public QueryOrder add(final String... fields) {
        if (fields != null) {
            for (final String field : fields) {
                this.parts.add(new QueryOrderPart(field, true));
            }
        }
        return this;
    }
    
    public QueryOrder add(final String field, final boolean ascending) {
        this.parts.add(new QueryOrderPart(field, ascending));
        return this;
    }
    
    public List<QueryOrderPart> getParts() {
        return this.parts;
    }
    
    @Override
	public String getSqlFragment() {
        String s = "";
        if (this.parts.size() > 0) {
            s = s + "ORDER BY " + this.parts.get(0);
        }
        for (int i = 1; i < this.parts.size(); ++i) {
            s = s + ", " + this.parts.get(i);
        }
        return s;
    }
}
