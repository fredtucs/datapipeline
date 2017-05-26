package com.northconcepts.datapipeline.internal.lang.comparator;

public final class ShortComparator extends BaseComparator<Short>
{
    public static final ShortComparator INSTANCE;
    
    public int compare(final Short o1, final Short o2) {
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
    
    public static boolean matches(final Short o1, final Short o2) {
        return ShortComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Short o1, final Short o2) {
        return ShortComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Short o1, final Short o2) {
        return ShortComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new ShortComparator();
    }
}
