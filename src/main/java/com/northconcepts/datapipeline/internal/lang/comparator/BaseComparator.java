package com.northconcepts.datapipeline.internal.lang.comparator;

import java.util.Comparator;

public abstract class BaseComparator<T> implements Comparator<T>
{
    public static final int EQUAL_TO = 0;
    public static final int GREATER_THAN = 1;
    public static final int LESS_THAN = -1;
    
    @Override
	public boolean equals(final Object o) {
        return false;
    }
}
