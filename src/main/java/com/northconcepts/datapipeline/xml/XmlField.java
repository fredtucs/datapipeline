package com.northconcepts.datapipeline.xml;

import java.util.LinkedList;

import com.northconcepts.datapipeline.internal.lang.Pair;
import com.northconcepts.datapipeline.internal.xpath.LocationPath;
import com.northconcepts.datapipeline.internal.xpath.parser.XPathParserUtil;

public class XmlField
{
    private String name;
    private LocationPath locationPath;
    private boolean cascadeValues;
    private LinkedList<Pair<Long, Object>> values;
    
    public XmlField() {
        super();
        this.values = new LinkedList<Pair<Long, Object>>();
    }
    
    public XmlField(final String name, final String locationPathAsString, final boolean cascadeValues) {
        super();
        this.values = new LinkedList<Pair<Long, Object>>();
        this.setName(name);
        this.setLocationPath(locationPathAsString);
        this.setCascadeValues(cascadeValues);
    }
    
    public String getName() {
        return this.name;
    }
    
    public XmlField setName(final String name) {
        this.name = name;
        return this;
    }
    
    public LocationPath getLocationPath() {
        return this.locationPath;
    }
    
    private XmlField setLocationPath(final LocationPath locationPath) {
        this.locationPath = locationPath;
        return this;
    }
    
    public XmlField setLocationPath(final String locationPathAsString) {
        this.setLocationPath(XPathParserUtil.parseLocationPath(locationPathAsString));
        return this;
    }
    
    public boolean isCascadeValues() {
        return this.cascadeValues;
    }
    
    public XmlField setCascadeValues(final boolean cascadeValues) {
        this.cascadeValues = cascadeValues;
        return this;
    }
    
    protected LinkedList<Pair<Long, Object>> getValues() {
        return this.values;
    }
    
    protected XmlField addValue(final long sequence, final Object value) {
        this.values.add(new Pair<Long, Object>(sequence, value));
        return this;
    }
    
    protected XmlField removeValues(final long sequence) {
        while (this.values.size() > 0 && this.values.getLast().getA() >= sequence) {
            this.values.removeLast();
        }
        return this;
    }
    
    @Override
	public String toString() {
        return "XmlField[" + this.locationPath + "]";
    }
}
