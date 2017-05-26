package com.northconcepts.datapipeline.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.RecordList;
import com.northconcepts.datapipeline.internal.lang.Pair;
import com.northconcepts.datapipeline.internal.xpath.LocationPath;
import com.northconcepts.datapipeline.internal.xpath.XmlNode;
import com.northconcepts.datapipeline.internal.xpath.XmlNodeReader;

public class XmlReader extends DataReader
{
    protected final List<XmlRecordBreak> recordBreaks;
    protected final List<XmlField> fields;
    protected final XmlNodeReader reader;
    protected final File file;
    protected Record currentRecord;
    private boolean expandDuplicateFields;
    
    public XmlReader(final File file) {
        super();
        this.recordBreaks = new ArrayList<XmlRecordBreak>();
        this.fields = new ArrayList<XmlField>();
        try {
            this.file = file;
            final XMLInputFactory factory = XMLInputFactory.newInstance();
            final XMLStreamReader streamReader = factory.createXMLStreamReader(new FileInputStream(file));
            this.reader = new XmlNodeReader(streamReader);
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public XmlReader(final Reader reader) {
        super();
        this.recordBreaks = new ArrayList<XmlRecordBreak>();
        this.fields = new ArrayList<XmlField>();
        try {
            this.file = null;
            final XMLInputFactory factory = XMLInputFactory.newInstance();
            final XMLStreamReader streamReader = factory.createXMLStreamReader(reader);
            this.reader = new XmlNodeReader(streamReader);
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public XmlReader(final XMLStreamReader streamReader) {
        super();
        this.recordBreaks = new ArrayList<XmlRecordBreak>();
        this.fields = new ArrayList<XmlField>();
        this.file = null;
        this.reader = new XmlNodeReader(streamReader);
    }
    
    public XmlReader(final XmlNodeReader reader) {
        super();
        this.recordBreaks = new ArrayList<XmlRecordBreak>();
        this.fields = new ArrayList<XmlField>();
        this.file = null;
        this.reader = reader;
    }
    
    public XmlReader addRecordBreak(final String locationPathAsString) {
        this.recordBreaks.add(new XmlRecordBreak(locationPathAsString));
        return this;
    }
    
    public XmlReader addField(final XmlField field) {
        this.fields.add(field);
        return this;
    }
    
    public XmlReader addField(final String name, final String locationPathAsString, final boolean cascadeValues) {
        return this.addField(new XmlField(name, locationPathAsString, cascadeValues));
    }
    
    public XmlReader addField(final String name, final String locationPathAsString) {
        return this.addField(name, locationPathAsString, false);
    }
    
    public boolean getExpandDuplicateFields() {
        return this.expandDuplicateFields;
    }
    
    public XmlReader setExpandDuplicateFields(final boolean expandDuplicateFields) {
        this.expandDuplicateFields = expandDuplicateFields;
        return this;
    }
    
    protected void createRecord() {
        final Record r = new Record();
        for (final XmlField field : this.fields) {
            final Field targetField = r.addField().setName(field.getName());
            if (this.currentRecord != null && field.isCascadeValues()) {
                targetField.copyFrom(this.currentRecord.getField(field.getName()));
            }
        }
        this.currentRecord = r;
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        this.createRecord();
        XmlNode node = null;
        while ((node = this.reader.read()) != null) {
            this.saveFieldValues(node);
            if (this.isRecordBreak(node)) {
                this.getFieldValues(node.getSequence());
                if (this.expandDuplicateFields) {
                    this.expandAndPushRecords(this.currentRecord);
                    return null;
                }
                return this.currentRecord;
            }
        }
        return null;
    }
    
    protected void expandAndPushRecords(final Record record) {
        RecordList records = new RecordList(new Record[] { record });
        for (final String fieldName : record.getFieldNames()) {
            final Object value = record.getField(fieldName, true).getValue();
            if (value instanceof List) {                
				final List<Object> list = (List<Object>)value;
                records = this.expandField(records, fieldName, list);
            }
        }
        for (final Record r : records.getRecords()) {
            this.push(r);
        }
    }
    
    private RecordList expandField(final RecordList records, final String fieldName, final List<Object> list) {
        final RecordList target = new RecordList();
        for (final Record record : records.getRecords()) {
            for (final Object value : list) {
                final Record clone = new Record(record);
                clone.setField(fieldName, value);
                target.add(clone);
            }
        }
        return target;
    }
    
    protected void saveFieldValues(final XmlNode node) {
        for (final XmlField xmlField : this.fields) {
            final LocationPath path = xmlField.getLocationPath();
            if (path.matchesElement(node)) {
                final Object value = path.getValue(node);
                xmlField.addValue(node.getSequence(), value);
                final Field recordField = this.currentRecord.getField(xmlField.getName(), true);
                if (this.expandDuplicateFields) {
                    this.saveExpandedFieldValues(recordField, value);
                }
                else {
                    recordField.setValue(value);
                }
            }
        }
    }
    
    protected void saveExpandedFieldValues(final Field recordField, final Object value) {
        final Object oldValue = recordField.getValue();
        if (oldValue == null) {
            recordField.setValue(value);
        }
        else if (oldValue instanceof List) {
            final List<Object> list = (List<Object>)oldValue;
            list.add(value);
        }
        else {
            final List<Object> list = new ArrayList<Object>();
            list.add(oldValue);
            list.add(value);
            recordField.setValue(list);
        }
    }
    
    protected void getFieldValues(final long sequence) {
        for (final XmlField xmlField : this.fields) {
            xmlField.removeValues(sequence);
            final LinkedList<Pair<Long, Object>> list = xmlField.getValues();
            if (list.isEmpty()) {
                continue;
            }
            final Pair<Long, Object> pair = list.getLast();
            final Field recordField = this.currentRecord.getField(xmlField.getName(), true);
            recordField.setValue(pair.getB());
        }
    }
    
    protected boolean isRecordBreak(final XmlNode node) {
        for (final XmlRecordBreak recordBreak : this.recordBreaks) {
            if (recordBreak.getLocationPath().matchesElement(node)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
	public void open() throws DataException {
        super.open();
    }
    
    @Override
	public void close() throws DataException {
        try {
            this.reader.close();
        }
        finally {
            super.close();
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.file != null) {
            exception.set("XmlReader.file", this.file);
        }
        exception.set("XmlReader.recordBreakCount", this.recordBreaks.size());
        for (int i = 0; i < this.recordBreaks.size(); ++i) {
            exception.set("XmlReader.recordBreaks[" + i + "]", this.recordBreaks.get(i));
        }
        exception.set("XmlReader.fieldCount", this.fields.size());
        for (int i = 0; i < this.fields.size(); ++i) {
            exception.set("XmlReader.field[" + i + "]", this.fields.get(i));
        }
        if (this.reader != null) {
            this.reader.addExceptionProperties("XmlReader", exception);
        }
        exception.set("XmlReader.expandDuplicateFields", this.expandDuplicateFields);
        exception.setRecord(this.currentRecord);
        return super.addExceptionProperties(exception);
    }
}
