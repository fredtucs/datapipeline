package com.northconcepts.datapipeline.json;

import java.io.File;
import java.io.Reader;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.json.JsonNodeReader;
import com.northconcepts.datapipeline.internal.json.JsonToXmlNodeReader;
import com.northconcepts.datapipeline.xml.XmlReader;

public class JsonReader extends ProxyReader
{
    private final XmlReader reader;
    
    public JsonReader(final File file) {
        super(null);
        try {
            final JsonNodeReader jsonNodeReader = new JsonNodeReader(file);
            this.setNestedDataReader(this.reader = new XmlReader(new JsonToXmlNodeReader(jsonNodeReader)));
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public JsonReader(final Reader reader) {
        super(null);
        try {
            final JsonNodeReader jsonNodeReader = new JsonNodeReader(reader);
            this.setNestedDataReader(this.reader = new XmlReader(new JsonToXmlNodeReader(jsonNodeReader)));
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
    }
    
    public JsonReader addRecordBreak(final String locationPathAsString) {
        this.reader.addRecordBreak(locationPathAsString);
        return this;
    }
    
    public JsonReader addField(final String name, final String locationPathAsString, final boolean cascadeValues) {
        this.reader.addField(name, locationPathAsString, cascadeValues);
        return this;
    }
    
    public JsonReader addField(final String name, final String locationPathAsString) {
        this.reader.addField(name, locationPathAsString, false);
        return this;
    }
    
    @Override
	public void open() throws DataException {
        super.open();
        this.reader.open();
    }
    
    @Override
	public Record read() throws DataException {
        return this.reader.read();
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.reader != null) {
            this.reader.addExceptionProperties(exception);
        }
        return super.addExceptionProperties(exception);
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        throw new RuntimeException("readImpl not implemented");
    }
}
