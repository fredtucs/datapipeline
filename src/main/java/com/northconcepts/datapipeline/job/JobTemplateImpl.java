package com.northconcepts.datapipeline.job;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;

public class JobTemplateImpl implements JobTemplate
{
    private static final Logger log;
    
    public void transfer(final DataReader reader, final DataWriter writer) throws DataException {
        this.assertNotNull(reader, writer);
        this.doTransfer(reader, writer, JobCallback.Helper.nullCallback());
    }
    
    public <R extends DataReader, W extends DataWriter> void transfer(final R reader, final W writer, final boolean async, JobCallback<R, W> callback) throws DataException {
        this.assertNotNull(reader, writer);
        if (callback == null) {
            callback = JobCallback.Helper.nullCallback();
        }
        if (async) {
            final JobCallback<R, W> callback2 = callback;
            final Thread t = new Thread() {
                @Override
				public void run() {
                    JobTemplateImpl.this.doTransfer(reader, writer, callback2);
                }
            };
            t.start();
        }
        else {
            this.doTransfer(reader, writer, callback);
        }
    }
    
    private void assertNotNull(final DataReader reader, final DataWriter writer) {
        if (reader == null) {
            throw new DataException("reader is null");
        }
        if (writer == null) {
            throw new DataException("writer is null");
        }
    }
    
    private <R extends DataReader, W extends DataWriter> void doTransfer(final R reader, final W writer, final JobCallback<R, W> callback) throws DataException {
        JobTemplateImpl.log.debug("job::Start");
        Record currentRecord = null;
        reader.open();
        try {
            writer.open();
            try {
                try {
                    while ((currentRecord = reader.read()) != null) {
                        writer.write(currentRecord);
                        callback.onProgress(reader, writer, currentRecord);
                        currentRecord = null;
                    }
                    JobTemplateImpl.log.debug("job::Success");
                    callback.onSuccess(reader, writer);
                }
                catch (Throwable e) {
                    JobTemplateImpl.log.debug("job::Failure");
                    callback.onFailure(reader, writer, currentRecord, e);
                }
            }
            catch (Throwable e) {
                throw DataException.wrap(e).setRecord(currentRecord);
            }
            finally {
                writer.close();
            }
        }
        finally {
            reader.close();
        }
    }
    
    static {
        log = DataEndpoint.log;
    }
}
