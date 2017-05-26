package com.northconcepts.datapipeline.json.builder;

import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class JsonPrimitive extends JsonValue
{
    private final JsonExpression valueExpression;
    
    public JsonPrimitive(final String valueExpression) {
        super();
        this.valueExpression = new JsonExpression(valueExpression);
    }
    
    @Override
	public JsonNodeType getType() {
        return JsonNodeType.PRIMITIVE;
    }
    
    @Override
	protected void writeStartImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
        final Object evaluatedValueExpression = this.valueExpression.evaluate(context);
        try {
            generator.writeObject(evaluatedValueExpression);
        }
        catch (Throwable e) {
            throw DataException.wrap(e).set("evaluatedValueExpression", evaluatedValueExpression);
        }
    }
    
    @Override
	protected void writeEndImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
    }
    
    @Override
	protected DataException exception(final Throwable e) {
        return super.exception(e).set("valueExpression", this.valueExpression);
    }
}
