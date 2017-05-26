package com.northconcepts.datapipeline.internal.xpath;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;

import com.northconcepts.datapipeline.core.DataException;

public class XmlNode
{
    private static final AtomicLong nextSequence;
    private final long sequence;
    private final XmlNode parent;
    private final List<Namespace> namespaces;
    private final int eventType;
    private final NodeType type;
    private final QName name;
    private final List<Attribute> attributes;
    private String text;
    private final int depth;
    private final int position;
    private final Location location;
    private int childCount;
    
    protected XmlNode(final XmlNode parent, final int eventType, final QName name, final String text, final int depth, final int position, final Location location) {
        super();
        this.sequence = XmlNode.nextSequence.incrementAndGet();
        this.namespaces = new ArrayList<Namespace>();
        this.attributes = new ArrayList<Attribute>();
        this.parent = parent;
        this.eventType = eventType;
        this.type = NodeType.lookupEventType(eventType);
        this.name = name;
        this.text = text;
        this.depth = depth;
        this.position = position;
        this.location = location;
    }
    
    public XmlNode(final XmlNode parent, final XMLStreamReader reader) {
        super();
        this.sequence = XmlNode.nextSequence.incrementAndGet();
        this.namespaces = new ArrayList<Namespace>();
        this.attributes = new ArrayList<Attribute>();
        this.parent = parent;
        this.eventType = reader.getEventType();
        this.type = NodeType.lookupEventType(this.eventType);
        this.name = this.type.getName(reader);
        this.text = this.type.getText(reader);
        this.depth = ((parent == null) ? 0 : (parent.getDepth() + 1));
        this.position = ((parent == null) ? 0 : parent.getChildCount());
        if (parent != null) {
            parent.incrementChildCount();
        }
        this.location = reader.getLocation();
        if (this.type == NodeType.NODE) {
            for (int attributeCount = reader.getAttributeCount(), i = 0; i < attributeCount; ++i) {
                this.attributes.add(new Attribute(reader.getAttributeName(i), reader.getAttributeValue(i)));
            }
            for (int namespaceCount = reader.getNamespaceCount(), j = 0; j < namespaceCount; ++j) {
                this.namespaces.add(new Namespace(reader.getNamespacePrefix(j), reader.getNamespaceURI(j)));
            }
        }
    }
    
    public long getSequence() {
        return this.sequence;
    }
    
    public XmlNode textAsChildNode() {
        return new XmlNode(this, 4, null, this.getText(), this.getDepth() + 1, this.getChildCount() + 1, this.getLocation());
    }
    
    public XmlNode getParent() {
        return this.parent;
    }
    
    public boolean hasParent() {
        return this.parent != null;
    }
    
    public boolean hasGrandParent() {
        return this.parent != null && this.parent.parent != null;
    }
    
    public void assertHasParent() {
        if (!this.hasParent()) {
            throw new DataException("node has no parent").set("node", this);
        }
    }
    
    public void assertHasGrandParent() {
        if (!this.hasGrandParent()) {
            throw new DataException("node has no grand parent").set("node", this).set("parent", this.parent);
        }
    }
    
    public List<Namespace> getNamespaces() {
        return this.namespaces;
    }
    
    public int getEventType() {
        return this.eventType;
    }
    
    public NodeType getType() {
        return this.type;
    }
    
    public QName getName() {
        return this.name;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public List<Attribute> getAttributes() {
        return this.attributes;
    }
    
    public Attribute getAttribute(final QName name) {
        for (int i = 0; i < this.attributes.size(); ++i) {
            final Attribute attribute = this.attributes.get(i);
            if (QNameComparator.matches(name, attribute.getName())) {
                return attribute;
            }
        }
        return null;
    }
    
    public String getPath() {
        String s = "";
        if (this.parent != null) {
            s += this.parent.getPath();
        }
        s += "/";
        if (this.name != null) {
            s += this.name.getLocalPart();
        }
        if (this.type != null) {
            s = s + ":" + this.type.getLiteral();
        }
        return s;
    }
    
    @Override
	public String toString() {
        String s = this.getPath();
        s = s + "#" + this.sequence;
        for (int i = 0; i < this.attributes.size(); ++i) {
            s = s + " @" + this.attributes.get(i);
        }
        if (this.text != null) {
            s = s + "  \"" + this.text + "\"";
        }
        return s;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void addText(final String text) {
        final String t = text.trim();
        if (t.length() > 0 && !t.equals("\r") && !t.equals("\n") && !t.equals("\r\n")) {
            if (this.text == null) {
                this.text = text;
            }
            else {
                this.text += text;
            }
        }
        if (this.parent != null) {
            this.parent.addText(text);
        }
    }
    
    public int getChildCount() {
        return this.childCount;
    }
    
    public void incrementChildCount() {
        ++this.childCount;
    }
    
    public Object getValue() {
        return this.getText();
    }
    
    static {
        nextSequence = new AtomicLong();
    }
}
