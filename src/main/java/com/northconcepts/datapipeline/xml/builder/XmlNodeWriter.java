package com.northconcepts.datapipeline.xml.builder;

import java.io.Writer;
import java.util.LinkedList;

import com.northconcepts.datapipeline.internal.expression.ExpressionContext;

class XmlNodeWriter
{
    private final LinkedList<IterationPointer> stack;
    private IterationPointer current;
    
    XmlNodeWriter(final XmlNode parent) {
        super();
        this.stack = new LinkedList<IterationPointer>();
        this.current = new IterationPointer(parent, 0);
    }
    
    public XmlNode nextNode(final ExpressionContext context, final Writer writer) {
        if (this.current.node.hasChildNodes(context)) {
            XmlNode child = this.current.getNextChild(context);
            if (child != null) {
                if (!child.isMarker(context) && child.hasChildNodes(context)) {
                    child.writeStart(context, writer);
                    this.stack.add(this.current);
                    this.current = new IterationPointer(child, 0);
                }
                else {
                    child.writeStart(context, writer);
                    child.writeEnd(context, writer);
                    final IterationPointer current = this.current;
                    ++current.nextChildIndex;
                }
                return child;
            }
            if (!this.stack.isEmpty()) {
                this.current = this.stack.removeLast();
                child = this.current.getNextChild(context);
                child.writeEnd(context, writer);
                final IterationPointer current2 = this.current;
                ++current2.nextChildIndex;
                return this.nextNode(context, writer);
            }
        }
        return null;
    }
    
    private static class IterationPointer
    {
        public XmlNode node;
        public int nextChildIndex;
        
        public IterationPointer(final XmlNode node, final int nextChildIndex) {
            super();
            this.node = node;
            this.nextChildIndex = nextChildIndex;
        }
        
        public XmlNode getNextChild(final ExpressionContext context) {
            if (this.node.hasChildNodes(context) && this.nextChildIndex < this.node.getChildNodes(context).size()) {
                return this.node.getChildNodes(context).get(this.nextChildIndex);
            }
            return null;
        }
    }
}
