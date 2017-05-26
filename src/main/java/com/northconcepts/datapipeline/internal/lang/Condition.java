package com.northconcepts.datapipeline.internal.lang;

public final class Condition
{
    private boolean _true;
    
    public Condition() {
        super();
    }
    
    public Condition(final boolean _true) {
        super();
        this._true = _true;
    }
    
    public boolean isTrue() {
        return this._true;
    }
    
    public void setTrue() {
        this.set(true);
    }
    
    public void setFalse() {
        this.set(false);
    }
    
    public synchronized void set(final boolean _true) {
        this._true = _true;
        this.notifyAll();
    }
    
    public synchronized void waitForTrue(long timeout) throws InterruptedException {
        for (long finish = System.currentTimeMillis() + timeout; !this._true && timeout > 0L; timeout = finish - System.currentTimeMillis()) {
            this.wait(timeout);
        }
        if (!this._true) {
            throw new InterruptedException("timed-out");
        }
    }
    
    public synchronized void waitForTrue() throws InterruptedException {
        while (!this._true) {
            this.wait();
        }
    }
    
    public synchronized void waitForFalse(long timeout) throws InterruptedException {
        for (long finish = System.currentTimeMillis() + timeout; this._true && timeout > 0L; timeout = finish - System.currentTimeMillis()) {
            this.wait(timeout);
        }
        if (this._true) {
            throw new InterruptedException("timed-out");
        }
    }
    
    public synchronized void waitForFalse() throws InterruptedException {
        while (this._true) {
            this.wait();
        }
    }
}
