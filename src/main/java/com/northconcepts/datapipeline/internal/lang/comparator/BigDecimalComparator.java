package com.northconcepts.datapipeline.internal.lang.comparator;

import java.math.BigDecimal;

public final class BigDecimalComparator extends BaseComparator<BigDecimal>
{
    public static final BigDecimalComparator INSTANCE;
    
    public int compare(final BigDecimal o1, final BigDecimal o2) {
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
    
    public static boolean matches(final BigDecimal o1, final BigDecimal o2) {
        return BigDecimalComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final BigDecimal o1, final BigDecimal o2) {
        return BigDecimalComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final BigDecimal o1, final BigDecimal o2) {
        return BigDecimalComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new BigDecimalComparator();
    }
}
