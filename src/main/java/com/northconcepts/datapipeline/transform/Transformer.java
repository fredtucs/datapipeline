package com.northconcepts.datapipeline.transform;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;

public abstract class Transformer
{
    private TransformingReader reader;
    
    public TransformingReader getReader() {
        return this.reader;
    }
    
    void setReader(final TransformingReader reader) {
        this.reader = reader;
    }
    
    public DataException addExceptionProperties(final DataException exception) {
        return exception;
    }
    
    public abstract boolean transform(final Record p0) throws Throwable;
}
