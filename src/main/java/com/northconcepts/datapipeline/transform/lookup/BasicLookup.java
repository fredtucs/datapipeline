package com.northconcepts.datapipeline.transform.lookup;

import java.util.HashMap;

import com.northconcepts.datapipeline.core.CompositeValue;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.internal.lang.Util;

public class BasicLookup extends Lookup
{
    private final FieldList resultFieldNames;
    private final HashMap<Object, RecordList> table;
    
    public BasicLookup(final FieldList resultFieldNames) {
        super();
        this.table = new HashMap<Object, RecordList>();
        this.resultFieldNames = resultFieldNames;
    }
    
    @Override
	public RecordList get(final Object... keys) {
        return this.get(new CompositeValue(keys), false);
    }
    
    public BasicLookup add(final Object key, final Object value) {
        this.addRecord(key, value);
        return this;
    }
    
    public BasicLookup add(final Object key, final Object[] values) {
        this.addRecord(key, values);
        return this;
    }
    
    public BasicLookup add(final Object[] key, final Object value) {
        this.addRecord(new CompositeValue(key), value);
        return this;
    }
    
    public BasicLookup add(final Object[] key, final Object[] values) {
        this.addRecord(new CompositeValue(key), values);
        return this;
    }
    
    private void addRecord(final Object key, final Object[] values) {
        if (this.resultFieldNames.size() != values.length) {
            throw new DataException("resultFieldNames count (" + this.resultFieldNames.size() + ") does not match value count (" + values.length + ")").set("DefaultLookup.resultFieldNames", this.resultFieldNames).set("DefaultLookup.values", Util.arrayToString(values, ","));
        }
        final Record record = new Record(this.resultFieldNames);
        for (int i = 0; i < values.length; ++i) {
            record.getField(i).setValue(values[i]);
        }
        this.get(key, true).add(record);
    }
    
    private void addRecord(final Object key, final Object value) {
        if (this.resultFieldNames.size() != 1) {
            throw new DataException("resultFieldNames count (" + this.resultFieldNames.size() + ") does not match value count (1)").set("DefaultLookup.resultFieldNames", this.resultFieldNames).set("DefaultLookup.values", value);
        }
        final Record record = new Record(this.resultFieldNames);
        record.getField(0).setValue(value);
        this.get(key, true).add(record);
    }
    
    private RecordList get(final Object key, final boolean create) {
        RecordList recordList = this.table.get(key);
        if (recordList == null && create) {
            recordList = new RecordList();
            this.table.put(key, recordList);
        }
        return recordList;
    }
}
