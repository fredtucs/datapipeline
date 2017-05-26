package com.northconcepts.datapipeline.internal.lang.comparator;

import java.math.BigInteger;

public final class BigIntegerComparator extends BaseComparator<BigInteger>
{
    public static final BigIntegerComparator INSTANCE;
    
    public int compare(final BigInteger o1, final BigInteger o2) {
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
    
    public static boolean matches(final BigInteger o1, final BigInteger o2) {
        return BigIntegerComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final BigInteger o1, final BigInteger o2) {
        return BigIntegerComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final BigInteger o1, final BigInteger o2) {
        return BigIntegerComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new BigIntegerComparator();
    }
}
