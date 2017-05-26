package com.northconcepts.datapipeline.internal.javabean;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.xpath.XmlNodeReader;

public class JavaToXmlNodeReader extends XmlNodeReader
{
    private final JavaStreamReader reader;
    
    public JavaToXmlNodeReader(final JavaStreamReader reader) {
        super(null);
        this.reader = reader;
    }
    
    @Override
	protected JavaNode readImpl() throws DataException {
        if (this.reader.next()) {
            return this.reader.getNode();
        }
        return null;
    }
    
    @Override
	public void close() {
    }
}
