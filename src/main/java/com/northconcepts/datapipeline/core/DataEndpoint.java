package com.northconcepts.datapipeline.core;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


@SuppressWarnings("unused")
public abstract class DataEndpoint {

	private static final String VENDOR = "North Concepts Inc.";
	private static final String PRODUCT = "Data Pipeline";
	private static final String PRODUCT_VERSION = "2.3.3";
	public static final Logger log;
	public static final int BUFFER_SIZE = 16384;
	private static final SimpleDateFormat TIMESTAMP_FORMAT;
	private State state;
	private String description;
	private static final long RECORD_COUNT_THRESHOLD = 1000000000000000000L;
	private BigInteger recordCountAsBigInteger;
	private long recordCount;

	public DataEndpoint() {
		super();
		this.state = State.NEW;
	}

	public String getDescription() {
		return this.description;
	}

	public DataEndpoint setDescription(final String description) {
		this.description = description;
		return this;
	}

	public void open() throws DataException {
		this.resetRecordCount();
		this.state = State.OPENED;
	}

	public void close() throws DataException {
		this.state = State.CLOSED;
	}

	public State getState() {
		return this.state;
	}

	public boolean isOpen() {
		return this.state == State.OPENED;
	}

	public boolean isClosed() {
		return this.state == State.CLOSED;
	}

	public void assertOpened() {
		if (this.state != State.OPENED) {
			throw this.exception("Endpoint is not open");
		}
	}

	public void assertNotOpened() {
		if (this.state == State.OPENED) {
			throw this.exception("Endpoint is open");
		}
	}

	public DataException addExceptionProperties(final DataException exception) {
		synchronized (DataEndpoint.TIMESTAMP_FORMAT) {
			exception.set("DataEndpoint.timestamp", DataEndpoint.TIMESTAMP_FORMAT.format(new Date()));
		}
		exception.set("DataEndpoint.thread", Thread.currentThread().getName());
		exception.set("DataEndpoint.description", this.getDescription());
		exception.set("DataEndpoint.state", this.getState());
		return exception;
	}

	public DataException exception(final String message, final Throwable exception) {
		return this.addExceptionProperties(DataException.wrap(message, exception));
	}

	public DataException exception(final Throwable exception) {
		return this.addExceptionProperties(DataException.wrap(exception));
	}

	public DataException exception(final String message) {
		return this.addExceptionProperties(new DataException(message));
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			if (this.isOpen()) {
				this.close();
			}
		} finally {
			super.finalize();
		}
	}

	@Override
	public String toString() {
		return super.toString() + " (" + this.getState() + ")";
	}

	protected void incrementRecordCount() {
		if (this.recordCount == 1000000000000000000L) {
			if (this.recordCountAsBigInteger == null) {
				this.recordCountAsBigInteger = BigInteger.valueOf(this.recordCount);
			} else {
				this.recordCountAsBigInteger = this.recordCountAsBigInteger.add(BigInteger.valueOf(this.recordCount));
			}
			this.recordCount = 0L;
		}
		++this.recordCount;
	}

	protected void decrementRecordCount() {
		if (this.recordCount == 0L && this.recordCountAsBigInteger != null && this.recordCountAsBigInteger.compareTo(BigInteger.ZERO) > 0) {
			this.recordCountAsBigInteger = this.recordCountAsBigInteger.subtract(BigInteger.valueOf(1000000000000000000L));
			this.recordCount = 999999999999999999L;
		} else if (this.recordCount > 0L) {
			--this.recordCount;
		}
	}

	protected void resetRecordCount() {
		this.recordCount = 0L;
		this.recordCountAsBigInteger = null;
	}

	public boolean isRecordCountBigInteger() {
		return this.recordCountAsBigInteger != null;
	}

	public long getRecordCount() {
		return this.recordCount;
	}

	public BigInteger getRecordCountAsBigInteger() {
		if (this.recordCountAsBigInteger == null) {
			return BigInteger.valueOf(this.recordCount);
		}
		return this.recordCountAsBigInteger.add(BigInteger.valueOf(this.recordCount));
	}

	public String getRecordCountAsString() {
		if (this.isRecordCountBigInteger()) {
			return this.getRecordCountAsBigInteger().toString();
		}
		return String.valueOf(this.recordCount);
	}

	static {
		log = Logger.getLogger("com.northconcepts.datapipeline");
		TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss.SSS");
		DataEndpoint.log.debug("Data Pipeline v2.3.3 by North Concepts Inc.");
	}

	public enum State {
		NEW, OPENED, CLOSED;
	}
}
