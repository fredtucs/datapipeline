package com.northconcepts.datapipeline.throttle;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.meter.Meter;

public class Throttle {
	private static final long MINIMUM_SLEEP = 10L;
	private static final double MARGIN_PERCENT = 1.1;
	private final Meter meter;
	private double peekUnitsPerSecond;
	private int unitsPerSecond;
	private int unitsPerSecondWithMargin;

	public Throttle(final Meter meter, final int unitsPerSecond) {
		super();
		this.meter = meter;
		this.setUnitsPerSecond(unitsPerSecond);
	}

	public Meter getMeter() {
		return this.meter;
	}

	public int getUnitsPerSecond() {
		return this.unitsPerSecond;
	}

	public void setUnitsPerSecond(final int unitsPerSecond) {
		this.unitsPerSecond = unitsPerSecond;
		this.unitsPerSecondWithMargin = (int) (unitsPerSecond * 1.1);
	}

	public double getPeekUnitsPerSecond() {
		return this.peekUnitsPerSecond;
	}

	public void throttle() throws InterruptedException {
		final double currentUnitsPerSecond = this.meter.getUnitsPerSecond();
		if (currentUnitsPerSecond > this.peekUnitsPerSecond) {
			this.peekUnitsPerSecond = currentUnitsPerSecond;
		}
		if (currentUnitsPerSecond > this.unitsPerSecondWithMargin) {
			final long waitTime = (long) ((1.0 - this.unitsPerSecond / currentUnitsPerSecond) * 1000.0);
			if (waitTime >= 10L) {
				Thread.sleep(waitTime);
			}
		}
	}

	public DataException addExceptionProperties(final DataException exception) {
		exception.set("Throttle.peekUnitsPerSecond", this.peekUnitsPerSecond);
		exception.set("Throttle.unitsPerSecond", this.unitsPerSecond);
		exception.set("Throttle.unitsPerSecondWithMargin", this.unitsPerSecondWithMargin);
		if (this.meter != null) {
			this.meter.addExceptionProperties(exception);
		}
		return exception;
	}
}
