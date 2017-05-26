package com.northconcepts.datapipeline.json.builder;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.expression.logical.LogicalExpression;
import com.northconcepts.datapipeline.internal.parser.Parser;

public class JsonLogicalExpression
{
    private final String source;
    private final LogicalExpression expression;
    
    public JsonLogicalExpression(final String source) {
        super();
        this.source = source;
        this.expression = Parser.parseLogicalExpression(source);
    }
    
    public String getSource() {
        return this.source;
    }
    
    public LogicalExpression getExpression() {
        return this.expression;
    }
    
    public boolean isTrue(final ExpressionContext context) {
        return this.expression.evaluateBoolean(context);
    }
}
