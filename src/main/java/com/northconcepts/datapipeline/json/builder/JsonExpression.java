package com.northconcepts.datapipeline.json.builder;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.parser.Parser;

public class JsonExpression
{
    private final String source;
    private final Expression expression;
    
    public JsonExpression(final String source) {
        super();
        this.source = source;
        this.expression = Parser.parseExpression(source);
    }
    
    public String getSource() {
        return this.source;
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    public Object evaluate(final ExpressionContext expressionContext) {
        return this.expression.evaluate(expressionContext);
    }
}
