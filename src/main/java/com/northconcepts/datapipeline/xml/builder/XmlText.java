package com.northconcepts.datapipeline.xml.builder;

import java.io.Writer;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class XmlText extends XmlNode
{
    private final XmlExpression text;
    
    public XmlText(final String text) {
        super();
        this.text = new XmlExpression(text);
    }
    
    public XmlExpression getText() {
        return this.text;
    }
    
    @Override
	protected void writeStartImpl(final ExpressionContext context, final Writer writer) throws Throwable {
        if (this.text != null) {
            this.text.write(context, writer);
        }
    }
    
    @Override
	protected void writeEndImpl(final ExpressionContext context, final Writer writer) throws Throwable {
    }
}
