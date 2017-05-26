package com.northconcepts.datapipeline.json.builder;

import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class JsonArray extends JsonValue
{
    @Override
	public JsonNodeType getType() {
        return JsonNodeType.ARRAY;
    }
    
    @Override
	protected void writeStartImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
        generator.writeStartArray();
    }
    
    @Override
	protected void writeEndImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
        generator.writeEndArray();
    }
    
    @Override
	public JsonObject object() {
        return super.object();
    }
    
    @Override
	public JsonArray array() {
        return super.array();
    }
    
    @Override
	public JsonArray value(final String valueExpression) {
        super.value(valueExpression);
        return this;
    }
}
