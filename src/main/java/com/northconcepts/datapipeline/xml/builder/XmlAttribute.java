package com.northconcepts.datapipeline.xml.builder;

import java.io.IOException;
import java.io.Writer;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class XmlAttribute
{
    private final XmlExpression name;
    private final XmlExpression value;
    private final XmlLogicalExpression condition;
    
    public XmlAttribute(final String name, final String value) {
        super();
        this.name = new XmlExpression(name);
        this.value = new XmlExpression(value);
        this.condition = null;
    }
    
    public XmlAttribute(final String name, final String value, final String condition) {
        super();
        this.name = new XmlExpression(name);
        this.value = new XmlExpression(value);
        this.condition = new XmlLogicalExpression(condition);
    }
    
    public XmlExpression getName() {
        return this.name;
    }
    
    public XmlExpression getValue() {
        return this.value;
    }
    
    public XmlLogicalExpression getCondition() {
        return this.condition;
    }
    
    public void write(final ExpressionContext context, final Writer writer) throws IOException {
        if (this.condition == null || this.condition.isTrue(context)) {
            this.name.write(context, writer);
            writer.write("=\"");
            if (this.value != null) {
                this.value.write(context, writer);
            }
            writer.write("\"");
        }
    }
}
