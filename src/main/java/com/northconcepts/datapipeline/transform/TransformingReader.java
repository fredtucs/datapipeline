package com.northconcepts.datapipeline.transform;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Messages;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.filter.Filter;

public class TransformingReader extends ProxyReader
{
    private Filter filter;
    private final List<Transformer> transformers;
    private boolean exceptionOnFailure;
    private boolean recordStackTraceInMessage;
    private Transformer currentTransformer;
    private int currentTransformerIndex;
    
    public TransformingReader(final DataReader reader) {
        super(reader);
        this.transformers = new ArrayList<Transformer>();
        this.exceptionOnFailure = true;
        this.recordStackTraceInMessage = true;
    }
    
    public Filter getFilter() {
        return this.filter;
    }
    
    public TransformingReader setFilter(final Filter filter) {
        this.filter = filter;
        return this;
    }
    
    public boolean isExceptionOnFailure() {
        return this.exceptionOnFailure;
    }
    
    public TransformingReader setExceptionOnFailure(final boolean exceptionOnFailure) {
        this.exceptionOnFailure = exceptionOnFailure;
        return this;
    }
    
    public boolean isRecordStackTraceInMessage() {
        return this.recordStackTraceInMessage;
    }
    
    public TransformingReader setRecordStackTraceInMessage(final boolean recordStackTraceInMessage) {
        this.recordStackTraceInMessage = recordStackTraceInMessage;
        return this;
    }
    
    public TransformingReader add(final Transformer... transformer) {
        for (int i = 0; i < transformer.length; ++i) {
            this.transformers.add(transformer[i]);
            transformer[i].setReader(this);
        }
        return this;
    }
    
    public int getCount() {
        return this.transformers.size();
    }
    
    public Transformer get(final int index) {
        return this.transformers.get(index);
    }
    
    @Override
	protected Record interceptRecord(final Record record) throws Throwable {
        final Messages messages = Messages.getCurrent();
        if (this.filter == null || this.filter.allow(record)) {
            this.currentTransformerIndex = 0;
            while (this.currentTransformerIndex < this.getCount()) {
                this.currentTransformer = this.get(this.currentTransformerIndex);
                if (!this.transformRecord(record, this.currentTransformer, messages)) {
                    return null;
                }
                ++this.currentTransformerIndex;
            }
        }
        return record;
    }
    
    protected boolean transformRecord(final Record record, final Transformer transformer, final Messages messages) {
        boolean transformed = false;
        DataException exception = null;
        try {
            transformed = transformer.transform(record);
        }
        catch (Throwable e) {
            exception = this.exception(e);
        }
        if (!transformed) {
            final String message = this.getFailureMessage(record, transformer);
            if (this.exceptionOnFailure) {
                if (exception != null) {
                    throw this.exception(message, exception);
                }
                throw this.exception(message);
            }
            else if (exception != null) {
                messages.addWarning(exception.getMessage(), exception);
                messages.addWarning(message, false);
            }
            else {
                messages.addWarning(message, this.recordStackTraceInMessage);
            }
        }
        return transformed;
    }
    
    private String getFailureMessage(final Record record, final Transformer transformer) {
        if (this.filter != null) {
            return "transformation [if filter(" + this.filter + ") then transform(" + transformer + ")] failed on record " + this.getRecordCountAsString();
        }
        return "transformation [" + transformer + "] failed on record " + this.getRecordCountAsString();
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.currentTransformer != null) {
            exception.set("TransformingReader.transformer", this.currentTransformer);
            exception.set("TransformingReader.transformerClass", this.currentTransformer.getClass());
            this.currentTransformer.addExceptionProperties(exception);
        }
        exception.set("TransformingReader.transformerIndex", this.currentTransformerIndex);
        exception.set("TransformingReader.filter", this.filter);
        if (this.filter != null) {
            this.filter.addExceptionProperties(exception);
        }
        return super.addExceptionProperties(exception);
    }
}
