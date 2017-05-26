package com.northconcepts.datapipeline.transform;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;

public class CopyField extends Transformer
{
    private final String sourceFieldName;
    private final String targetFieldName;
    private final boolean overwrite;
    
    public CopyField(final String sourceFieldName, final String targetFieldName, final boolean overwrite) {
        super();
        this.sourceFieldName = sourceFieldName;
        this.targetFieldName = targetFieldName;
        this.overwrite = overwrite;
    }
    
    public String getSourceFieldName() {
        return this.sourceFieldName;
    }
    
    public String getTargetFieldName() {
        return this.targetFieldName;
    }
    
    public boolean getOverwrite() {
        return this.overwrite;
    }
    
    @Override
	public boolean transform(final Record record) throws Throwable {
        final Field sourceField = record.getField(this.sourceFieldName);
        Field targetField;
        if (this.overwrite) {
            targetField = record.getField(this.targetFieldName, true);
        }
        else {
            if (record.containsField(this.targetFieldName)) {
                return true;
            }
            targetField = record.addField().setName(this.targetFieldName);
        }
        targetField.copyFrom(sourceField, false);
        return true;
    }
    
    @Override
	public String toString() {
        return "copying " + this.getSourceFieldName() + " to " + this.getTargetFieldName();
    }
}
