package com.northconcepts.datapipeline.meter;

import com.northconcepts.datapipeline.core.DataException;

public class Meter
{
    public static final long K = 1000L;
    public static final long M = 1000000L;
    public static final long G = 1000000000L;
    public static final long T = 1000000000000L;
    public static final long P = 1000000000000000L;
    public static final long E = 1000000000000000000L;
    private long count;
    private long started;
    private long stopped;
    
    public Meter() {
        super();
        this.reset();
    }
    
    public Meter reset() {
        this.count = 0L;
        this.started = System.currentTimeMillis();
        this.stopped = this.started;
        return this;
    }
    
    public Meter stopTimer() {
        if (this.isRunning()) {
            this.stopped = System.currentTimeMillis();
        }
        return this;
    }
    
    public boolean isRunning() {
        return this.started == this.stopped;
    }
    
    public void add() {
        ++this.count;
    }
    
    public Meter add(final long difference) {
        this.count += difference;
        return this;
    }
    
    public long getCount() {
        return this.count;
    }
    
    public long getStarted() {
        return this.started;
    }
    
    public long getElapsedTime() {
        long elapsedTime;
        if (this.isRunning()) {
            elapsedTime = System.currentTimeMillis() - this.started;
        }
        else {
            elapsedTime = this.stopped - this.started;
        }
        if (elapsedTime < 1L) {
            elapsedTime = 1L;
        }
        return elapsedTime;
    }
    
    public double getUnitsPerSecond() {
        return this.getCount() / this.getElapsedTime() * 1000.0;
    }
    
    @Override
	public String toString() {
        return this.getUnitsPerSecondAsString();
    }
    
    public String getUnitsPerSecondAsString() {
        return this.getUnitsPerSecondAsString(null);
    }
    
    public String getUnitsPerSecondAsString(String units) {
        if (units == null) {
            units = "units";
        }
        final double rate = this.getUnitsPerSecond();
        if (rate < 0.0) {
            return "undefined " + units + "/s";
        }
        if (rate < 100.0) {
            return round(rate) + " " + units + "/s";
        }
        if (rate < 100000.0) {
            return round(rate / 1000.0) + " kilo-" + units + "/s";
        }
        if (rate < 1.0E8) {
            return round(rate / 1000000.0) + " mega-" + units + "/s";
        }
        if (rate < 1.0E11) {
            return round(rate / 1.0E9) + " giga-" + units + "/s";
        }
        if (rate < 1.0E14) {
            return round(rate / 1.0E12) + " tera-" + units + "/s";
        }
        if (rate < 1.0E17) {
            return round(rate / 1.0E15) + " peta-" + units + "/s";
        }
        return round(rate / 1.0E18) + " exa-" + units + "/s";
    }
    
    private static double round(final double value) {
        return Math.floor(value * 10.0) / 10.0;
    }
    
    public DataException addExceptionProperties(final DataException exception) {
        exception.set("Meter.count", this.count);
        exception.set("Meter.started", this.started);
        exception.set("Meter.stopped", this.stopped);
        exception.set("Meter.isRunning", this.isRunning());
        return exception;
    }
}
