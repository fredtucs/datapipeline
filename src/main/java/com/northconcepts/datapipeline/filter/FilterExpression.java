package com.northconcepts.datapipeline.filter;

import com.northconcepts.datapipeline.core.FieldDeclarations;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.expression.RecordExpressionContext;
import com.northconcepts.datapipeline.internal.expression.logical.LogicalExpression;
import com.northconcepts.datapipeline.internal.parser.Parser;

public class FilterExpression extends Filter
{
    private final String expressionAsString;
    private final LogicalExpression expression;
    private final RecordExpressionContext expressionContext;
    
    public FilterExpression(final String logicalExpression, final FieldDeclarations fieldDeclarations) {
        super();
        this.expressionAsString = logicalExpression;
        this.expression = Parser.parseLogicalExpression(logicalExpression);
        this.expressionContext = new RecordExpressionContext(fieldDeclarations);
    }
    
    public FilterExpression(final String logicalExpression) {
        this(logicalExpression, new FieldDeclarations());
    }
    
    @Override
	public boolean allow(final Record record) {
        this.expressionContext.setRecord(record);
        return this.expression.evaluateBoolean(this.expressionContext);
    }
    
    @Override
	public String toString() {
        return "record satisfies expression: " + this.expressionAsString;
    }
}
