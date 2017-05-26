package com.northconcepts.datapipeline.internal.lang.comparator;

import java.sql.Timestamp;

public final class SQLTimestampComparator extends BaseComparator<Timestamp>
{
    public static final SQLTimestampComparator INSTANCE;
    
    public int compare(final Timestamp o1, final Timestamp o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        return o1.compareTo(o2);
    }
    
    public static boolean matches(final Timestamp o1, final Timestamp o2) {
        return SQLTimestampComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Timestamp o1, final Timestamp o2) {
        return SQLTimestampComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Timestamp o1, final Timestamp o2) {
        return SQLTimestampComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new SQLTimestampComparator();
    }
}
