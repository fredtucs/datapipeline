package com.northconcepts.datapipeline.internal.lang.comparator;

public final class DoubleComparator extends BaseComparator<Double>
{
    public static final DoubleComparator INSTANCE;
    
    public int compare(final Double o1, final Double o2) {
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
    
    public static boolean matches(final Double o1, final Double o2) {
        return DoubleComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Double o1, final Double o2) {
        return DoubleComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Double o1, final Double o2) {
        return DoubleComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new DoubleComparator();
    }
}
