package com.northconcepts.datapipeline.template;

import java.io.File;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.TextWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateWriter extends TextWriter
{
    private final Map<String, Object> data;
    private final Configuration configuration;
    private Template headerTemplate;
    private Template footerTemplate;
    private Template detailTemplate;
    
    public TemplateWriter(final File file) {
        super(file);
        this.data = new HashMap<String, Object>();
        this.configuration = new Configuration();
        this.data.put("writer", this);
    }
    
    public TemplateWriter(final Writer writer) {
        super(writer);
        this.data = new HashMap<String, Object>();
        this.configuration = new Configuration();
        this.data.put("writer", this);
    }
    
    public Map<String, Object> getData() {
        return this.data;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public Template getHeaderTemplate() {
        return this.headerTemplate;
    }
    
    public TemplateWriter setHeaderTemplate(final String headerTemplate) {
        try {
            return this.setHeaderTemplate(this.configuration.getTemplate(headerTemplate));
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public TemplateWriter setHeaderTemplate(final Template headerTemplate) {
        this.headerTemplate = headerTemplate;
        return this;
    }
    
    public Template getFooterTemplate() {
        return this.footerTemplate;
    }
    
    public TemplateWriter setFooterTemplate(final String footerTemplate) {
        try {
            return this.setFooterTemplate(this.configuration.getTemplate(footerTemplate));
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public TemplateWriter setFooterTemplate(final Template footerTemplate) {
        this.footerTemplate = footerTemplate;
        return this;
    }
    
    public Template getDetailTemplate() {
        return this.detailTemplate;
    }
    
    public TemplateWriter setDetailTemplate(final String detailTemplate) {
        try {
            return this.setDetailTemplate(this.configuration.getTemplate(detailTemplate));
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public TemplateWriter setDetailTemplate(final Template detailTemplate) {
        this.detailTemplate = detailTemplate;
        return this;
    }
    
    @Override
	public void open() throws DataException {
        try {
            super.open();
            if (this.headerTemplate != null) {
                this.headerTemplate.process(this.data, this.getWriter());
            }
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public void close() throws DataException {
        try {
            if (this.footerTemplate != null) {
                this.footerTemplate.process(this.data, this.getWriter());
            }
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
        for (final String name : record.getFieldNames()) {
            final Object value = record.getField(name).getValue();
            this.data.put(name, value);
        }
        this.data.put("record", record);
        this.detailTemplate.process(this.data, this.getWriter());
    }
}
