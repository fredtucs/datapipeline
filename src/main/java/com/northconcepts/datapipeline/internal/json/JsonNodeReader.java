package com.northconcepts.datapipeline.internal.json;

import java.io.File;
import java.io.Reader;
import java.util.LinkedList;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.lang.Util;

public class JsonNodeReader
{
    private final JsonFactory factory;
    private final JsonParser reader;
    private JsonXmlNode currentNode;
    private final LinkedList<JsonXmlNode> buffer;
    
    public JsonNodeReader(final File file) {
        super();
        this.factory = new JsonFactory();
        this.buffer = new LinkedList<JsonXmlNode>();
        try {
            this.initFactory(this.factory);
            this.reader = this.factory.createJsonParser(file);
        }
        catch (Throwable e) {
            throw DataException.wrap(e).set("file", file);
        }
    }
    
    public JsonNodeReader(final Reader reader) {
        super();
        this.factory = new JsonFactory();
        this.buffer = new LinkedList<JsonXmlNode>();
        try {
            this.initFactory(this.factory);
            this.reader = this.factory.createJsonParser(reader);
        }
        catch (Throwable e) {
            throw DataException.wrap(e).set("reader", reader);
        }
    }
    
    protected void initFactory(final JsonFactory factory) {
        factory.enable(JsonParser.Feature.ALLOW_COMMENTS);
        factory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
    }
    
    public JsonXmlNode getNode() {
        return this.currentNode;
    }
    
    public void close() {
        try {
            this.reader.close();
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    public int available() {
        return this.buffer.size();
    }
    
    public final int getBufferSize() {
        return this.buffer.size();
    }
    
    protected void push(final JsonXmlNode node) {
        this.buffer.addFirst(node);
    }
    
    protected void put(final JsonXmlNode node) {
        this.buffer.addLast(node);
    }
    
    protected JsonXmlNode pop() {
        if (this.buffer.isEmpty()) {
            return null;
        }
        return this.buffer.removeFirst();
    }
    
    protected JsonXmlNode peek(final int index) {
        while (index >= this.buffer.size()) {
            final JsonXmlNode node = this.readImpl();
            if (node == null) {
                return null;
            }
            this.put(node);
        }
        return this.buffer.get(index);
    }
    
    protected JsonXmlNode readImpl() throws DataException {
        try {
            while (this.reader.nextToken() != null) {
                final JsonToken token = this.reader.getCurrentToken();
                final JsonXmlNode lastNode = this.currentNode;
                switch (token) {
                    case START_OBJECT: {
                        this.currentNode = new JsonXmlNode(this.currentNode, token, "object", this.reader.getTokenLocation());
                        continue;
                    }
                    case START_ARRAY: {
                        this.currentNode = new JsonXmlNode(this.currentNode, token, "array", this.reader.getTokenLocation());
                        continue;
                    }
                    case END_OBJECT:
                    case END_ARRAY: {
                        final JsonXmlNode result = this.currentNode;
                        this.currentNode = this.currentNode.getParent();
                        if (this.currentNode != null && this.currentNode.getToken() == JsonToken.FIELD_NAME) {
                            this.push(this.currentNode);
                            this.currentNode = this.currentNode.getParent();
                        }
                        return result;
                    }
                    case FIELD_NAME: {
                        this.currentNode = new JsonXmlNode(this.currentNode, token, this.reader.getCurrentName(), this.reader.getTokenLocation());
                        continue;
                    }
                    case VALUE_STRING: {
                        return this.addValue(token, lastNode, this.reader.getText());
                    }
                    case VALUE_NUMBER_INT: {
                        return this.addValue(token, lastNode, this.reader.getLongValue());
                    }
                    case VALUE_NUMBER_FLOAT: {
                        return this.addValue(token, lastNode, this.reader.getDoubleValue());
                    }
                    case VALUE_NULL: {
                        return this.addValue(token, lastNode, null);
                    }
                    case VALUE_FALSE: {
                        return this.addValue(token, lastNode, false);
                    }
                    case VALUE_TRUE: {
                        return this.addValue(token, lastNode, true);
                    }
                    default: {
                        DataEndpoint.log.debug("  --unhandled JSON token: " + token);
                        continue;
                    }
                }
            }
            return null;
        }
        catch (Throwable e) {
            throw this.addExceptionProperties("JsonNodeReader", DataException.wrap(e));
        }
    }
    
    private JsonXmlNode addValue(final JsonToken token, JsonXmlNode result, final Object value) {
        if (this.currentNode.getToken() == JsonToken.START_ARRAY) {
            return new JsonXmlNode(this.currentNode, token, null, this.reader.getTokenLocation()).setValue(value);
        }
        this.currentNode.setValue(value);
        this.currentNode = this.currentNode.getParent();
        if (result != null && Util.isNotEmpty(result.getText())) {
            this.push(result);
            result = result.textAsChildNode();
        }
        return result;
    }
    
    public JsonXmlNode read() throws DataException {
        try {
            JsonXmlNode node = this.pop();
            if (node != null) {
                return node;
            }
            node = this.readImpl();
            if (node == null) {
                if (this.getBufferSize() > 0) {
                    return this.read();
                }
            }
            return node;
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    public DataException addExceptionProperties(String prefix, final DataException exception) {
        try {
            if (prefix == null) {
                prefix = "";
            }
            else if (!prefix.endsWith(".")) {
                prefix += ".";
            }
            if (this.reader != null) {
                if (this.reader.hasTextCharacters()) {
                    exception.set(prefix + "textStart", this.reader.getTextOffset());
                    exception.set(prefix + "textLength", this.reader.getTextLength());
                    exception.set(prefix + "textCharacters", new String(this.reader.getTextCharacters()));
                    exception.set(prefix + "text", this.reader.getText());
                }
                final JsonLocation location = this.reader.getTokenLocation();
                if (location != null) {
                    exception.set(prefix + "location.characterOffset", location.getCharOffset());
                    exception.set(prefix + "location.columnNumber", location.getColumnNr());
                    exception.set(prefix + "location.lineNumber", location.getLineNr());
                }
                exception.set(prefix + "encoding", this.reader.getCodec());
            }
            if (this.currentNode != null) {
                exception.set(prefix + "currentNode", this.currentNode);
            }
            return exception;
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
}
