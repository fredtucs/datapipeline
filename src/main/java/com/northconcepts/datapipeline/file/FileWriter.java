package com.northconcepts.datapipeline.file;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.Util;

public class FileWriter extends DataWriter
{
    private final DataOutput out;
    private boolean firstRow;
    private final File file;
    
    public FileWriter(final File file) {
        super();
        this.firstRow = true;
        this.file = file;
        try {
            this.out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file), 8192));
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    public FileWriter(final DataOutput out) {
        super();
        this.firstRow = true;
        this.file = null;
        this.out = out;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("FileWriter.file", this.file);
        return super.addExceptionProperties(exception);
    }
    
    @Override
	public void close() {
        try {
            Util.closeIO((Closeable)this.out);
        }
        catch (IOException e) {
            throw this.exception(e);
        }
        finally {
            super.close();
        }
    }
    
    @Override
	protected void writeImpl(final Record record) throws Throwable {
        this.writeRecord(record);
    }
    
    private void writeRecord(final Record record) throws IOException {
        final int fieldCount = record.getFieldCount();
        if (this.firstRow) {
            this.firstRow = false;
            this.out.writeInt(fieldCount);
            for (int i = 0; i < fieldCount; ++i) {
                this.out.writeInt(FieldType.STRING.ordinal());
                this.writeString(record.getField(i).getName());
            }
        }
        this.out.writeInt(fieldCount);
        for (int i = 0; i < fieldCount; ++i) {
            this.writeField(record, i);
        }
    }
    
    private void writeField(final Record record, final int fieldIndex) throws IOException {
        FieldType type = record.getField(fieldIndex).getType();
        final Object value = record.getField(fieldIndex).getValue();
        if (type == FieldType.UNDEFINED) {
            type = FieldType.STRING;
        }
        if (value == null) {
            this.out.writeInt(type.ordinal() * -1);
            return;
        }
        this.out.writeInt(type.ordinal());
        switch (type) {
            case STRING: {
                this.writeString(record.getField(fieldIndex).getValueAsString());
            }
            case INT: {
                this.out.writeInt(record.getField(fieldIndex).getValueAsInteger());
            }
            case LONG: {
                this.out.writeLong(record.getField(fieldIndex).getValueAsLong());
            }
            case DOUBLE: {
                this.out.writeDouble(record.getField(fieldIndex).getValueAsDouble());
            }
            case DATE: {
                this.out.writeLong(record.getField(fieldIndex).getValueAsDate().getTime());
            }
            case TIME: {
                this.out.writeLong(record.getField(fieldIndex).getValueAsTime().getTime());
            }
            case DATETIME: {
                this.out.writeLong(record.getField(fieldIndex).getValueAsDatetime().getTime());
            }
            case BOOLEAN: {
                this.out.writeBoolean(record.getField(fieldIndex).getValueAsBoolean());
            }
            case BYTE: {
                this.out.writeByte(record.getField(fieldIndex).getValueAsByte());
            }
            case FLOAT: {
                this.out.writeFloat(record.getField(fieldIndex).getValueAsFloat());
            }
            case SHORT: {
                this.out.writeShort(record.getField(fieldIndex).getValueAsShort());
            }
            case CHAR: {
                this.out.writeChar(record.getField(fieldIndex).getValueAsChar());
            }
            case BLOB: {
                this.wrtieBytes(record.getField(fieldIndex).getValueAsBytes());
            }
            default: {
                throw new DataException("unknown field type: " + type + "; fieldIndex=" + fieldIndex + "; record=" + record).set("type", type).set("fieldIndex", fieldIndex).setRecord(record);
            }
        }
    }
    
    private void wrtieBytes(final byte[] bytes) throws IOException {
        this.out.writeInt(bytes.length);
        this.out.write(bytes);
    }
    
    private void writeString(final String string) throws IOException {
        this.out.writeInt(string.length());
        this.out.writeChars(string);
    }
}
