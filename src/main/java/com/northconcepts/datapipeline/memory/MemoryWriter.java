package com.northconcepts.datapipeline.memory;

import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;

public class MemoryWriter extends DataWriter
{
    private final RecordList recordList;
    
    public MemoryWriter() {
        super();
        this.recordList = new RecordList();
    }
    
    public MemoryWriter(final RecordList recordSet) {
        super();
        this.recordList = recordSet;
    }
    
    public RecordList getRecordList() {
        return this.recordList;
    }
    
    @Override
	protected void writeImpl(final Record record) throws Throwable {
        this.recordList.add(record);
    }
}
