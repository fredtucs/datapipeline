package com.northconcepts.datapipeline.core;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.northconcepts.datapipeline.internal.lang.Count;
import com.northconcepts.datapipeline.internal.lang.NumericSequence;

public class AsyncWriter extends ProxyWriter
{
    private static final Record EOF;
    private CountDownLatch finished;
    private LinkedBlockingDeque<Record> queue;
    private final Count bufferSizeInBytes;
    private int priority;
    private DataException exception;
    private final long id;
    private final String name;
    private Thread thread;
    private long recordPollTimeout;
    
    public AsyncWriter(final DataWriter nestedDataWriter, final int maxQueuedRecords) {
        super(nestedDataWriter);
        this.finished = new CountDownLatch(1);
        this.bufferSizeInBytes = new Count();
        this.priority = 5;
        this.id = NumericSequence.next();
        this.name = "AsyncWriter-" + this.id;
        this.recordPollTimeout = 2000L;
        this.queue = new LinkedBlockingDeque<Record>(maxQueuedRecords);
    }
    
    public AsyncWriter(final DataWriter nestedDataWriter) {
        super(nestedDataWriter);
        this.finished = new CountDownLatch(1);
        this.bufferSizeInBytes = new Count();
        this.priority = 5;
        this.id = NumericSequence.next();
        this.name = "AsyncWriter-" + this.id;
        this.recordPollTimeout = 2000L;
        this.queue = new LinkedBlockingDeque<Record>();
    }
    
    public long getBufferSizeInBytes() {
        return this.bufferSizeInBytes.getValue();
    }
    
    public long getPeakBufferSizeInBytes() {
        return this.bufferSizeInBytes.getPeak();
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public AsyncWriter setPriority(final int priority) {
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
                AsyncWriter.this.writeAsync();
            }
        }, this.name)).setPriority(this.priority);
        this.thread.start();
    }
    
    @Override
	public void close() throws DataException {
        try {
            this.thread = null;
            this.queue.put(AsyncWriter.EOF);
            this.finished.await();
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    @Override
	public int available() throws DataException {
        return this.queue.remainingCapacity();
    }
    
    @Override
	protected void writeImpl(final Record record) throws Throwable {
        this.queue.put(record);
        this.bufferSizeInBytes.add(record.getSizeInBytes());
    }
    
    private void writeAsync() {
        try {
            Record record;
            while ((record = this.queue.poll(this.recordPollTimeout, TimeUnit.MILLISECONDS)) != AsyncWriter.EOF) {
                if (record == null) {
                    continue;
                }
                if (record != null) {
                    this.bufferSizeInBytes.subtract(record.getSizeInBytes());
                }
                this.getNestedWriter().write(record);
            }
        }
        catch (Throwable e) {
            this.exception = this.exception(e);
        }
        finally {
            super.close();
            this.finished.countDown();
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("AsyncWriter.id", this.id);
        exception.set("AsyncWriter.name", this.name);
        exception.set("AsyncWriter.thread", this.thread);
        exception.set("AsyncWriter.recordPollTimeout", this.recordPollTimeout);
        exception.set("AsyncWriter.queueSize", this.queue.size());
        exception.set("AsyncWriter.bufferSizeInBytes", this.bufferSizeInBytes.getValue());
        exception.set("AsyncWriter.peakBufferSizeInBytes", this.bufferSizeInBytes.getPeak());
        exception.set("AsyncWriter.priority", this.priority);
        exception.set("AsyncWriter.exception", this.exception);
        return super.addExceptionProperties(exception);
    }
    
    static {
        EOF = new Record();
    }
}
