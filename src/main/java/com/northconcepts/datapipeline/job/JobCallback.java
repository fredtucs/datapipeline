package com.northconcepts.datapipeline.job;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;

public interface JobCallback<R extends DataReader, W extends DataWriter>
{
    void onProgress(R p0, W p1, Record p2) throws Throwable;
    
    void onSuccess(R p0, W p1) throws Throwable;
    
    void onFailure(R p0, W p1, Record p2, Throwable p3) throws Throwable;
    
    public static class Helper
    {
        public static <R extends DataReader, W extends DataWriter> JobCallback<R, W> nullCallback() {
            return new JobCallback<R, W>() {
                public void onProgress(R reader, W writer, Record currentRecord) throws Throwable {
                }
                
                public void onSuccess(R reader, W writer) throws Throwable {
                }
                
                public void onFailure(R reader, W writer, Record currentRecord, Throwable exception) throws Throwable {
                    throw exception;
                }
            };
        }
    }
}
