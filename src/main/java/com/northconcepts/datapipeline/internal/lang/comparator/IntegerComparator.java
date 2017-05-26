package com.northconcepts.datapipeline.internal.lang.comparator;

public final class IntegerComparator extends BaseComparator<Integer>
{
    public static final IntegerComparator INSTANCE;
    
    public int compare(final Integer o1, final Integer o2) {
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
    
    public static boolean matches(final Integer o1, final Integer o2) {
        return IntegerComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Integer o1, final Integer o2) {
        return IntegerComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Integer o1, final Integer o2) {
        return IntegerComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new IntegerComparator();
    }
}
