package com.northconcepts.datapipeline.internal.lang.comparator;

import java.util.Date;

public final class DateComparator extends BaseComparator<Date>
{
    public static final DateComparator INSTANCE;
    
    public int compare(final Date o1, final Date o2) {
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
    
    public static boolean matches(final Date o1, final Date o2) {
        return DateComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Date o1, final Date o2) {
        return DateComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Date o1, final Date o2) {
        return DateComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new DateComparator();
    }
}
