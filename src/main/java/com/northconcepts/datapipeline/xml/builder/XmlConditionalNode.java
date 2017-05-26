package com.northconcepts.datapipeline.xml.builder;

import java.io.Writer;
import java.util.List;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class XmlConditionalNode extends XmlNodeContainer
{
    private final XmlLogicalExpression condition;
    
    public XmlConditionalNode(final String condition) {
        super();
        this.condition = new XmlLogicalExpression(condition);
    }
    
    public XmlLogicalExpression getCondition() {
        return this.condition;
    }
    
    @Override
	public boolean hasChildNodes(final ExpressionContext context) {
        return super.hasChildNodes(context) && (this.condition == null || this.condition.isTrue(context));
    }
    
    @Override
	public List<XmlNode> getChildNodes(final ExpressionContext context) {
        if (this.hasChildNodes(context)) {
            return super.getChildNodes(context);
        }
        return null;
    }
    
    @Override
	protected void writeStartImpl(final ExpressionContext context, final Writer writer) throws Throwable {
    }
    
    @Override
	protected void writeEndImpl(final ExpressionContext context, final Writer writer) throws Throwable {
    }
}
