package com.northconcepts.datapipeline.core;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

public class RecordComparator implements Comparator<Record>, Cloneable
{
    static final RecordComparator DEFAULT;
    private final ArrayList<FieldComparator> fieldComparators;
    protected boolean cacheFieldIndexes;
    
    public RecordComparator() {
        super();
        this.fieldComparators = new ArrayList<FieldComparator>();
    }
    
    public RecordComparator(final RecordComparator o) {
        super();
        this.fieldComparators = new ArrayList<FieldComparator>();
        for (int i = 0; i < o.getCount(); ++i) {
            this.fieldComparators.add((FieldComparator)o.get(i).clone());
        }
        this.cacheFieldIndexes = o.cacheFieldIndexes;
    }
    
    @Override
	public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw DataException.wrap(e);
        }
    }
    
    public RecordComparator add(final String fieldName) {
        this.fieldComparators.add(new FieldComparator().setFieldName(fieldName));
        return this;
    }
    
    public RecordComparator add(final String fieldName, final boolean ascending) {
        this.fieldComparators.add(new FieldComparator().setFieldName(fieldName).setAscending(ascending));
        return this;
    }
    
    public RecordComparator add(final String fieldName, final boolean ascending, final Collator collator) {
        this.fieldComparators.add(new FieldComparator().setFieldName(fieldName).setAscending(ascending).setCollator(collator));
        return this;
    }
    
    public int getCount() {
        return this.fieldComparators.size();
    }
    
    public FieldComparator get(final int index) {
        return this.fieldComparators.get(index);
    }
    
    public RecordComparator remove(final int index) {
        this.fieldComparators.remove(index);
        return this;
    }
    
    public RecordComparator removeAll() {
        this.fieldComparators.clear();
        return this;
    }
    
    public int compare(final Record record1, final Record record2) {
        if (record1 == record2) {
            return 0;
        }
        if (record1 == null) {
            return -1;
        }
        if (record2 == null) {
            return 1;
        }
        if (this.getCount() > 0) {
            for (int i = 0; i < this.getCount(); ++i) {
                final int difference = this.get(i).compareRecord(record1, record2, this.cacheFieldIndexes);
                if (difference != 0) {
                    return difference;
                }
            }
        }
        else {
            for (int i = 0; i < record1.getFieldCount(); ++i) {
                final Field field1 = record1.getField(i);
                final String field1Name = field1.getName();
                final int field2Index = record2.indexOfField(field1Name, false);
                if (field2Index < 0) {
                    return 1;
                }
                final Field field2 = record2.getField(field2Index);
                final int difference2 = FieldComparator.DEFAULT.compare(field1, field2);
                if (difference2 != 0) {
                    return difference2;
                }
            }
            if (record1.getFieldCount() < record2.getFieldCount()) {
                return -1;
            }
        }
        return 0;
    }
    
    static {
        DEFAULT = new RecordComparator() {
            @Override
			public RecordComparator add(final String fieldName, final boolean ascending, final Collator collator) {
                throw new UnsupportedOperationException("Immutable instance");
            }
            
            @Override
			public RecordComparator add(final String fieldName, final boolean ascending) {
                throw new UnsupportedOperationException("Immutable instance");
            }
            
            @Override
			public RecordComparator add(final String fieldName) {
                throw new UnsupportedOperationException("Immutable instance");
            }
        };
    }
}
