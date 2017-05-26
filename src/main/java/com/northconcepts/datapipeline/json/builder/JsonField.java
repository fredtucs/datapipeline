package com.northconcepts.datapipeline.json.builder;

import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class JsonField extends JsonNode
{
    private final JsonExpression nameExpression;
    private final JsonValue value;
    
    public JsonField(final String nameExpression, final JsonValue value) {
        super();
        this.nameExpression = new JsonExpression(nameExpression);
        this.add(this.value = value);
    }
    
    @Override
	public JsonNodeType getType() {
        return JsonNodeType.FIELD;
    }
    
    @Override
	protected void writeStartImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
        final Object evaluatedNameExpression = this.nameExpression.evaluate(context);
        try {
            generator.writeFieldName(evaluatedNameExpression.toString());
        }
        catch (Throwable e) {
            throw DataException.wrap(e).set("evaluatedNameExpression", evaluatedNameExpression);
        }
    }
    
    @Override
	protected void writeEndImpl(final ExpressionContext context, final JsonGenerator generator) throws Throwable {
    }
    
    @Override
	protected DataException exception(final Throwable e) {
        return super.exception(e).set("nameExpression", this.nameExpression).set("value", this.value);
    }
}
