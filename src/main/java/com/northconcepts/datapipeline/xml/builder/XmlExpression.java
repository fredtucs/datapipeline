package com.northconcepts.datapipeline.xml.builder;

import java.io.IOException;
import java.io.Writer;

import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.XMLUtil;
import com.northconcepts.datapipeline.internal.parser.Parser;

public class XmlExpression
{
    private final String source;
    private final Expression expression;
    
    public XmlExpression(final String source) {
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
    
    public void write(final ExpressionContext context, final Writer writer) throws IOException {
        final Object result = this.expression.evaluate(context);
        if (result != null) {
            String s = result.toString();
            s = XMLUtil.escapeXml(s);
            writer.write(s);
        }
    }
}
