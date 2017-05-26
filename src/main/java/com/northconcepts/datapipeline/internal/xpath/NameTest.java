package com.northconcepts.datapipeline.internal.xpath;

import javax.xml.namespace.QName;

import com.northconcepts.datapipeline.core.DataException;

public class NameTest extends NodeTest
{
    private final QName name;
    
    public NameTest(final QName name) {
        super();
        this.name = name;
    }
    
    @Override
	public String toString() {
        return "NameTest[" + this.name + "]";
    }
    
    public QName getName() {
        return this.name;
    }
    
    @Override
	public boolean matches(final XmlNode node) {
        switch (this.step.getAxis().getPrincipalNodeType()) {
            case ELEMENT: {
                return node.getType() == NodeType.NODE && QNameComparator.matches(this.name, node.getName());
            }
            case ATTRIBUTE: {
                return node.getType() == NodeType.NODE && node.getAttribute(this.name) != null;
            }
            default: {
                throw new DataException("unhandled node type");
            }
        }
    }
    
    @Override
	public Object getValue(final XmlNode node) {
        switch (this.step.getAxis().getPrincipalNodeType()) {
            case ELEMENT: {
                return node.getValue();
            }
            case ATTRIBUTE: {
                return node.getAttribute(this.name).getValue();
            }
            default: {
                throw new DataException("unhandled node type");
            }
        }
    }
}
