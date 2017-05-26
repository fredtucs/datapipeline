package com.northconcepts.datapipeline.transform.lookup;

import java.util.HashMap;
import java.util.List;

import com.northconcepts.datapipeline.core.CompositeValue;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.FieldList;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;

public class DataReaderLookup extends Lookup
{
    private DataReader reader;
    private final HashMap<CompositeValue, RecordList> table;
    private final FieldList parameterFields;
    private final FieldList resultFields;
    
    public DataReaderLookup(final DataReader reader, final FieldList parameterFields) {
        this(reader, parameterFields, null);
    }
    
    public DataReaderLookup(final DataReader reader, final FieldList parameterFields, final FieldList resultFields) {
        super();
        this.table = new HashMap<CompositeValue, RecordList>();
        this.reader = reader;
        this.parameterFields = parameterFields;
        this.resultFields = resultFields;
        this.init();
    }
    
    private void init() {
        this.reader.open();
        try {
            Record record;
            while ((record = this.reader.read()) != null) {
                final List<?> parameters = record.getValues(this.parameterFields);
                if (this.resultFields != null) {
                    record.selectFields(this.resultFields);
                }
                this.put(parameters.toArray(), record);
            }
        }
        finally {
            this.reader.close();
            this.reader = null;
        }
    }
    
    private void put(final Object[] parameters, final Record result) {
        final CompositeValue key = new CompositeValue(parameters);
        RecordList list = this.table.get(key);
        if (list == null) {
            list = new RecordList();
            this.table.put(key, list);
        }
        list.add(result);
    }
    
    @Override
	public RecordList get(final Object... keys) {
        return this.table.get(new CompositeValue(keys));
    }
}
