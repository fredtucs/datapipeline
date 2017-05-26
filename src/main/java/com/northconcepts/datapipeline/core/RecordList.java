package com.northconcepts.datapipeline.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.filter.Filter;

public class RecordList implements Cloneable
{
    public static final Logger log;
    private final ArrayList<Record> records;
    
    public RecordList() {
        super();
        this.records = new ArrayList<Record>();
    }
    
    public RecordList(final int initialCapacity) {
        super();
        this.records = new ArrayList<Record>(initialCapacity);
    }
    
    public RecordList(final Record... records) {
        this(records.length);
        this.add(records);
    }
    
    private RecordList(final RecordList recordSet) {
        this(recordSet, 0, recordSet.getRecordCount(), true);
    }
    
    private RecordList(final RecordList recordSet, final int fromIndex, final int toIndex, final boolean cloneRecords) {
        super();
        this.records = new ArrayList<Record>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; ++i) {
            Record record = recordSet.get(i);
            if (cloneRecords) {
                record = (Record)record.clone();
            }
            this.add(record);
        }
    }
    
    @Override
	public Object clone() {
        return new RecordList(this);
    }
    
    public List<Record> getRecords() {
        return this.records;
    }
    
    public int getRecordCount() {
        return this.records.size();
    }
    
    public Record get(final int index) {
        return this.records.get(index);
    }
    
    public RecordList add(final Record... records) {
        for (int i = 0; i < records.length; ++i) {
            this.records.add(records[i]);
        }
        return this;
    }
    
    public RecordList add(final int index, final Record record) {
        this.records.add(index, record);
        return this;
    }
    
    public RecordList set(final int index, final Record record) {
        this.records.set(index, record);
        return this;
    }
    
    public RecordList move(final int oldIndex, int newIndex) {
        final Record record = this.records.get(oldIndex);
        this.records.remove(oldIndex);
        if (newIndex > oldIndex) {
            --newIndex;
        }
        this.records.add(newIndex, record);
        return this;
    }
    
    public RecordList sublist(final int fromIndex, final int toIndex, final boolean cloneRecords) {
        return new RecordList(this, fromIndex, toIndex, cloneRecords);
    }
    
    public RecordList sublist(final int fromIndex, final boolean cloneRecords) {
        return new RecordList(this, fromIndex, this.getRecordCount() - fromIndex, cloneRecords);
    }
    
    public void clear() {
        this.records.clear();
    }
    
    public RecordList sort(RecordComparator comparator, final boolean cacheFieldIndexes) {
        if (cacheFieldIndexes) {
            comparator = new RecordComparator(comparator) {};
        }
        Collections.sort(this.records, comparator);
        return this;
    }
    
    public RecordList sort(final RecordComparator comparator) {
        return this.sort(comparator, false);
    }
    
    public int findFirst(final Filter filter, int from) {
        final int size = this.getRecordCount();
        if (from < 0) {
            from = 0;
        }
        for (int i = from; i < size; ++i) {
            final Record record = this.get(i);
            if (filter.allow(record)) {
                return i;
            }
        }
        return -1;
    }
    
    public int findLast(final Filter filter, int from) {
        final int size = this.getRecordCount();
        if (from == -1 || from >= size) {
            from = size - 1;
        }
        for (int i = from; i >= 0; --i) {
            final Record record = this.get(i);
            if (filter.allow(record)) {
                return i;
            }
        }
        return -1;
    }
    
    public RecordList findAll(final Filter filter) {
        final RecordList list = new RecordList();
        for (int size = this.getRecordCount(), i = 0; i < size; ++i) {
            final Record record = this.get(i);
            if (filter.allow(record)) {
                list.add(record);
            }
        }
        return list;
    }
    
    public long getSizeInBytes() {
        long size = 0L;
        final int recordCount = this.getRecordCount();
        if (recordCount > 0) {
            size += this.get(0).getSizeInBytesOfFieldNames();
        }
        for (int i = 0; i < recordCount; ++i) {
            size += this.get(i).getSizeInBytes();
        }
        return size;
    }
    
    static {
        log = DataEndpoint.log;
    }
}
