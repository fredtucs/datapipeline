package com.northconcepts.datapipeline.json.builder;

public abstract class JsonValue extends JsonNode
{
    @Override
	public JsonConditionalNode when(final String condition) {
        return super.when(condition);
    }
    
    @Override
	public JsonDetailMarker detail() {
        return super.detail();
    }
}
