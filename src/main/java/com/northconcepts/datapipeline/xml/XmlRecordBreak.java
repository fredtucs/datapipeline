package com.northconcepts.datapipeline.xml;

import com.northconcepts.datapipeline.internal.xpath.LocationPath;
import com.northconcepts.datapipeline.internal.xpath.parser.XPathParserUtil;

public class XmlRecordBreak
{
    private LocationPath locationPath;
    
    public XmlRecordBreak() {
        super();
    }
    
    public XmlRecordBreak(final String locationPathAsString) {
        super();
        this.setLocationPath(locationPathAsString);
    }
    
    public LocationPath getLocationPath() {
        return this.locationPath;
    }
    
    private XmlRecordBreak setLocationPath(final LocationPath locationPath) {
        this.locationPath = locationPath;
        return this;
    }
    
    public XmlRecordBreak setLocationPath(final String locationPathAsString) {
        this.setLocationPath(XPathParserUtil.parseLocationPath(locationPathAsString));
        return this;
    }
    
    @Override
	public String toString() {
        return "XmlRecordBreak[" + this.locationPath + "]";
    }
}
