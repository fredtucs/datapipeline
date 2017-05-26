package com.northconcepts.datapipeline.internal.javabean;

import java.util.Date;
import java.util.Map;
import java.util.Stack;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.javabean.iterator.ArrayIterator;
import com.northconcepts.datapipeline.internal.javabean.iterator.CollectionIterator;
import com.northconcepts.datapipeline.internal.javabean.iterator.JavaIterator;
import com.northconcepts.datapipeline.internal.javabean.iterator.JavaIteratorContext;
import com.northconcepts.datapipeline.internal.javabean.iterator.MapEntryIterator;
import com.northconcepts.datapipeline.internal.javabean.iterator.MapIterator;
import com.northconcepts.datapipeline.internal.javabean.iterator.PropertyIterator;
import com.northconcepts.datapipeline.internal.javabean.iterator.RootIterator;
import com.northconcepts.datapipeline.internal.javabean.iterator.SingleObjectIterator;

public class JavaStreamReader
{
    private JavaIterator iterator;
    private int depth;
    private int child;
    private Stack<JavaIteratorContext> iteratorStack;
    private JavaNode node;
    
    public JavaStreamReader(final String name, final Object object) {
        super();
        this.child = -1;
        this.iteratorStack = new Stack<JavaIteratorContext>();
        this.iterator = new RootIterator(name, object);
    }
    
    public JavaNode getNode() {
        return this.node;
    }
    
    public boolean next() {
        this.endNode();
        if (this.iterator.next()) {
            this.beginNode();
            final JavaIterator childIterator;
            if ((childIterator = iteratorFor(this.iterator.getValue())) != null) {
                this.pushIterator(childIterator);
                return this.next();
            }
            if (!(this.iterator instanceof SingleObjectIterator)) {
                this.pushIterator(new SingleObjectIterator(this.iterator.getName(), this.iterator.getValue()));
            }
            return true;
        }
        else {
            if (this.iteratorStack.size() > 0) {
                this.popIterator();
                return true;
            }
            return false;
        }
    }
    
    private void pushIterator(final JavaIterator childIterator) {
        this.iteratorStack.push(new JavaIteratorContext(this.iterator, this.depth, this.child));
        this.iterator = childIterator;
        this.child = -1;
        ++this.depth;
    }
    
    private void popIterator() {
        final JavaIteratorContext context = this.iteratorStack.pop();
        this.iterator = context.getIterator();
        this.child = context.getChild();
        --this.depth;
    }
    
    private void beginNode() {
        ++this.child;
        int eventType;
        if (this.iterator instanceof SingleObjectIterator) {
            eventType = 4;
        }
        else {
            eventType = 2;
        }
        this.node = new JavaNode(this.node, this.getName(), this.getValue(), this.depth, this.child, eventType);
    }
    
    private void endNode() {
        if (this.child >= 0 && this.node != null) {
            this.node = this.node.getParent();
        }
    }
    
    public String getName() {
        return this.iterator.getName();
    }
    
    public Object getValue() {
        if (isSingleValued(this.iterator.getValue())) {
            return this.iterator.getValue();
        }
        return this.iterator.getName();
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public static JavaIterator iteratorFor(final Object object) {
        try {
            if (object == null) {
                return null;
            }
            if (object.getClass().isArray()) {
                return new ArrayIterator(object);
            }
            if (object instanceof Iterable) {
                return new CollectionIterator((Iterable<?>)object);
            }
            if (object instanceof Map) {
                return new MapIterator((Map<?, ?>)object);
            }
            if (object instanceof Map.Entry) {
                return new MapEntryIterator((Map.Entry<?, ?>)object);
            }
            if (isSingleValued(object)) {
                return null;
            }
            return new PropertyIterator(object);
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    public static boolean isSingleValued(final Object object) {
        if (object == null) {
            return true;
        }
        final Class<?> type = object.getClass();
        return type.isPrimitive() || object instanceof Number || object instanceof CharSequence || object instanceof Date || object instanceof Boolean || object instanceof Character || object instanceof Void;
    }
}
