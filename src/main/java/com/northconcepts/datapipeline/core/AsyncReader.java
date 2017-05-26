package com.northconcepts.datapipeline.core;

import com.northconcepts.datapipeline.internal.lang.NumericSequence;

public class AsyncReader extends ProxyReader {
	@SuppressWarnings("unused")
	private static final int DEFAULT_MAX_BUFFER_SIZE = 524288;
	public static final int MINIMUM_BUFFER_SIZE = 16384;
	private final Object lock;
	private long maxBufferSizeInBytes;
	private long bufferSizeInBytes;
	private long peakBufferSizeInBytes;
	private int priority;
	private boolean eof;
	private DataException exception;
	private final long id;
	private final String name;
	private Thread thread;

	public AsyncReader(final DataReader reader) {
		super(reader);
		this.lock = new Object();
		this.maxBufferSizeInBytes = 524288L;
		this.priority = 5;
		this.id = NumericSequence.next();
		this.name = "AsyncReader-" + this.id;
	}

	public long getMaxBufferSizeInBytes() {
		return this.maxBufferSizeInBytes;
	}

	public AsyncReader setMaxBufferSizeInBytes(final long maxBufferSize) {
		if (maxBufferSize < 16384L) {
			throw new IllegalArgumentException("maxBufferSize (" + maxBufferSize + ") must be at least " + 16384 + " bytes");
		}
		this.maxBufferSizeInBytes = maxBufferSize;
		return this;
	}

	public long getBufferSizeInBytes() {
		synchronized (this.lock) {
			return this.bufferSizeInBytes;
		}
	}

	public long getPeakBufferSizeInBytes() {
		return this.peakBufferSizeInBytes;
	}

	public int getPriority() {
		return this.priority;
	}

	public AsyncReader setPriority(final int priority) {
		if (priority < 1 || priority > 10) {
			throw new IllegalArgumentException("priority (" + priority + ") must be between " + 1 + " and " + 10);
		}
		this.priority = priority;
		return this;
	}

	public DataException getException() {
		return this.exception;
	}

	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public void open() throws DataException {
		if (this.thread != null) {
			throw this.exception("cannot reopen while thread is still running");
		}
		super.open();
		final Messages messages = Messages.getCurrent();
		(this.thread = new Thread(new Runnable() {
			public void run() {
				Messages.setCurrent(messages);
				AsyncReader.this.fillCache();
			}
		}, this.name)).setPriority(this.priority);
		this.thread.start();
	}

	@Override
	public void close() throws DataException {
		this.eof = true;
		this.thread = null;
		super.close();
	}

	@Override
	public void push(final Record record) {
		synchronized (this.lock) {
			super.push(record);
			this.bufferSizeInBytes += record.getSizeInBytes();
			if (this.bufferSizeInBytes > this.peakBufferSizeInBytes) {
				this.peakBufferSizeInBytes = this.bufferSizeInBytes;
			}
			this.lock.notify();
		}
	}

	@Override
	protected Record pop() {
		synchronized (this.lock) {
			this.lock.notify();
			final Record record = super.pop();
			if (record != null) {
				this.bufferSizeInBytes -= record.getSizeInBytes();
			}
			return record;
		}
	}

	@Override
	public int available() throws DataException {
		synchronized (this.lock) {
			return super.available();
		}
	}

	private void fillCache() {
		try {
			while (!this.eof) {
				final Record record = super.readImpl();
				if (record == null) {
					this.eof = true;
					synchronized (this.lock) {
						this.lock.notify();
					}
					return;
				}
				this.push(record);
				while (this.getBufferSizeInBytes() >= this.maxBufferSizeInBytes) {
					synchronized (this.lock) {
						this.lock.wait();
					}
				}
			}
		} catch (Throwable e) {
			this.exception = this.exception(e);
		}
	}

	@Override
	protected Record readImpl() throws Throwable {
		while (this.getBufferSizeInBytes() <= 0L) {
			if (this.eof) {
				return null;
			}
			if (this.exception != null) {
				throw this.exception("exception in pipeline thread", this.exception);
			}
			this.assertOpened();
			synchronized (this.lock) {
				this.lock.wait();
			}
		}
		return this.read();
	}

	@Override
	public DataException addExceptionProperties(final DataException exception) {
		exception.set("AsyncReader.id", this.id);
		exception.set("AsyncReader.name", this.name);
		exception.set("AsyncReader.thread", this.thread);
		exception.set("AsyncReader.maxBufferSizeInBytes", this.maxBufferSizeInBytes);
		exception.set("AsyncReader.bufferSizeInBytes", this.bufferSizeInBytes);
		exception.set("AsyncReader.peakBufferSizeInBytes", this.peakBufferSizeInBytes);
		exception.set("AsyncReader.priority", this.priority);
		exception.set("AsyncReader.exception", this.exception);
		return super.addExceptionProperties(exception);
	}
}
