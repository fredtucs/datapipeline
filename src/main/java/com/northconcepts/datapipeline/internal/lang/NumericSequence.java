package com.northconcepts.datapipeline.internal.lang;

public class NumericSequence
{
    private static long sequence;
    
    public static synchronized long next() {
        if (NumericSequence.sequence < 0L) {
            NumericSequence.sequence = 0L;
        }
        return NumericSequence.sequence++;
    }
}
