package com.northconcepts.datapipeline.jdbc.sql.select;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.jdbc.sql.SqlPart;

public final class Select extends SqlPart
{
    private static final Object[] EMPTY_ARRAY;
    private final QuerySelection selection;
    private final String table;
    private final List<InnerJoin> joins;
    private final QueryCriteria criteria;
    private final QueryGrouping grouping;
    private final QueryOrder order;
    private int offset;
    private int count;
    
    public Select(final String table) {
        super();
        this.selection = new QuerySelection();
        this.joins = new ArrayList<InnerJoin>();
        this.criteria = new QueryCriteria();
        this.grouping = new QueryGrouping();
        this.order = new QueryOrder();
        this.offset = -1;
        this.count = -1;
        if (Util.isEmpty(table)) {
            throw new IllegalArgumentException("table is empty");
        }
        this.table = table;
    }
    
    public String getTable() {
        return this.table;
    }
    
    public QuerySelection getSelection() {
        return this.selection;
    }
    
    public Select select(final String... fields) {
        this.selection.add(fields);
        return this;
    }
    
    public Select innerJoin(final String table, final String condition) {
        this.joins.add(new InnerJoin(table, condition));
        return this;
    }
    
    public QueryCriteria getCriteria() {
        return this.criteria;
    }
    
    public Select where(final String criteria, final Object... value) {
        this.criteria.and(criteria, value);
        return this;
    }
    
    public QueryGrouping getGrouping() {
        return this.grouping;
    }
    
    public Select groupBy(final String... fields) {
        this.grouping.add(fields);
        return this;
    }
    
    public QueryOrder getOrder() {
        return this.order;
    }
    
    public Select orderBy(final String... fields) {
        this.order.add(fields);
        return this;
    }
    
    public Select orderBy(final String field, final boolean ascending) {
        this.order.add(field, ascending);
        return this;
    }
    
    public Select limit(final int count) {
        return this.limit(0, count);
    }
    
    public Select limit(final int offset, final int count) {
        this.offset = offset;
        this.count = count;
        return this;
    }
    
    public int getOffset() {
        return this.offset;
    }
    
    public int getCount() {
        return this.count;
    }
    
    @Override
	public String getSqlFragment() {
        String s = this.selection.getSqlFragment() + " FROM " + this.table;
        for (final InnerJoin join : this.joins) {
            s = s + " " + join.getSqlFragment();
        }
        s = s + " " + this.criteria.getSqlFragment();
        s = s + " " + this.grouping.getSqlFragment();
        s = s + " " + this.order.getSqlFragment();
        if (this.count >= 0) {
            if (this.offset > 0) {
                s = s + " LIMIT " + this.offset + ", " + this.count;
            }
            else {
                s = s + " LIMIT " + this.count;
            }
        }
        return s;
    }
    
    public final Object[] getParameterValues() {
        if (this.criteria != null) {
            final Object[] values = this.criteria.getParameterValues();
            return values;
        }
        return Select.EMPTY_ARRAY;
    }
    
    static {
        EMPTY_ARRAY = new Object[0];
    }
}
