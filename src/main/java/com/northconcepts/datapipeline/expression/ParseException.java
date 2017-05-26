package com.northconcepts.datapipeline.expression;

import com.northconcepts.datapipeline.core.DataException;

public class ParseException extends DataException
{
    private static final String EXPRESSION_AS_STRING = "expressionAsString";
    private static final long serialVersionUID = 1L;
    
    public ParseException() {
        super();
    }
    
    public ParseException(final String message) {
        super(message);
    }
    
    public ParseException(final Throwable cause) {
        super(cause);
    }
    
    public ParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ParseException setExpressionAsString(final String expressionAsString) {
        this.set("expressionAsString", expressionAsString);
        return this;
    }
    
    public String getExpressionAsString() {
        return this.getAsString("expressionAsString");
    }
}
