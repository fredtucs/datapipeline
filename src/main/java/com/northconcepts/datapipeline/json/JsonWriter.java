package com.northconcepts.datapipeline.json;

import java.io.File;
import java.io.Writer;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.TextWriter;
import com.northconcepts.datapipeline.json.builder.JsonTemplate;

public class JsonWriter extends TextWriter
{
    private final JsonTemplate template;
    private JsonGenerator generator;
    
    public JsonWriter(final JsonTemplate template, final File file) {
        super(file);
        this.template = template;
        this.setFieldNamesInFirstRow(false);
    }
    
    public JsonWriter(final JsonTemplate template, final Writer writer) {
        super(writer);
        this.template = template;
        this.setFieldNamesInFirstRow(false);
    }
    
    @Override
	public void open() {
        super.open();
        final JsonFactory jsonFactory = new JsonFactory();
        try {
            this.generator = jsonFactory.createJsonGenerator(this.getWriter());
            this.template.buildHeader(this.generator);
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public void close() {
        try {
            this.template.buildFooter(this.generator);
            this.generator.close();
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
        finally {
            super.close();
        }
    }
    
    @Override
	protected void writeRecord(final Record record) throws Throwable {
        this.template.buildDetail(record, this.generator);
    }
}
