package com.northconcepts.datapipeline.core;

public enum FieldType {
	UNDEFINED(4), STRING(4), INT(4), LONG(8), DOUBLE(8), DATETIME(8), BOOLEAN(1), BYTE(1), FLOAT(4), SHORT(2), CHAR(2), DATE(8), TIME(8), BLOB(0);

	private final int sizeInBytes;

	private FieldType(int sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}

	public int getSizeInBytes(Object value) {
		return this.sizeInBytes;
	}
}
