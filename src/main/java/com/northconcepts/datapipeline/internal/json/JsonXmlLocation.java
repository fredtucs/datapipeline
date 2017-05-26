package com.northconcepts.datapipeline.internal.json;

import javax.xml.stream.Location;

import org.codehaus.jackson.JsonLocation;

public class JsonXmlLocation implements Location
{
    private final JsonLocation location;
    
    public JsonXmlLocation(final JsonLocation location) {
        super();
        this.location = location;
    }
    
    public JsonLocation getLocation() {
        return this.location;
    }
    
    public int getLineNumber() {
        return this.location.getLineNr();
    }
    
    public int getColumnNumber() {
        return this.location.getColumnNr();
    }
    
    public int getCharacterOffset() {
        return (int)this.location.getCharOffset();
    }
    
    public String getPublicId() {
        return null;
    }
    
    public String getSystemId() {
        return null;
    }
}
