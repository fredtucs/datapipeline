package com.northconcepts.datapipeline.internal.lang.comparator;

public final class BooleanComparator extends BaseComparator<Boolean>
{
    public static final BooleanComparator INSTANCE;
    
    public int compare(final Boolean o1, final Boolean o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        final int b1 = (o1) ? 1 : 0;
        final int b2 = (o2) ? 1 : 0;
        return b1 - b2;
    }
    
    public static boolean matches(final Boolean o1, final Boolean o2) {
        return BooleanComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Boolean o1, final Boolean o2) {
        return BooleanComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Boolean o1, final Boolean o2) {
        return BooleanComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new BooleanComparator();
    }
}
