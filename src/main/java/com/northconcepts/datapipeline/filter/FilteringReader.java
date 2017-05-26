package com.northconcepts.datapipeline.filter;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;

public class FilteringReader extends ProxyReader
{
    private final List<Filter> filters;
    private Filter currentFilter;
    
    public FilteringReader(final DataReader reader) {
        super(reader);
        this.filters = new ArrayList<Filter>();
    }
    
    public FilteringReader add(final Filter... filters) {
        for (int i = 0; i < filters.length; ++i) {
            this.filters.add(filters[i]);
        }
        return this;
    }
    
    public Filter getCurrentFilter() {
        return this.currentFilter;
    }
    
    public int getCount() {
        return this.filters.size();
    }
    
    public Filter get(final int index) {
        return this.filters.get(index);
    }
    
    public FilteringReader remove(final int index) {
        this.filters.remove(index);
        return this;
    }
    
    public FilteringReader removeAll() {
        this.filters.clear();
        return this;
    }
    
    @Override
	protected Record interceptRecord(final Record record) throws Throwable {
        try {
            for (int i = 0; i < this.filters.size(); ++i) {
                this.currentFilter = this.get(i);
                if (!this.currentFilter.allow(record)) {
                    return this.discard(record, this.currentFilter);
                }
            }
            return record;
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    protected Record discard(final Record record, final Filter filter) {
        return null;
    }
    
    @Override
	public DataException addExceptionProperties(DataException exception) {
        if (this.currentFilter != null) {
            exception.set("FilteringReader.filter", this.currentFilter);
            exception.set("FilteringReader.filterClass", this.currentFilter.getClass());
            exception = this.currentFilter.addExceptionProperties(exception);
        }
        return super.addExceptionProperties(exception);
    }
}
