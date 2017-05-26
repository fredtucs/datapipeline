package com.northconcepts.datapipeline.json.builder;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public abstract class JsonNode
{
    private final List<JsonNode> nodes;
    
    public JsonNode() {
        super();
        this.nodes = new ArrayList<JsonNode>();
    }
    
    public abstract JsonNodeType getType();
    
    protected abstract void writeStartImpl(final ExpressionContext p0, final JsonGenerator p1) throws Throwable;
    
    protected abstract void writeEndImpl(final ExpressionContext p0, final JsonGenerator p1) throws Throwable;
    
    public final void writeStart(final ExpressionContext context, final JsonGenerator jsonGenerator) {
        try {
            this.writeStartImpl(context, jsonGenerator);
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public final void writeEnd(final ExpressionContext context, final JsonGenerator jsonGenerator) {
        try {
            this.writeEndImpl(context, jsonGenerator);
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    protected DataException exception(final Throwable e) {
        return DataException.wrap(e).set("type", this.getType()).set("class", this.getClass()).set("marker", this.isMarker()).set("nodes", this.nodes);
    }
    
    public List<JsonNode> getNodes(final ExpressionContext context) {
        return this.nodes;
    }
    
    public boolean hasNodes(final ExpressionContext context) {
        return this.nodes.size() > 0;
    }
    
    public boolean isMarker() {
        return false;
    }
    
    protected final <T extends JsonNode> T add(final T node) {
        this.nodes.add(node);
        return node;
    }
    
    protected JsonObject object() {
        this.assertType(JsonNodeType.ARRAY, "anonymous object can only be added to an array");
        return this.add(new JsonObject());
    }
    
    protected JsonArray array() {
        this.assertType(JsonNodeType.ARRAY, "anonymous array can only be added to an array");
        return this.add(new JsonArray());
    }
    
    protected JsonNode value(final String valueExpression) {
        this.assertType(JsonNodeType.ARRAY, "primitive value can only be added to an array");
        return this.add(new JsonPrimitive(valueExpression));
    }
    
    protected JsonObject object(final String nameExpression) {
        this.assertType(JsonNodeType.OBJECT, "object field can only be added to an object");
        final JsonObject object = new JsonObject();
        this.add(new JsonField(nameExpression, object));
        return object;
    }
    
    protected JsonArray array(final String nameExpression) {
        this.assertType(JsonNodeType.OBJECT, "array field can only be added to an object");
        final JsonArray array = new JsonArray();
        this.add(new JsonField(nameExpression, array));
        return array;
    }
    
    protected JsonNode field(final String nameExpression, final String valueExpression) {
        this.assertType(JsonNodeType.OBJECT, "primitive field can only be added to an object");
        return this.add(new JsonField(nameExpression, new JsonPrimitive(valueExpression)));
    }
    
    protected JsonConditionalNode when(final String condition) {
        return this.add(new JsonConditionalNode(this, condition));
    }
    
    protected JsonDetailMarker detail() {
        return this.add(new JsonDetailMarker(this));
    }
    
    protected void assertType(final JsonNodeType type, final String message) {
        if (this.getType() != type) {
            throw new DataException("JSON type mismatch: " + message).set("expectedJsonNodeType", type).set("foundJsonNodeType", this.getType());
        }
    }
}
