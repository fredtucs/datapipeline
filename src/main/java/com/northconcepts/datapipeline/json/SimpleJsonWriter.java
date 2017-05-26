package com.northconcepts.datapipeline.json;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.TextWriter;

public class SimpleJsonWriter extends TextWriter
{
    private JsonGenerator jGenerator;
    
    public SimpleJsonWriter(final File file) {
        super(file);
        this.setFieldNamesInFirstRow(false);
    }
    
    public SimpleJsonWriter(final Writer writer) {
        super(writer);
        this.setFieldNamesInFirstRow(false);
    }
    
    @Override
	public void open() {
        super.open();
        final JsonFactory jsonFactory = new JsonFactory();
        try {
            (this.jGenerator = jsonFactory.createJsonGenerator(this.getWriter())).writeStartArray();
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public void close() {
        try {
            this.jGenerator.writeEndArray();
            this.jGenerator.close();
        }
        catch (IOException e) {
            throw this.exception(e);
        }
        super.close();
    }
    
    @Override
	protected void writeRecord(final Record record) throws Throwable {
        this.jGenerator.writeStartObject();
        for (int i = 0; i < record.getFieldCount(); ++i) {
            final Field field = record.getField(i);
            this.jGenerator.writeObjectField(field.getName(), field.getValue());
        }
        this.jGenerator.writeEndObject();
    }
}
