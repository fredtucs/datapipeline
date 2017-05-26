package com.northconcepts.datapipeline.excel;

import java.io.InputStream;
import java.io.OutputStream;

import com.northconcepts.datapipeline.core.Field;

abstract class Provider
{
    private boolean evaluateExpressions;
    
    public boolean isEvaluateExpressions() {
        return this.evaluateExpressions;
    }
    
    public Provider setEvaluateExpressions(final boolean evaluateExpressions) {
        this.evaluateExpressions = evaluateExpressions;
        return this;
    }
    
    public abstract void newWorkbook();
    
    public abstract void openWorkbook(final InputStream p0);
    
    public abstract void saveWorkbook(final OutputStream p0);
    
    public abstract void startReading(final String p0);
    
    public abstract void startReading(final int p0);
    
    public abstract void readField(final int p0, final int p1, final Field p2);
    
    public abstract void endReading();
    
    public abstract void startWriting(final String p0, final int p1);
    
    public abstract void writeField(final int p0, final int p1, final int p2, final int p3, final Field p4);
    
    public abstract void setColumnWidths(final int p0, final int[] p1);
    
    public abstract void endWriting();
    
    public abstract int getLastRowIndex();
    
    public abstract int getLastColumnIndex();
    
    public abstract int getCellCount(final int p0);
}
