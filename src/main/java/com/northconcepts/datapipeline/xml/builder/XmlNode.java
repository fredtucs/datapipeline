package com.northconcepts.datapipeline.xml.builder;

import java.io.Writer;
import java.util.List;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public abstract class XmlNode
{
    protected abstract void writeStartImpl(final ExpressionContext p0, final Writer p1) throws Throwable;
    
    protected abstract void writeEndImpl(final ExpressionContext p0, final Writer p1) throws Throwable;
    
    public final void writeStart(final ExpressionContext context, final Writer writer) {
        try {
            this.writeStartImpl(context, writer);
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    public final void writeEnd(final ExpressionContext context, final Writer writer) {
        try {
            this.writeEndImpl(context, writer);
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    public boolean hasChildNodes(final ExpressionContext context) {
        return false;
    }
    
    public List<XmlNode> getChildNodes(final ExpressionContext context) {
        return null;
    }
    
    public boolean isMarker(final ExpressionContext context) {
        return false;
    }
}
