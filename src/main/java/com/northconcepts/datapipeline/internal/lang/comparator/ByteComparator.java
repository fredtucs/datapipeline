package com.northconcepts.datapipeline.internal.lang.comparator;

public final class ByteComparator extends BaseComparator<Byte>
{
    public static final ByteComparator INSTANCE;
    
    public int compare(final Byte o1, final Byte o2) {
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
    
    public static boolean matches(final Byte o1, final Byte o2) {
        return ByteComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Byte o1, final Byte o2) {
        return ByteComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Byte o1, final Byte o2) {
        return ByteComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new ByteComparator();
    }
}
