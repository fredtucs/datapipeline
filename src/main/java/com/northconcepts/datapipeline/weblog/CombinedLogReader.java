package com.northconcepts.datapipeline.weblog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.northconcepts.datapipeline.core.ParsingReader;
import com.northconcepts.datapipeline.core.Record;

public class CombinedLogReader extends ParsingReader
{
    private static final String[] FIELD_NAMES;
    private final SimpleDateFormat dateFormat;
    
    public CombinedLogReader(final File file) throws FileNotFoundException {
        super(file);
        this.dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        this.setFieldNames(CombinedLogReader.FIELD_NAMES);
    }
    
    public CombinedLogReader(final Reader reader) {
        super(reader);
        this.dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        this.setFieldNames(CombinedLogReader.FIELD_NAMES);
    }
    
    @Override
	protected boolean fillRecord(final Record record) throws Throwable {
        if (!super.fillRecord(record)) {
            return false;
        }
        record.getField(0).setValue(this.parseUnquotedText());
        record.getField(1).setValue(this.parseUnquotedText());
        record.getField(2).setValue(this.parseUnquotedText());
        record.getField(3).setValue(this.parseDate());
        record.getField(4).setValue(this.parseQuotedText());
        record.getField(5).setValue(this.parseUnquotedText());
        record.getField(6).setValue(this.parseUnquotedText());
        if (this.hasMore()) {
            record.getField(7).setValue(this.parseQuotedText());
        }
        if (this.hasMore()) {
            record.getField(8).setValue(this.parseQuotedText());
        }
        if (this.hasMore()) {
            record.getField(9).setValue(this.parseQuotedText());
        }
        this.parser.consumeWhitespace();
        return true;
    }
    
    private boolean hasMore() {
        this.parser.consumeWhitespace();
        final int c = this.parser.peek(0);
        return !this.isNewLine(c) && c != -1;
    }
    
    private String parseUnquotedText() {
        this.valueBuffer.setLength(0);
        this.parser.consumeWhitespace();
        this.matchUNQUOTED_TEXT();
        return this.valueBuffer.toString();
    }
    
    private String parseQuotedText() {
        this.valueBuffer.setLength(0);
        this.parser.consumeWhitespace();
        this.matchQUOTED_TEXT('\"', '\"');
        return this.valueBuffer.toString();
    }
    
    private Date parseDate() {
        this.valueBuffer.setLength(0);
        this.parser.consumeWhitespace();
        this.matchQUOTED_TEXT('[', ']');
        try {
            return this.dateFormat.parse(this.valueBuffer.toString());
        }
        catch (ParseException e) {
            throw this.exception(e);
        }
    }
    
    private void matchUNQUOTED_TEXT() {
        for (int c = this.parser.peek(0); c != 32 && c != -1 && !this.isNewLine(c); c = this.parser.peek(0)) {
            this.match(c);
        }
    }
    
    private void matchQUOTED_TEXT(final char begin, final char end) {
        this.parser.match(begin);
        for (int c = this.parser.peek(0); c != end && c != -1 && !this.isNewLine(c); c = this.parser.peek(0)) {
            this.match(c);
        }
        this.parser.match(end);
    }
    
    static {
        FIELD_NAMES = new String[] { "remoteHost", "clientUsername", "authenticatedUsername", "date", "request", "status", "bytes", "referrer", "userAgent", "cookies" };
    }
}
