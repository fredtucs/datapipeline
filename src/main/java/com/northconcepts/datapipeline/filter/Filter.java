package com.northconcepts.datapipeline.filter;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;

public abstract class Filter
{
    public DataException addExceptionProperties(final DataException exception) {
        return exception;
    }
    
    public abstract boolean allow(final Record p0);
}
