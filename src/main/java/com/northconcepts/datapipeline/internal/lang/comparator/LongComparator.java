package com.northconcepts.datapipeline.internal.lang.comparator;

public final class LongComparator extends BaseComparator<Long>
{
    public static final LongComparator INSTANCE;
    
    public int compare(final Long o1, final Long o2) {
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
    
    public static boolean matches(final Long o1, final Long o2) {
        return LongComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Long o1, final Long o2) {
        return LongComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Long o1, final Long o2) {
        return LongComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new LongComparator();
    }
}
