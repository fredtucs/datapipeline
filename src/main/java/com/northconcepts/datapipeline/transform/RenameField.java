package com.northconcepts.datapipeline.transform;

import com.northconcepts.datapipeline.core.Record;

public class RenameField extends Transformer
{
    private final String oldName;
    private final String newName;
    
    public RenameField(final String oldName, final String newName) {
        super();
        this.oldName = oldName;
        this.newName = newName;
    }
    
    public String getOldName() {
        return this.oldName;
    }
    
    public String getNewName() {
        return this.newName;
    }
    
    @Override
	public boolean transform(final Record record) throws Throwable {
        if (record.containsField(this.newName)) {
            throw this.getReader().exception("cannot rename; a field with the new name already exists").set("oldName", this.oldName).set("newName", this.newName);
        }
        record.getField(this.oldName).setName(this.newName);
        return true;
    }
    
    @Override
	public String toString() {
        return "renaming " + this.getOldName() + " to " + this.getNewName();
    }
}
