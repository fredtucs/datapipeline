package com.northconcepts.datapipeline.xml.builder;

import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public abstract class XmlNodeContainer extends XmlNode
{
    private final List<XmlNode> nodes;
    
    XmlNodeContainer() {
        super();
        this.nodes = new ArrayList<XmlNode>();
    }
    
    public List<XmlNode> getNodes() {
        return this.nodes;
    }
    
    @Override
	public boolean hasChildNodes(final ExpressionContext context) {
        return this.nodes.size() > 0;
    }
    
    @Override
	public List<XmlNode> getChildNodes(final ExpressionContext context) {
        return this.getNodes();
    }
    
    public <T extends XmlNode> T add(final T node) {
        this.nodes.add(node);
        return node;
    }
    
    public final XmlConditionalNode when(final String condition) {
        return this.add(new XmlConditionalNode(condition));
    }
    
    public final XmlElement element(final String name) {
        return this.add(new XmlElement(name));
    }
    
    public final XmlNodeContainer text(final String text) {
        this.add(new XmlText(text));
        return this;
    }
    
    public final XmlNodeContainer detail() {
        return this.add(new XmlDetailMarker());
    }
}
