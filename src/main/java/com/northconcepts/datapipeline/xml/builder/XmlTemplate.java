package com.northconcepts.datapipeline.xml.builder;

import java.io.IOException;
import java.io.Writer;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;
import com.northconcepts.datapipeline.internal.expression.RecordExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Util;

public class XmlTemplate extends XmlFragment
{
    private String declaration;
    private final DefaultExpressionContext expressionContext;
    private final RecordExpressionContext recordExpressionContext;
    private final XmlNodeWriter nodeWriter;
    private XmlFragment detail;
    
    public XmlTemplate() {
        super();
        this.expressionContext = new DefaultExpressionContext();
        this.recordExpressionContext = new RecordExpressionContext();
        this.nodeWriter = new XmlNodeWriter(this);
        this.recordExpressionContext.setParent(this.expressionContext);
    }
    
    public String getDeclaration() {
        return this.declaration;
    }
    
    public XmlTemplate setDeclaration(final String declaration) {
        this.declaration = declaration;
        return this;
    }
    
    public void setValue(final String name, final Object value) {
        this.expressionContext.setValue(name, value);
    }
    
    public void setValue(final String name, final Object value, final Class<?> type) {
        this.expressionContext.setValue(name, value, type);
    }
    
    public void buildHeader(final Writer writer) throws IOException {
        if (Util.isNotEmpty(this.declaration)) {
            writer.write(this.declaration);
            writer.write("\n");
        }
        XmlNode node;
        while ((node = this.nodeWriter.nextNode(this.expressionContext, writer)) != null) {
            if (node.isMarker(this.expressionContext) && node instanceof XmlDetailMarker) {
                this.detail = (XmlFragment)node;
                break;
            }
        }
    }
    
    public void buildDetail(final Record record, final Writer writer) throws IOException {
        this.recordExpressionContext.setRecord(record);
        if (this.detail == null) {
            throw new DataException("no detail marker found, call detail() on XmlBuilder or a descendant");
        }
        final XmlNodeWriter nodeWriter = new XmlNodeWriter(this.detail);
        while (nodeWriter.nextNode(this.recordExpressionContext, writer) != null) {}
    }
    
    public void buildFooter(final Writer writer) throws IOException {
        while (this.nodeWriter.nextNode(this.expressionContext, writer) != null) {}
    }
}
