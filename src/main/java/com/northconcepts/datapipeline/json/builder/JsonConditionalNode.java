package com.northconcepts.datapipeline.json.builder;

import java.util.List;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class JsonConditionalNode extends JsonFragment
{
    private final JsonLogicalExpression condition;
    
    public JsonConditionalNode(final JsonNode parentNode, final String condition) {
        super(parentNode);
        this.condition = new JsonLogicalExpression(condition);
    }
    
    public JsonLogicalExpression getCondition() {
        return this.condition;
    }
    
    @Override
	public boolean hasNodes(final ExpressionContext context) {
        return super.hasNodes(context) && (this.condition == null || this.condition.isTrue(context));
    }
    
    @Override
	public List<JsonNode> getNodes(final ExpressionContext context) {
        if (this.hasNodes(context)) {
            return super.getNodes(context);
        }
        return null;
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
	public JsonConditionalNode field(final String nameExpression, final String valueExpression) {
        super.field(nameExpression, valueExpression);
        return this;
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
	public JsonConditionalNode value(final String valueExpression) {
        super.value(valueExpression);
        return this;
    }
    
    @Override
	protected DataException exception(final Throwable e) {
        return super.exception(e).set("condition", this.condition);
    }
}
