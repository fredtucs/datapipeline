package com.northconcepts.datapipeline.transform;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.filter.Filter;

@Deprecated
class ConditionalTransformer extends Transformer
{
    private Filter filter;
    private Transformer transformer;
    
    public ConditionalTransformer() {
        super();
    }
    
    public ConditionalTransformer(final Filter filter, final Transformer transformer) {
        super();
        this.setFilter(filter);
        this.setTransformer(transformer);
    }
    
    @Override
	void setReader(final TransformingReader reader) {
        super.setReader(reader);
        if (this.transformer != null) {
            this.transformer.setReader(reader);
        }
    }
    
    public Filter getFilter() {
        return this.filter;
    }
    
    public ConditionalTransformer setFilter(final Filter filter) {
        this.filter = filter;
        return this;
    }
    
    public Transformer getTransformer() {
        return this.transformer;
    }
    
    public ConditionalTransformer setTransformer(final Transformer transformer) {
        this.transformer = transformer;
        if (transformer != null) {
            transformer.setReader(this.getReader());
        }
        return this;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("ConditionalTransformer.filter", this.filter);
        if (this.filter != null) {
            this.filter.addExceptionProperties(exception);
        }
        exception.set("ConditionalTransformer.transformer", this.transformer);
        if (this.transformer != null) {
            this.transformer.addExceptionProperties(exception);
        }
        return super.addExceptionProperties(exception);
    }
    
    @Override
	public boolean transform(final Record record) throws Throwable {
        if (this.filter == null) {
            throw this.getReader().exception("filter is null");
        }
        if (this.transformer == null) {
            throw this.getReader().exception("transformer is null");
        }
        return this.filter == null || this.transformer == null || !this.filter.allow(record) || this.transformer.transform(record);
    }
    
    @Override
	public String toString() {
        return "if filter(" + this.filter + ") then transformer(" + this.transformer + ")";
    }
}
