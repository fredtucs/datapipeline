package com.northconcepts.datapipeline.core;

public class NullWriter extends DataWriter
{
    @Override
	protected void writeImpl(final Record record) throws Throwable {
    }
}
