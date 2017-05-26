package com.northconcepts.datapipeline.json.builder;

import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class JsonFragment extends JsonNode
{
    private final JsonNode parentNode;
    
    public JsonFragment(final JsonNode parentNode) {
        super();
        this.parentNode = parentNode;
    }
    
    public JsonNode getParentNode() {
        return this.parentNode;
    }
    
    @Override
	public JsonNodeType getType() {
        if (this.parentNode == null) {
            return null;
        }
        return this.parentNode.getType();
    }
    
    @Override
	protected void writeStartImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
    }
    
    @Override
	protected void writeEndImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
    }
    
    @Override
	public JsonConditionalNode when(final String condition) {
        return super.when(condition);
    }
    
    @Override
	public JsonDetailMarker detail() {
        return super.detail();
    }
    
    @Override
	protected DataException exception(final Throwable e) {
        return super.exception(e).set("parentNode", this.parentNode);
    }
}
