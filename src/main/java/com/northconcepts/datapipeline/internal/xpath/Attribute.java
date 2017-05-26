package com.northconcepts.datapipeline.internal.xpath;

import javax.xml.namespace.QName;

public class Attribute
{
    private final QName name;
    private final String value;
    
    public Attribute(final QName name, final String value) {
        super();
        this.name = name;
        this.value = value;
    }
    
    public QName getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
	public String toString() {
        return this.name + "=" + this.value;
    }
}
