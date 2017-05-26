package com.northconcepts.datapipeline.internal.lang.reflect;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.northconcepts.datapipeline.internal.lang.XMLUtil;

public class XMLReflection implements ReflectionHandler
{
    private static final Class<Object> OBJECT_CLASS;
    private Document document;
    private Element element;
    private String string;
    private String rootName;
    
    public XMLReflection(final String rootName) {
        super();
        this.rootName = rootName;
    }
    
    public void startReflection(final Object object) throws Throwable {
        this.document = XMLUtil.newDocument();
        this.element = (Element)this.document.appendChild(this.document.createElement(this.rootName));
    }
    
    public void endReflection(final Object object) throws Throwable {
        this.string = XMLUtil.nodeToString(this.document);
        this.document = null;
    }
    
    @Override
	public String toString() {
        return this.string;
    }
    
    public void enterClass(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex) throws Throwable {
    }
    
    public void leaveClass(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex) throws Throwable {
    }
    
    public void enterField(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final boolean previouslyVisited) throws Throwable {
        if (!previouslyVisited && (object == null || objectType == XMLReflection.OBJECT_CLASS || ObjectReflector.isPrimitive(objectType))) {
            (this.element = (Element)this.element.appendChild(this.document.createElement(objectName))).appendChild(this.document.createCDATASection((object == null) ? "" : object.toString()));
        }
    }
    
    public void leaveField(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final boolean previouslyVisited) throws Throwable {
        if (!previouslyVisited && (object == null || objectType == XMLReflection.OBJECT_CLASS || ObjectReflector.isPrimitive(objectType))) {
            final Node node = this.element.getParentNode();
            if (node instanceof Element) {
                this.element = (Element)node;
            }
        }
    }
    
    public void enterArrayElement(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final int arrayLength, final Object array) throws Throwable {
        if (object != null && objectType != XMLReflection.OBJECT_CLASS && !ObjectReflector.isPrimitive(objectType)) {
            this.element = (Element)this.element.appendChild(this.document.createElement(objectName));
        }
    }
    
    public void leaveArrayElement(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final int arrayLength, final Object array) throws Throwable {
        if (object != null && objectType != XMLReflection.OBJECT_CLASS && !ObjectReflector.isPrimitive(objectType)) {
            final Node node = this.element.getParentNode();
            if (node instanceof Element) {
                this.element = (Element)node;
            }
        }
    }
    
    static {
        OBJECT_CLASS = Object.class;
    }
}
