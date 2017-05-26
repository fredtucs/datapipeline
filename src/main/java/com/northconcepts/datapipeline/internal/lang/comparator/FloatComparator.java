package com.northconcepts.datapipeline.internal.lang.comparator;

public final class FloatComparator extends BaseComparator<Float>
{
    public static final FloatComparator INSTANCE;
    
    public int compare(final Float o1, final Float o2) {
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
    
    public static boolean matches(final Float o1, final Float o2) {
        return FloatComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Float o1, final Float o2) {
        return FloatComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Float o1, final Float o2) {
        return FloatComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new FloatComparator();
    }
}
