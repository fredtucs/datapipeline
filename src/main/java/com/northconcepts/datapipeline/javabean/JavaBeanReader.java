package com.northconcepts.datapipeline.javabean;

import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.internal.javabean.JavaStreamReader;
import com.northconcepts.datapipeline.internal.javabean.JavaToXmlNodeReader;
import com.northconcepts.datapipeline.xml.XmlReader;

public class JavaBeanReader extends ProxyReader
{
    private final XmlReader reader;
    
    public JavaBeanReader(final String name, final Object javabean) {
        super(null);
        try {
            final JavaStreamReader javaStreamReader = new JavaStreamReader(name, javabean);
            final JavaToXmlNodeReader javaToXmlNodeReader = new JavaToXmlNodeReader(javaStreamReader);
            this.setNestedDataReader(this.reader = new XmlReader(javaToXmlNodeReader));
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public JavaBeanReader addRecordBreak(final String locationPathAsString) {
        this.reader.addRecordBreak(locationPathAsString);
        return this;
    }
    
    public JavaBeanReader addField(final String name, final String locationPathAsString, final boolean cascadeValues) {
        this.reader.addField(name, locationPathAsString, cascadeValues);
        return this;
    }
    
    public JavaBeanReader addField(final String name, final String locationPathAsString) {
        this.reader.addField(name, locationPathAsString);
        return this;
    }
}
