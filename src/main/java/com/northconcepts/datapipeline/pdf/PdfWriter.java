package com.northconcepts.datapipeline.pdf;

import java.io.File;
import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.northconcepts.datapipeline.core.BinaryWriter;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;

public class PdfWriter extends BinaryWriter
{
    private final Document document;
    private PdfPTable table;
    
    public PdfWriter(final File file) {
        super(file);
        this.document = new Document();
    }
    
    public PdfWriter(final OutputStream outputStream) {
        super(outputStream);
        this.document = new Document();
    }
    
    @Override
	public void open() {
        try {
            com.lowagie.text.pdf.PdfWriter.getInstance(this.document, this.getOutputStream());
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
        if (this.table == null) {
            (this.table = new PdfPTable(fieldCount)).setHeaderRows(1);
        }
        for (int i = 0; i < fieldCount; ++i) {
            final Field field = record.getField(i);
            if (field.isNull()) {
                this.table.addCell("");
            }
            else if (field.getValue() instanceof Number) {
                final Paragraph p = new Paragraph(field.getValueAsString());
                final PdfPCell cell = new PdfPCell(p);
                cell.setHorizontalAlignment(2);
                this.table.addCell(cell);
            }
            else {
                this.table.addCell(field.getValueAsString());
            }
        }
    }
}
