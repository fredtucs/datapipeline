package com.northconcepts.datapipeline.json.builder;

import java.util.LinkedList;

import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

public class JsonNodeWriter
{
    private final LinkedList<IterationPointer> stack;
    private IterationPointer current;
    
    JsonNodeWriter(final JsonNode parent) {
        super();
        this.stack = new LinkedList<IterationPointer>();
        this.current = new IterationPointer(parent, 0);
    }
    
    public JsonNode nextNode(final ExpressionContext context, final JsonGenerator jsonGenerator) {
        if (this.current.node.hasNodes(context)) {
            JsonNode child = this.current.getNextChild(context);
            if (child != null) {
                if (!child.isMarker() && child.hasNodes(context)) {
                    child.writeStart(context, jsonGenerator);
                    this.stack.add(this.current);
                    this.current = new IterationPointer(child, 0);
                }
                else {
                    child.writeStart(context, jsonGenerator);
                    child.writeEnd(context, jsonGenerator);
                    final IterationPointer current = this.current;
                    ++current.nextChildIndex;
                }
                return child;
            }
            if (!this.stack.isEmpty()) {
                this.current = this.stack.removeLast();
                child = this.current.getNextChild(context);
                child.writeEnd(context, jsonGenerator);
                final IterationPointer current2 = this.current;
                ++current2.nextChildIndex;
                return this.nextNode(context, jsonGenerator);
            }
        }
        return null;
    }
    
    private static class IterationPointer
    {
        public JsonNode node;
        public int nextChildIndex;
        
        public IterationPointer(final JsonNode node, final int nextChildIndex) {
            super();
            this.node = node;
            this.nextChildIndex = nextChildIndex;
        }
        
        public JsonNode getNextChild(final ExpressionContext context) {
            if (this.node.hasNodes(context) && this.nextChildIndex < this.node.getNodes(context).size()) {
                return this.node.getNodes(context).get(this.nextChildIndex);
            }
            return null;
        }
    }
}
