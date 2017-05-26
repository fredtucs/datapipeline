package com.northconcepts.datapipeline.internal.javabean;

import javax.xml.namespace.QName;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.xpath.XmlNode;

public class JavaNode extends XmlNode
{
    private final Object value;
    
    public JavaNode(final JavaNode parent, final String name, final Object value, final int depth, final int position, final int eventType) {
        super(parent, eventType, new QName((name == null) ? "" : name), (value == null) ? null : value.toString(), depth, position, null);
        this.value = value;
    }
    
    @Override
	public JavaNode getParent() {
        return (JavaNode)super.getParent();
    }
    
    @Override
	public Object getValue() {
        return this.value;
    }
    
    public boolean startsWith(JavaNode prefix) {
        JavaNode node = this;
        if (prefix == null || prefix.getDepth() > node.getDepth()) {
            return false;
        }
        while (node != null && prefix.getDepth() < node.getDepth()) {
            node = node.getParent();
        }
        if (node == null) {
            return false;
        }
        while (prefix != null && node != null) {
            if (prefix != node) {
                return false;
            }
            prefix = prefix.getParent();
            node = node.getParent();
        }
        if (prefix != null || node != null) {
            throw new DataException("assertion failed: expected prefix and node to be null").set("prefix", prefix).set("node", node);
        }
        return true;
    }
}
