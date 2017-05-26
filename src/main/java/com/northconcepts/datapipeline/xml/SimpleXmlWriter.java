package com.northconcepts.datapipeline.xml;

import java.io.File;
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.TextWriter;

public class SimpleXmlWriter extends TextWriter
{
    private XMLStreamWriter xtw;
    
    public SimpleXmlWriter(final File file) {
        super(file);
        this.setFieldNamesInFirstRow(false);
    }
    
    public SimpleXmlWriter(final Writer writer) {
        super(writer);
        this.setFieldNamesInFirstRow(false);
    }
    
    @Override
	public void open() {
        super.open();
        final XMLOutputFactory xof = XMLOutputFactory.newInstance();
        try {
            (this.xtw = xof.createXMLStreamWriter(this.getWriter())).writeStartDocument();
            this.xtw.writeStartElement("records");
        }
        catch (XMLStreamException e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public void close() {
        try {
            this.xtw.writeEndElement();
            this.xtw.writeEndDocument();
            this.xtw.flush();
            this.xtw.close();
        }
        catch (XMLStreamException e) {
            throw this.exception(e);
        }
        super.close();
    }
    
    @Override
	protected void writeRecord(final Record record) throws Throwable {
        this.xtw.writeStartElement("record");
        for (int i = 0; i < record.getFieldCount(); ++i) {
            final Field field = record.getField(i);
            this.xtw.writeStartElement("field");
            this.xtw.writeAttribute("name", field.getName());
            this.xtw.writeCharacters(field.getValueAsString());
            this.xtw.writeEndElement();
        }
        this.xtw.writeEndElement();
    }
}
