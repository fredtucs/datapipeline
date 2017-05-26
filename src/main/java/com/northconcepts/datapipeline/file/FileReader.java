package com.northconcepts.datapipeline.file;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.Util;

public class FileReader extends DataReader
{
    private final DataInput in;
    private String[] fieldNames;
    private boolean firstRow;
    private final File file;
    
    public FileReader(final File file) {
        super();
        this.firstRow = true;
        this.file = file;
        try {
            this.in = new DataInputStream(new BufferedInputStream(new FileInputStream(file), 8192));
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    public FileReader(final DataInput in) {
        super();
        this.firstRow = true;
        this.file = null;
        this.in = in;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("FileReader.file", this.file);
        return super.addExceptionProperties(exception);
    }
    
    @Override
	public void close() {
        try {
            Util.closeIO((Closeable)this.in);
        }
        catch (IOException e) {
            throw DataException.wrap(e);
        }
        finally {
            super.close();
        }
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        try {
            Record record;
            if (this.fieldNames != null) {
                record = new Record(this.fieldNames);
            }
            else {
                record = new Record();
            }
            this.readRecord(record);
            if (this.firstRow) {
                this.firstRow = false;
                this.fieldNames = new String[record.getFieldCount()];
                for (int i = 0; i < this.fieldNames.length; ++i) {
                    this.fieldNames[i] = record.getField(i).getValueAsString();
                }
                this.decrementRecordCount();
                return this.readImpl();
            }
            record.setAlive();
            return record;
        }
        catch (EOFException e) {
            return null;
        }
    }
    
    private void readRecord(final Record record) throws IOException {
        for (int fieldCount = this.in.readInt(), i = 0; i < fieldCount; ++i) {
            this.readField(record, i);
        }
    }
    
    private void readField(final Record record, final int fieldIndex) throws IOException {
        int typeIndex = this.in.readInt();
        if (typeIndex < 0) {
            typeIndex *= -1;
            final FieldType type = FieldType.values()[typeIndex];
            record.getField(fieldIndex, true).setNull(type);
            return;
        }
        final FieldType type = FieldType.values()[typeIndex];
        final Field field = record.getField(fieldIndex, true);
        switch (type) {
            case STRING: {
                final int length = this.in.readInt();
                final StringBuilder s = new StringBuilder(length);
                for (int i = 0; i < length; ++i) {
                    s.append(this.in.readChar());
                }
                field.setValue(s.toString());
            }
            case INT: {
                field.setValue(this.in.readInt());
            }
            case LONG: {
                field.setValue(this.in.readLong());
            }
            case DOUBLE: {
                field.setValue(this.in.readDouble());
            }
            case DATE: {
                field.setValue(new Date(this.in.readLong()));
            }
            case TIME: {
                field.setValue(new Time(this.in.readLong()));
            }
            case DATETIME: {
                field.setValue(new Timestamp(this.in.readLong()));
            }
            case BOOLEAN: {
                field.setValue(this.in.readBoolean());
            }
            case BYTE: {
                field.setValue(this.in.readByte());
            }
            case FLOAT: {
                field.setValue(this.in.readFloat());
            }
            case SHORT: {
                field.setValue(this.in.readShort());
            }
            case CHAR: {
                field.setValue(this.in.readChar());
            }
            case BLOB: {
                final int lengthBytes = this.in.readInt();
                final byte[] bytes = new byte[lengthBytes];
                for (int j = 0; j < lengthBytes; ++j) {
                    bytes[j] = this.in.readByte();
                }
                field.setValue(bytes);
            }
            default: {
                throw new DataException("unknown field type: " + type + "; fieldIndex=" + fieldIndex + "; record=" + record).set("type", type).set("field index", fieldIndex).setRecord(record);
            }
        }
    }
}
