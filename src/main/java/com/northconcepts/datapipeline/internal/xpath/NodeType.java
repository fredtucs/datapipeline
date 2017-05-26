package com.northconcepts.datapipeline.internal.xpath;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.lang.Util;

public enum NodeType
{
    COMMENT("comment", true) {
        @Override
		public QName getName(final XMLStreamReader reader) {
            return null;
        }
        
        @Override
		public String getText(final XMLStreamReader reader) {
            return reader.getText();
        }
    }, 
    TEXT("text", true) {
        @Override
		public QName getName(final XMLStreamReader reader) {
            return null;
        }
        
        @Override
		public String getText(final XMLStreamReader reader) {
            return reader.getText();
        }
    }, 
    PROCESSING_INSTRUCTION("processing-instruction", true) {
        @Override
		public QName getName(final XMLStreamReader reader) {
            return new QName(reader.getPITarget());
        }
        
        @Override
		public String getText(final XMLStreamReader reader) {
            return reader.getPIData();
        }
    }, 
    NODE("node", true) {
        @Override
		public QName getName(final XMLStreamReader reader) {
            return reader.getName();
        }
        
        @Override
		public String getText(final XMLStreamReader reader) {
            return null;
        }
    };
    
    private final String literal;
    private final boolean supported;
    
    private NodeType(final String value, final boolean supported) {
        this.literal = value;
        this.supported = supported;
    }
    
    public String getLiteral() {
        return this.literal;
    }
    
    public boolean isSupported() {
        return this.supported;
    }
    
    public abstract QName getName(final XMLStreamReader p0);
    
    public abstract String getText(final XMLStreamReader p0);
    
    public static NodeType lookup(final String value) {
        final NodeType[] arr$;
        final NodeType[] values = arr$ = values();
        for (final NodeType nodeType : arr$) {
            if (Util.matches(value, nodeType.literal, false, false)) {
                return nodeType;
            }
        }
        throw new DataException("no matching node type found").set("node type", value);
    }
    
    public static NodeType lookupEventType(final int eventType) {
        switch (eventType) {
            case 1:
            case 2:
            case 7:
            case 8: {
                return NodeType.NODE;
            }
            case 4:
            case 12: {
                return NodeType.TEXT;
            }
            case 5: {
                return NodeType.COMMENT;
            }
            case 3: {
                return NodeType.PROCESSING_INSTRUCTION;
            }
            default: {
                DataEndpoint.log.debug("  --unhandled Event Type: " + eventType);
                return null;
            }
        }
    }
}
