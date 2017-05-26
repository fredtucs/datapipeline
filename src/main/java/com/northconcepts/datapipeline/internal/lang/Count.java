package com.northconcepts.datapipeline.internal.lang;

public class Count
{
    private long value;
    private long peak;
    
    public long getValue() {
        return this.value;
    }
    
    public long getPeak() {
        return this.peak;
    }
    
    public synchronized void add(final long amount) {
        this.value += amount;
        if (this.value > this.peak) {
            this.peak = this.value;
        }
    }
    
    public synchronized void subtract(final long amount) {
        this.value -= amount;
    }
}
