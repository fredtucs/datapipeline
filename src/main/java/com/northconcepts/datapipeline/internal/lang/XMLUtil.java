package com.northconcepts.datapipeline.internal.lang;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.northconcepts.datapipeline.core.DataException;

public final class XMLUtil
{
    private static final ThreadLocal<XMLUtil> threadLocal;
    private final DocumentBuilderFactory documentBuilderFactory;
    private final TransformerFactory transformerFactory;
    private final DocumentBuilder documentBuilder;
    private final Transformer transformer;
    private final StringWriter stringWriter;
    private final StreamResult result;
    
    public static Document newDocument() throws ParserConfigurationException, TransformerConfigurationException {
        return getThreadLocalInstance().getDocumentBuilder().newDocument();
    }
    
    public static String nodeToString(final Node node) throws TransformerException {
        return getThreadLocalInstance().nodeToString0(node);
    }
    
    public static String escapeXml(final String source) {
        if (Util.isEmpty(source)) {
            return source;
        }
        final StringBuilder s = new StringBuilder(source.length() + 25);
        for (int i = 0; i < source.length(); ++i) {
            final char c = source.charAt(i);
            switch (c) {
                case '\"': {
                    s.append("&quot;");
                    break;
                }
                case '&': {
                    s.append("&amp;");
                    break;
                }
                case '<': {
                    s.append("&lt;");
                    break;
                }
                case '>': {
                    s.append("&gt;");
                    break;
                }
                case '\'': {
                    s.append("&apos;");
                    break;
                }
                default: {
                    s.append(c);
                    break;
                }
            }
        }
        return s.toString();
    }
    
    public static final XMLUtil getThreadLocalInstance() {
        return XMLUtil.threadLocal.get();
    }
    
    private XMLUtil() {
        super();
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.transformerFactory = TransformerFactory.newInstance();
        this.stringWriter = new StringWriter(8192);
        this.result = new StreamResult(this.stringWriter);
        try {
            this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
            this.transformer = this.transformerFactory.newTransformer();
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    public DocumentBuilderFactory getDocumentBuilderFactory() {
        return this.documentBuilderFactory;
    }
    
    public TransformerFactory getTransformerFactory() {
        return this.transformerFactory;
    }
    
    public DocumentBuilder getDocumentBuilder() {
        return this.documentBuilder;
    }
    
    public Transformer getTransformer() {
        return this.transformer;
    }
    
    public StreamResult getResult() {
        return this.result;
    }
    
    private String nodeToString0(final Node node) throws TransformerException {
        final DOMSource source = new DOMSource(node);
        this.transformer.transform(source, this.result);
        final String string = this.stringWriter.toString();
        this.stringWriter.getBuffer().setLength(0);
        return string;
    }
    
    static {
        threadLocal = new ThreadLocal<XMLUtil>() {
            @Override
			protected XMLUtil initialValue() {
                return new XMLUtil();
            }
        };
    }
}
