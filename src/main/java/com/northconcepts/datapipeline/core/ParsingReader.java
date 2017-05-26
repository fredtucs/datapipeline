package com.northconcepts.datapipeline.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.northconcepts.datapipeline.internal.lang.Util;

public abstract class ParsingReader extends AbstractReader
{
    protected final Parser parser;
    protected StringBuilder valueBuffer;
    private final File file;
    private boolean firstRow;
    private long lineNumber;
    
    public ParsingReader(final File file) throws FileNotFoundException {
        super();
        this.valueBuffer = new StringBuilder(128);
        this.firstRow = true;
        this.lineNumber = 1L;
        try {
            this.file = file;
            this.parser = new Parser(new FileReader(file));
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    public ParsingReader(final Reader reader) {
        super();
        this.valueBuffer = new StringBuilder(128);
        this.firstRow = true;
        this.lineNumber = 1L;
        this.parser = new Parser(reader);
        this.file = null;
    }
    
    @Override
	public int available() {
        try {
            return super.available() + (this.parser.ready() ? 1 : 0);
        }
        catch (IOException e) {
            throw this.exception(e);
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.file != null) {
            exception.set("ParsingReader.file", this.file);
        }
        exception.set("ParsingReader.lineNumber", this.lineNumber);
        if (this.parser != null) {
            this.parser.exception(exception);
        }
        return super.addExceptionProperties(exception);
    }
    
    @Override
	protected boolean fillRecord(final Record record) throws Throwable {
        if (this.parser.peek(0) == -1) {
            return false;
        }
        if (this.firstRow) {
            if (this.isNewLine(this.parser.peek(0))) {
                this.matchNEWLINES();
            }
        }
        else {
            this.matchNEWLINES();
        }
        return this.parser.peek(0) != -1;
    }
    
    protected void matchDOUBLE() {
        final int c = this.parser.peek(0);
        if (this.isDigit(c)) {
            this.matchINTEGER();
            if (this.parser.peek(0) == 46) {
                this.match(46);
                this.matchDIGITS();
            }
        }
        else {
            if (c != 46) {
                throw this.exception("expected a number, found " + Util.toPrintableString(c));
            }
            this.match(46);
            this.matchDIGITS();
        }
    }
    
    protected void matchINTEGER() {
        this.matchDIGIT();
        for (int c = this.parser.peek(0); this.isDigit(c) || c == 44; c = this.parser.peek(0)) {
            if (c == 44) {
                this.parser.consume();
            }
            else {
                this.matchDIGIT();
            }
        }
    }
    
    protected void matchDIGITS() {
        this.matchDIGIT();
        while (this.isDigit(this.parser.peek(0))) {
            this.matchDIGIT();
        }
    }
    
    protected void matchDIGIT() {
        final int c = this.parser.peek(0);
        if (this.isDigit(c)) {
            this.match(c);
            return;
        }
        throw this.exception("expected digit (0..9), found " + Util.toPrintableString(c));
    }
    
    protected boolean peekCHARACTER() {
        final int c = this.parser.peek(0);
        return this.isLetter(c);
    }
    
    protected void matchCHARACTER() {
        final int c = this.parser.peek(0);
        if (this.isLetter(c)) {
            this.match(c);
            return;
        }
        throw this.exception("expected a character (a..z|A..Z), found " + Util.toPrintableString(c));
    }
    
    protected void matchNEWLINES() {
        this.matchNEWLINE();
        while (this.isNewLine(this.parser.peek(0))) {
            this.matchNEWLINE();
        }
    }
    
    protected void matchNEWLINE() {
        final int c = this.parser.peek(0);
        if (c == 10) {
            this.parser.consume();
        }
        else {
            if (c != 13) {
                throw this.exception("expected newline (\n, \r, or \r\n), found " + Util.toPrintableString(c));
            }
            this.parser.consume();
            if (this.parser.peek(0) == 10) {
                this.parser.consume();
            }
        }
        this.parser.resetColumn();
        ++this.lineNumber;
    }
    
    protected boolean isLetter(final int c) {
        return (c >= 97 && c <= 122) || (c >= 65 && c <= 90);
    }
    
    protected boolean isDigit(final int c) {
        return c >= 48 && c <= 57;
    }
    
    protected boolean isNewLine(final int c) {
        return c == 10 || c == 13;
    }
    
    protected void match(final int c) {
        this.parser.match((char)c);
        this.valueBuffer.append((char)c);
    }
}
