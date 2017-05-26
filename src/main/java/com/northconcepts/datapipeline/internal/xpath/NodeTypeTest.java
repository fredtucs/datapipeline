package com.northconcepts.datapipeline.internal.xpath;

import com.northconcepts.datapipeline.core.DataException;

public class NodeTypeTest extends NodeTest
{
    private final NodeType type;
    
    public NodeTypeTest(final String type) {
        super();
        this.type = NodeType.lookup(type);
    }
    
    @Override
	public String toString() {
        return "NodeTypeTest[" + this.type + "]";
    }
    
    public NodeType getType() {
        return this.type;
    }
    
    @Override
	public boolean matches(final XmlNode node) {
        return this.type == node.getType();
    }
    
    @Override
	public Object getValue(final XmlNode node) {
        switch (node.getType()) {
            case TEXT: {
                return node.getValue();
            }
            case NODE: {
                return (node.getName() == null) ? null : node.getName().toString();
            }
            case COMMENT: {
                return node.getValue();
            }
            case PROCESSING_INSTRUCTION: {
                return node.getValue();
            }
            default: {
                throw new DataException("unhandled node type").set("nodeType", node.getType());
            }
        }
    }
}
