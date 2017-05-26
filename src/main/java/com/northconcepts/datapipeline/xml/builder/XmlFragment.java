package com.northconcepts.datapipeline.xml.builder;

import java.io.Writer;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class XmlFragment extends XmlNodeContainer
{
    @Override
	protected void writeStartImpl(final ExpressionContext context, final Writer writer) throws Throwable {
    }
    
    @Override
	protected void writeEndImpl(final ExpressionContext context, final Writer writer) throws Throwable {
    }
}
