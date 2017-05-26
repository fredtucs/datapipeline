package com.northconcepts.datapipeline.internal.expression;

import java.util.Date;

import com.northconcepts.datapipeline.core.FieldDeclarations;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.Moment;

public class RecordExpressionContext extends HierarchicalExpressionContext
{
    private final FieldDeclarations fieldDeclarations;
    private Record record;
    
    public RecordExpressionContext(final FieldDeclarations fieldDeclarations) {
        super();
        this.fieldDeclarations = fieldDeclarations;
    }
    
    public RecordExpressionContext() {
        super();
        this.fieldDeclarations = new FieldDeclarations();
    }
    
    public FieldDeclarations getFieldDeclarations() {
        return this.fieldDeclarations;
    }
    
    public Record getRecord() {
        return this.record;
    }
    
    public void setRecord(final Record record) {
        this.record = record;
    }
    
    @Override
	protected Class<?> getTypeImpl(final String name) {
        Class<?> type = this.getType0(name);
        if (type != null && Date.class.isAssignableFrom(type)) {
            type = Expression.MOMENT_TYPE;
        }
        return type;
    }
    
    private Class<?> getType0(final String name) {
        final Class<?> type = this.fieldDeclarations.get(name);
        if (type != null) {
            return type;
        }
        final Object value = Helper.getValue(this, name);
        if (value != null) {
            return value.getClass();
        }
        return Expression.OBJECT_TYPE;
    }
    
    @Override
	protected Object getValueImpl(final String name) {
        Object value = this.record.getField(name).getValue();
        if (value instanceof Date) {
            final Date d = (Date)value;
            value = new Moment(d);
        }
        return value;
    }
    
    @Override
	protected boolean containsKeyImpl(final String name) {
        return this.record.containsField(name);
    }
}
