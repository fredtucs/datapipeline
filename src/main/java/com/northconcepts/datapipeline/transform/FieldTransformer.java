package com.northconcepts.datapipeline.transform;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;

public abstract class FieldTransformer extends Transformer
{
    private final String name;
    private Field currentField;
    
    public FieldTransformer(final String name) {
        super();
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
	public boolean transform(final Record record) throws Throwable {
        final int index = record.indexOfField(this.name, true);
        this.transformField(this.currentField = record.getField(index));
        return true;
    }
    
    protected abstract void transformField(final Field p0) throws Throwable;
    
    @Override
	public String toString() {
        return "transforming " + this.getName();
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.currentField != null) {
            exception.set("FieldTransformer.field", this.currentField);
        }
        exception.setFieldName(this.name);
        return super.addExceptionProperties(exception);
    }
}
