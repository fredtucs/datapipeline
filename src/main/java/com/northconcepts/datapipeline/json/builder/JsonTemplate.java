package com.northconcepts.datapipeline.json.builder;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.expression.DefaultExpressionContext;
import com.northconcepts.datapipeline.internal.expression.RecordExpressionContext;

public class JsonTemplate extends JsonFragment
{
    private final DefaultExpressionContext expressionContext;
    private final RecordExpressionContext recordExpressionContext;
    private final JsonNodeWriter nodeWriter;
    private JsonDetailMarker detail;
    
    public JsonTemplate() {
        super(null);
        this.expressionContext = new DefaultExpressionContext();
        this.recordExpressionContext = new RecordExpressionContext();
        this.nodeWriter = new JsonNodeWriter(this);
        this.recordExpressionContext.setParent(this.expressionContext);
    }
    
    public void setValue(final String name, final Object value) {
        this.expressionContext.setValue(name, value);
    }
    
    public void setValue(final String name, final Object value, final Class<?> type) {
        this.expressionContext.setValue(name, value, type);
    }
    
    public void buildHeader(final JsonGenerator jsonGenerator) throws IOException {
        JsonNode node;
        while ((node = this.nodeWriter.nextNode(this.expressionContext, jsonGenerator)) != null) {
            if (node.isMarker() && node instanceof JsonDetailMarker) {
                this.detail = (JsonDetailMarker)node;
                break;
            }
        }
    }
    
    public void buildDetail(final Record record, final JsonGenerator jsonGenerator) throws IOException {
        this.recordExpressionContext.setRecord(record);
        if (this.detail == null) {
            throw new DataException("no detail marker found, call detail() on JsonValue or subclass");
        }
        final JsonNodeWriter nodeWriter = new JsonNodeWriter(this.detail);
        while (nodeWriter.nextNode(this.recordExpressionContext, jsonGenerator) != null) {}
    }
    
    public void buildFooter(final JsonGenerator jsonGenerator) throws IOException {
        while (this.nodeWriter.nextNode(this.expressionContext, jsonGenerator) != null) {}
    }
    
    @Override
	public JsonObject object() {
        return this.add(new JsonObject());
    }
    
    @Override
	public JsonArray array() {
        return this.add(new JsonArray());
    }
}
