package com.northconcepts.datapipeline.internal.json;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.xpath.XmlNodeReader;

public class JsonToXmlNodeReader extends XmlNodeReader
{
    private final JsonNodeReader reader;
    
    public JsonToXmlNodeReader(final JsonNodeReader reader) {
        super(null);
        this.reader = reader;
    }
    
    @Override
	protected JsonXmlNode readImpl() throws DataException {
        return this.reader.read();
    }
    
    @Override
	public void close() {
        this.reader.close();
    }
}
