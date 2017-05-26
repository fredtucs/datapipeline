package com.northconcepts.datapipeline.memory;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;

public class MemoryReader extends DataReader
{
    private final RecordList recordList;
    private int nextRecordIndex;
    
    public MemoryReader(final RecordList recordSet) {
        super();
        this.recordList = recordSet;
    }
    
    public RecordList getRecordList() {
        return this.recordList;
    }
    
    @Override
	public void open() {
        super.open();
        this.nextRecordIndex = 0;
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        if (this.nextRecordIndex >= this.recordList.getRecordCount()) {
            return null;
        }
        return (Record)this.recordList.get(this.nextRecordIndex++).clone();
    }
    
    @Override
	public int available() {
        return this.recordList.getRecordCount() + super.available() - this.nextRecordIndex;
    }
}
