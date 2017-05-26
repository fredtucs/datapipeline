package com.northconcepts.datapipeline.xml.builder;

import java.util.List;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class XmlDetailMarker extends XmlFragment
{
    @Override
	public boolean hasChildNodes(final ExpressionContext context) {
        return super.hasChildNodes(context);
    }
    
    @Override
	public List<XmlNode> getChildNodes(final ExpressionContext context) {
        return super.getChildNodes(context);
    }
    
    @Override
	public boolean isMarker(final ExpressionContext context) {
        return true;
    }
}
