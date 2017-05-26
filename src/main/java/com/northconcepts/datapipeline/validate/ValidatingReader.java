package com.northconcepts.datapipeline.validate;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Messages;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.filter.Filter;
import com.northconcepts.datapipeline.filter.FilteringReader;

public class ValidatingReader extends FilteringReader
{
    private boolean exceptionOnFailure;
    private boolean recordStackTraceInMessage;
    
    public ValidatingReader(final DataReader reader) {
        super(reader);
        this.exceptionOnFailure = true;
        this.recordStackTraceInMessage = true;
    }
    
    public boolean isExceptionOnFailure() {
        return this.exceptionOnFailure;
    }
    
    public ValidatingReader setExceptionOnFailure(final boolean exceptionOnFailure) {
        this.exceptionOnFailure = exceptionOnFailure;
        return this;
    }
    
    public boolean isRecordStackTraceInMessage() {
        return this.recordStackTraceInMessage;
    }
    
    public ValidatingReader setRecordStackTraceInMessage(final boolean recordStackTraceInMessage) {
        this.recordStackTraceInMessage = recordStackTraceInMessage;
        return this;
    }
    
    @Override
	protected Record discard(final Record record, final Filter filter) {
        final String message = "validation [" + this.getCurrentFilter() + "] failed on record #" + this.getRecordCountAsString() + " " + record;
        if (this.exceptionOnFailure) {
            throw this.exception(message).setRecord(record);
        }
        Messages.getCurrent().addWarning(message, this.recordStackTraceInMessage);
        return null;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("ValidatingReader.exceptionOnFailure", this.exceptionOnFailure);
        exception.set("ValidatingReader.recordMessageStackTrace", this.recordStackTraceInMessage);
        return super.addExceptionProperties(exception);
    }
}
