package com.northconcepts.datapipeline.internal.xpath;

public abstract class NodeTest
{
    Step step;
    
    public abstract boolean matches(final XmlNode p0);
    
    public abstract Object getValue(final XmlNode p0);
    
    public Step getStep() {
        return this.step;
    }
    
    public PrincipalNodeType getNodeType() {
        return this.step.getAxis().getPrincipalNodeType();
    }
}
