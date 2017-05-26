package com.northconcepts.datapipeline.xml.builder;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class XmlElement extends XmlNodeContainer
{
    private final XmlExpression name;
    private final List<XmlAttribute> attributes;
    
    public XmlElement(final String name) {
        super();
        this.attributes = new ArrayList<XmlAttribute>();
        this.name = new XmlExpression(name);
    }
    
    public XmlExpression getName() {
        return this.name;
    }
    
    public List<XmlAttribute> getAttributes() {
        return this.attributes;
    }
    
    public XmlElement attribute(final String name, final String value) {
        final XmlAttribute a = new XmlAttribute(name, value);
        this.attributes.add(a);
        return this;
    }
    
    @Override
	protected void writeStartImpl(final ExpressionContext context, final Writer writer) throws Throwable {
        writer.write("<");
        this.name.write(context, writer);
        for (final XmlAttribute attribute : this.attributes) {
            writer.write(" ");
            attribute.write(context, writer);
        }
        writer.write(">");
    }
    
    @Override
	protected void writeEndImpl(final ExpressionContext context, final Writer writer) throws Throwable {
        writer.write("</");
        this.name.write(context, writer);
        writer.write(">");
    }
}
