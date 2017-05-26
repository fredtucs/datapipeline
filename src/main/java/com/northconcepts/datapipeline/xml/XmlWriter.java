package com.northconcepts.datapipeline.xml;

import java.io.File;
import java.io.Writer;

import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.TextWriter;
import com.northconcepts.datapipeline.xml.builder.XmlTemplate;

public class XmlWriter extends TextWriter
{
    private final XmlTemplate template;
    
    public XmlWriter(final XmlTemplate template, final File file) {
        super(file);
        this.template = template;
        this.setFieldNamesInFirstRow(false);
    }
    
    public XmlWriter(final XmlTemplate template, final Writer writer) {
        super(writer);
        this.template = template;
        this.setFieldNamesInFirstRow(false);
    }
    
    @Override
	public void open() {
        try {
            this.template.buildHeader(this.getWriter());
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
        super.open();
    }
    
    @Override
	public void close() {
        try {
            this.template.buildFooter(this.getWriter());
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
        this.template.buildDetail(record, this.getWriter());
    }
}
