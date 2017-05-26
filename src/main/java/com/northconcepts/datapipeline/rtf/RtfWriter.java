package com.northconcepts.datapipeline.rtf;

import java.io.File;
import java.io.OutputStream;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.northconcepts.datapipeline.core.BinaryWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;

public class RtfWriter extends BinaryWriter
{
    private final Document document;
    private Table table;
    
    public RtfWriter(final File file) {
        super(file);
        this.document = new Document();
    }
    
    public RtfWriter(final OutputStream outputStream) {
        super(outputStream);
        this.document = new Document();
    }
    
    @Override
	public void open() {
        try {
            RtfWriter2.getInstance(this.document, this.getOutputStream());
            this.document.setPageSize(PageSize.LETTER.rotate());
            this.document.open();
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
        super.open();
    }
    
    @Override
	public void close() {
        try {
            this.document.add(this.table);
            this.document.close();
        }
        catch (Throwable e) {
            throw this.exception(e);
        }
        finally {
            super.close();
        }
    }
    
    @Override
	protected void writeRecord(final Record record) throws Throwable {
        final int fieldCount = record.getFieldCount();
        final boolean headerRow = this.table == null;
        if (headerRow) {
            this.table = new Table(fieldCount);
        }
        for (int i = 0; i < fieldCount; ++i) {
            final Field field = record.getField(i);
            if (headerRow) {
                final Cell cell = new Cell(field.getValueAsString());
                cell.setHeader(true);
                this.table.addCell(cell);
            }
            else if (field.isNull()) {
                this.table.addCell("");
            }
            else if (field.getValue() instanceof Number) {
                final Cell cell = new Cell(field.getValueAsString());
                cell.setHorizontalAlignment(2);
                this.table.addCell(cell);
            }
            else {
                this.table.addCell(field.getValueAsString());
            }
        }
        if (headerRow) {
            this.table.endHeaders();
        }
    }
}
