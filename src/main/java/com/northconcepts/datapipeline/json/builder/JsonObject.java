package com.northconcepts.datapipeline.json.builder;

import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class JsonObject extends JsonValue
{
    @Override
	public JsonNodeType getType() {
        return JsonNodeType.OBJECT;
    }
    
    @Override
	protected void writeStartImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
        generator.writeStartObject();
    }
    
    @Override
	protected void writeEndImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
        generator.writeEndObject();
    }
    
    @Override
	public JsonObject object(final String nameExpression) {
        return super.object(nameExpression);
    }
    
    @Override
	public JsonArray array(final String nameExpression) {
        return super.array(nameExpression);
    }
    
    @Override
	public JsonObject field(final String nameExpression, final String valueExpression) {
        super.field(nameExpression, valueExpression);
        return this;
    }
}
