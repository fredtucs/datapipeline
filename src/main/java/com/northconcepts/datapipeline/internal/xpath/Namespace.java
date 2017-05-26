package com.northconcepts.datapipeline.internal.xpath;

public class Namespace
{
    private final String prefix;
    private final String namespaceURI;
    
    public Namespace(final String prefix, final String namespaceURI) {
        super();
        this.prefix = prefix;
        this.namespaceURI = namespaceURI;
    }
    
    public String getNamespaceURI() {
        return this.namespaceURI;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
}
