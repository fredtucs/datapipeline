package com.northconcepts.datapipeline.core;

import java.text.Collator;
import java.util.Comparator;

public class FieldComparator implements Comparator<Field>, Cloneable
{
    static final FieldComparator DEFAULT;
    private String fieldName;
    private int fieldIndex;
    private boolean ascending;
    private Collator collator;
    
    public FieldComparator() {
        super();
        this.fieldIndex = -1;
        this.ascending = true;
    }
    
    public FieldComparator copyFrom(final FieldComparator o) {
        this.fieldName = o.fieldName;
        this.fieldIndex = o.fieldIndex;
        this.ascending = o.ascending;
        this.collator = o.collator;
        return this;
    }
    
    public boolean isAscending() {
        return this.ascending;
    }
    
    public FieldComparator setAscending(final boolean ascending) {
        this.ascending = ascending;
        return this;
    }
    
    public Collator getCollator() {
        return this.collator;
    }
    
    public FieldComparator setCollator(final Collator collator) {
        this.collator = collator;
        return this;
    }
    
    public String getFieldName() {
        return this.fieldName;
    }
    
    public FieldComparator setFieldName(final String fieldName) {
        this.fieldName = fieldName;
        return this;
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
    
    private int applySortDirection(final int difference) {
        return difference * (this.ascending ? 1 : -1);
    }
    
    public int compare(final Field o1, final Field o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return this.applySortDirection(-1);
        }
        if (o2 == null) {
            return this.applySortDirection(1);
        }
        return this.applySortDirection(o1.compareTo(o2, this.collator));
    }
    
    public int compareRecord(final Record record1, final Record record2, final boolean cacheFieldIndexes) {
        Field field1;
        Field field2;
        if (cacheFieldIndexes) {
            if (this.fieldIndex < 0) {
                this.fieldIndex = record1.indexOfField(this.fieldName, true);
            }
            field1 = record1.getField(this.fieldIndex);
            field2 = record2.getField(this.fieldIndex);
        }
        else {
            field1 = record1.getField(this.fieldName);
            field2 = record2.getField(this.fieldName);
        }
        return this.compare(field1, field2);
    }
    
    static {
        DEFAULT = new FieldComparator() {
            @Override
			public FieldComparator setAscending(final boolean ascending) {
                throw new UnsupportedOperationException("Immutable instance");
            }
            
            @Override
			public FieldComparator setFieldName(final String fieldName) {
                throw new UnsupportedOperationException("Immutable instance");
            }
            
            @Override
			public int compareRecord(final Record record1, final Record record2, final boolean cacheFieldIndexes) {
                throw new UnsupportedOperationException("Immutable instance");
            }
        };
    }
}
