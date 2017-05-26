package com.northconcepts.datapipeline.internal.lang.comparator;

public final class StringComparator extends BaseComparator<Object>
{
    public static final StringComparator INSTANCE;
    
    public int compare(final Object o1, final Object o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        return o1.toString().compareTo(o2.toString());
    }
    
    public static boolean matches(final Object o1, final Object o2) {
        return StringComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Object o1, final Object o2) {
        return StringComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Object o1, final Object o2) {
        return StringComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new StringComparator();
    }
}
