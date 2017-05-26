package com.northconcepts.datapipeline.transform.lookup;

import java.util.List;

import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.internal.lang.Util;
import com.northconcepts.datapipeline.transform.Transformer;

public class LookupTransformer extends Transformer
{
    private final FieldList fields;
    private final Lookup lookup;
    private final boolean overwriteFields;
    
    public LookupTransformer(final FieldList fields, final Lookup lookup, final boolean overwriteFields) {
        super();
        this.fields = fields;
        this.lookup = lookup;
        this.overwriteFields = overwriteFields;
    }
    
    public LookupTransformer(final FieldList fields, final Lookup lookup) {
        this(fields, lookup, false);
    }
    
    public FieldList getFields() {
        return this.fields;
    }
    
    public Lookup getLookup() {
        return this.lookup;
    }
    
    public boolean isOverwriteFields() {
        return this.overwriteFields;
    }
    
    @Override
	public boolean transform(final Record record) throws Throwable {
        final List<?> arguments = record.getValues(this.fields);
        final RecordList list = this.lookup.get(arguments);
        this.join(record, list, arguments);
        return true;
    }
    
    protected void join(final Record originalRecord, RecordList lookupResult, final List<?> lookupArguments) {
        if (lookupResult == null || lookupResult.getRecordCount() == 0) {
            lookupResult = this.noResults(originalRecord, lookupArguments);
        }
        else if (lookupResult.getRecordCount() > 1) {
            lookupResult = this.tooManyResults(originalRecord, lookupArguments, lookupResult);
        }
        if (lookupResult != null && lookupResult.getRecordCount() > 0) {
            originalRecord.copyFrom(lookupResult.get(0), this.overwriteFields);
        }
    }
    
    protected RecordList noResults(final Record originalRecord, final List<?> arguments) {
        throw this.getReader().exception("no results for lookup").set("LookupTransformer.arguments", arguments).set("LookupTransformer.argumentsAsCsv", Util.arrayToString(arguments, ",")).set("LookupTransformer.fields", this.fields).setRecord(originalRecord);
    }
    
    protected RecordList tooManyResults(final Record originalRecord, final List<?> arguments, final RecordList lookupResult) {
        throw this.getReader().exception("too many results for lookup").set("LookupTransformer.arguments", arguments).set("LookupTransformer.argumentsAsCsv", Util.arrayToString(arguments, ",")).set("LookupTransformer.fields", this.fields).set("LookupTransformer.resultCount", lookupResult.getRecordCount()).setRecord(originalRecord);
    }
    
    @Override
	public String toString() {
        return this.lookup + " using field(s) " + this.fields;
    }
}
