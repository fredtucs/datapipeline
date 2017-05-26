package com.northconcepts.datapipeline.internal.xpath;

import java.util.LinkedList;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.lang.Util;

public class XmlNodeReader
{
    private final XMLStreamReader reader;
    protected XmlNode currentNode;
    private final LinkedList<XmlNode> buffer;
    
    public XmlNodeReader(final XMLStreamReader reader) {
        super();
        this.buffer = new LinkedList<XmlNode>();
        this.reader = reader;
    }
    
    public XMLStreamReader getReader() {
        return this.reader;
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
    
    protected void push(final XmlNode node) {
        this.buffer.addFirst(node);
    }
    
    protected void put(final XmlNode node) {
        this.buffer.addLast(node);
    }
    
    protected XmlNode pop() {
        if (this.buffer.isEmpty()) {
            return null;
        }
        return this.buffer.removeFirst();
    }
    
    protected XmlNode peek(final int index) {
        while (index >= this.buffer.size()) {
            final XmlNode node = this.readImpl();
            if (node == null) {
                return null;
            }
            this.put(node);
        }
        return this.buffer.get(index);
    }
    
    protected XmlNode readImpl() throws DataException {
        try {
            while (this.reader.hasNext()) {
                final int eventType = this.reader.next();
                switch (eventType) {
                    case 1:
                    case 7: {
                        this.currentNode = new XmlNode(this.currentNode, this.reader);
                        continue;
                    }
                    case 2:
                    case 8: {
                        XmlNode result = this.currentNode;
                        if (this.currentNode != null) {
                            this.currentNode = this.currentNode.getParent();
                        }
                        if (result != null && Util.isNotEmpty(result.getText())) {
                            this.put(result);
                            result = result.textAsChildNode();
                        }
                        return result;
                    }
                    case 4:
                    case 12: {
                        this.currentNode.addText(this.reader.getText());
                        continue;
                    }
                    case 3:
                    case 5: {
                        return new XmlNode(this.currentNode, this.reader);
                    }
                    case 6:
                    case 11: {
                        continue;
                    }
                    default: {
                        DataEndpoint.log.debug("  --unhandled Event Type: " + eventType);
                        continue;
                    }
                }
            }
            return null;
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    public XmlNode read() throws DataException {
        try {
            XmlNode node = this.pop();
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
        if (prefix == null) {
            prefix = "";
        }
        else if (!prefix.endsWith(".")) {
            prefix += ".";
        }
        if (this.reader != null) {
            if (this.reader.hasText()) {
                exception.set(prefix + "XmlNodeReader.textStart", this.reader.getTextStart());
                exception.set(prefix + "XmlNodeReader.textLength", this.reader.getTextLength());
                exception.set(prefix + "XmlNodeReader.textCharacters", new String(this.reader.getTextCharacters()));
                exception.set(prefix + "XmlNodeReader.text", this.reader.getText());
            }
            final Location location = this.reader.getLocation();
            if (location != null) {
                exception.set(prefix + "XmlNodeReader.location.characterOffset", location.getCharacterOffset());
                exception.set(prefix + "XmlNodeReader.location.columnNumber", location.getColumnNumber());
                exception.set(prefix + "XmlNodeReader.location.lineNumber", location.getLineNumber());
                exception.set(prefix + "XmlNodeReader.location.publicId", location.getPublicId());
                exception.set(prefix + "XmlNodeReader.location.systemId", location.getSystemId());
            }
            exception.set(prefix + "XmlNodeReader.encoding", this.reader.getEncoding());
            if (this.reader.isStartElement() || this.reader.isEndElement()) {
                exception.set(prefix + "XmlNodeReader.name", this.reader.getName());
            }
            try {
                exception.set(prefix + "XmlNodeReader.localName", this.reader.getLocalName());
            }
            catch (IllegalStateException e) {
                exception.set(prefix + "XmlNodeReader.localName", (Object)null);
            }
            exception.set(prefix + "XmlNodeReader.version", this.reader.getVersion());
        }
        if (this.currentNode != null) {
            exception.set(prefix + "XmlNodeReader.currentNode", this.currentNode);
        }
        return exception;
    }
}
