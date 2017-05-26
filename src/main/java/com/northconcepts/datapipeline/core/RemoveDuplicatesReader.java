package com.northconcepts.datapipeline.core;

import java.util.HashSet;
import java.util.List;

public class RemoveDuplicatesReader extends ProxyReader {
	private static final int DEFAULT_INITIAL_SIZE = 8192;
	private final HashSet<CompositeValue> originals;
	private final FieldList fields;

	public RemoveDuplicatesReader(final DataReader targetDataReader, final FieldList fields) {
		super(targetDataReader);
		this.originals = new HashSet<CompositeValue>(8192);
		this.fields = fields;
	}

	public RemoveDuplicatesReader(final DataReader targetDataReader) {
		this(targetDataReader, null);
	}

	@Override
	protected Record interceptRecord(final Record record) throws Throwable {
		final List<?> values = record.getValues(this.fields);
		final CompositeValue key = new CompositeValue(values);
		if (this.originals.contains(key)) {
			this.discard(record);
			return null;
		}
		this.originals.add(key);
		return record;
	}

	private void discard(final Record record) {
	}
}
