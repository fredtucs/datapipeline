package com.northconcepts.datapipeline.json.builder;

public class JsonDetailMarker extends JsonFragment
{
    public JsonDetailMarker(final JsonNode parentNode) {
        super(parentNode);
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
	public JsonDetailMarker field(final String nameExpression, final String valueExpression) {
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
	public JsonDetailMarker value(final String valueExpression) {
        super.value(valueExpression);
        return this;
    }
    
    @Override
	public boolean isMarker() {
        return true;
    }
}
