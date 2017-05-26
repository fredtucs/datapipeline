package com.northconcepts.datapipeline.internal.json;

import javax.xml.namespace.QName;

import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonToken;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.xpath.XmlNode;

public class JsonXmlNode extends XmlNode
{
    private final JsonToken token;
    private Object value;
    
    public JsonXmlNode(final JsonXmlNode parent, final JsonToken token, final String name, final JsonLocation location) {
        super(parent, jsonTokenToXmlEvent(token), (name == null) ? null : new QName(name), null, (parent == null) ? 0 : (parent.getDepth() + 1), (parent == null) ? 0 : parent.getChildCount(), new JsonXmlLocation(location));
        this.token = token;
    }
    
    @Override
	public JsonXmlNode getParent() {
        return (JsonXmlNode)super.getParent();
    }
    
    public JsonToken getToken() {
        return this.token;
    }
    
    @Override
	public Object getValue() {
        return this.value;
    }
    
    public JsonXmlNode setValue(final Object value) {
        this.value = value;
        if (value != null) {
            this.addText(value.toString());
        }
        return this;
    }
    
    @Override
	public JsonXmlLocation getLocation() {
        return (JsonXmlLocation)super.getLocation();
    }
    
    @Override
	public JsonXmlNode textAsChildNode() {
        final JsonLocation location = (this.getLocation() == null) ? null : this.getLocation().getLocation();
        final JsonXmlNode node = new JsonXmlNode(this, JsonToken.VALUE_STRING, null, location);
        node.setValue(this.getValue());
        return node;
    }
    
    public boolean startsWith(JsonXmlNode prefix) {
        JsonXmlNode node = this;
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
    
    public static int jsonTokenToXmlEvent(final JsonToken token) {
        switch (token) {
            case START_OBJECT:
            case START_ARRAY: {
                return 1;
            }
            case END_OBJECT:
            case END_ARRAY:
            case FIELD_NAME: {
                return 2;
            }
            case VALUE_STRING:
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
            case VALUE_NULL:
            case VALUE_FALSE:
            case VALUE_TRUE: {
                return 4;
            }
            default: {
                throw new DataException("unhandled JsonToken").set("JsonToken", token);
            }
        }
    }
}
