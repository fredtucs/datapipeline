package com.northconcepts.datapipeline.job;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;

public interface JobTemplate
{
    public static final JobTemplate DEFAULT = new JobTemplateImpl();
    
    void transfer(DataReader p0, DataWriter p1) throws DataException;
    
     <R extends DataReader, W extends DataWriter> void transfer(R p0, W p1, boolean p2, JobCallback<R, W> p3) throws DataException;
}
