package com.northconcepts.datapipeline.core;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import com.northconcepts.datapipeline.internal.lang.Util;

public class DebugReader extends ProxyReader
{
    private final String id;
    private final PrintWriter writer;
    private boolean closeWriterOnClose;
    private StringBuilder buffer;
    
    public DebugReader(final DataReader nestedDataReader, final String id, final Writer writer) {
        super(nestedDataReader);
        this.buffer = new StringBuilder(2048);
        this.id = id;
        if (writer instanceof PrintWriter) {
            this.writer = (PrintWriter)writer;
        }
        else {
            this.writer = new PrintWriter(writer);
        }
    }
    
    public DebugReader(final DataReader nestedDataReader, final String id, final OutputStream outputStream) {
        this(nestedDataReader, id, new OutputStreamWriter(outputStream));
    }
    
    @Override
	public void close() throws DataException {
        try {
            if (this.closeWriterOnClose) {
                this.writer.close();
            }
        }
        finally {
            super.close();
        }
    }
    
    public boolean getCloseWriterOnClose() {
        return this.closeWriterOnClose;
    }
    
    public void setCloseWriterOnClose(final boolean closeWriterOnClose) {
        this.closeWriterOnClose = closeWriterOnClose;
    }
    
    @Override
	protected Record interceptRecord(final Record record) throws Throwable {
        this.buffer.setLength(0);
        this.buffer.append("==========================================================").append(Util.LINE_SEPARATOR);
        this.buffer.append(this.id).append(Util.LINE_SEPARATOR);
        this.buffer.append("------------------------------------------").append(Util.LINE_SEPARATOR);
        this.buffer.append(record);
        this.writer.println(this.buffer);
        this.writer.flush();
        return super.interceptRecord(record);
    }
}
