package com.northconcepts.datapipeline.csv;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.StringParser;
import com.northconcepts.datapipeline.core.TextReader;
import com.northconcepts.datapipeline.internal.lang.Util;

public class CSVReader extends TextReader {
	private char fieldSeparator;
	private char quoteChar;
	private String newLine;
	private boolean allowMultiLineText;
	private boolean trimFields;
	private String lineText;
	private StringParser lineParser;
	private StringBuilder valueBuffer;

	public static Record parse(final String line, final char fieldSeparator, final char quoteChar, final boolean allowMultiLineText) {
		final StringReader stringReader = new StringReader(line);
		final CSVReader reader = new CSVReader(stringReader);
		reader.setFieldSeparator(fieldSeparator);
		reader.setQuoteChar(quoteChar);
		reader.setAllowMultiLineText(allowMultiLineText);
		reader.open();
		try {
			final Record record = reader.read();
			return record;
		} finally {
			reader.close();
		}
	}

	public CSVReader(final File file) {
		super(file);
		this.fieldSeparator = ',';
		this.quoteChar = '\"';
		this.newLine = System.getProperty("line.separator", "\n");
		this.allowMultiLineText = false;
		this.trimFields = true;
		this.valueBuffer = new StringBuilder(128);
	}

	public CSVReader(final Reader reader) {
		super(reader);
		this.fieldSeparator = ',';
		this.quoteChar = '\"';
		this.newLine = System.getProperty("line.separator", "\n");
		this.allowMultiLineText = false;
		this.trimFields = true;
		this.valueBuffer = new StringBuilder(128);
	}

	public char getFieldSeparator() {
		return this.fieldSeparator;
	}

	public CSVReader setFieldSeparator(final char columnSeparator) {
		this.assertNotOpened();
		this.fieldSeparator = columnSeparator;
		return this;
	}

	public char getQuoteChar() {
		return this.quoteChar;
	}

	public CSVReader setQuoteChar(final char quoteChar) {
		this.assertNotOpened();
		this.quoteChar = quoteChar;
		return this;
	}

	public String getNewLine() {
		return this.newLine;
	}

	public CSVReader setNewLine(final String newLine) {
		this.assertNotOpened();
		this.newLine = newLine;
		return this;
	}

	public boolean isAllowMultiLineText() {
		return this.allowMultiLineText;
	}

	public CSVReader setAllowMultiLineText(final boolean allowMultiLineText) {
		this.assertNotOpened();
		this.allowMultiLineText = allowMultiLineText;
		return this;
	}

	public boolean isTrimFields() {
		return this.trimFields;
	}

	public CSVReader setTrimFields(final boolean trimFields) {
		this.assertNotOpened();
		this.trimFields = trimFields;
		return this;
	}

	@Override
	public DataException addExceptionProperties(final DataException exception) {
		exception.set("CSVReader.fieldSeparator", this.fieldSeparator);
		exception.set("CSVReader.quoteChar", this.quoteChar);
		exception.set("CSVReader.newLine", this.newLine);
		exception.set("CSVReader.allowMultiLineText", this.allowMultiLineText);
		exception.set("CSVReader.trimFields", this.trimFields);
		if (this.lineParser != null) {
			exception.set("CSVReader.column", this.lineParser.getColumn());
		}
		exception.set("CSVReader.lineText", this.lineText);
		exception.setRecord(this.currentRecord);
		return super.addExceptionProperties(exception);
	}

	@Override
	protected boolean fillRecord(final Record record) throws Throwable {
		if (!this.readLineText()) {
			return false;
		}
		int i = 0;
		while (this.lineParser.peek(0) != -1) {
			record.getField(i, true).setValue(this.matchValue());
			if (this.lineParser.peek(0) == this.fieldSeparator) {
				this.lineParser.consume();
				if (this.lineParser.peek(0) == -1) {
					record.getField(i + 1, true).setValue((String) null);
				}
			} else if (this.lineParser.peek(0) != -1) {
				final int c = this.lineParser.peek(0);
				final String printableChar = Util.toPrintableString(c);
				throw new DataException("expected \"" + this.fieldSeparator + "\" found \"" + printableChar + "\"").set("char", (char) c).set(
						"printable char", printableChar);
			}
			++i;
		}
		return true;
	}

	private boolean readLineText() throws IOException {
		this.lineText = this.reader.readLine();
		if (this.lineText == null) {
			return false;
		}
		this.lineParser = new StringParser(this.lineText);
		return true;
	}

	private String matchValue() throws IOException {
		if (this.trimFields) {
			this.lineParser.consumeWhitespace();
		}
		final int c = this.lineParser.peek(0);
		if (c == this.quoteChar) {
			return this.matchString((char) c);
		}
		return this.matchNonString();
	}

	private String matchString(final char quoteChar) throws IOException {
		this.valueBuffer.setLength(0);
		this.lineParser.match(quoteChar);
		while (true) {
			final int c = this.lineParser.peek(0);
			if (c == quoteChar) {
				if (this.lineParser.peek(1) != quoteChar) {
					break;
				}
				this.valueBuffer.append(quoteChar);
				this.lineParser.consume(2);
			} else if (c == -1) {
				if (!this.allowMultiLineText || !this.readLineText()) {
					break;
				}
				this.valueBuffer.append(this.newLine);
			} else {
				this.valueBuffer.append((char) c);
				this.lineParser.consume();
			}
		}
		this.lineParser.match(quoteChar, "unterminated string");
		this.lineParser.consumeWhitespace(this.fieldSeparator);
		return this.valueBuffer.toString();
	}

	private String matchNonString() {
		this.valueBuffer.setLength(0);
		for (int c = this.lineParser.peek(0); c != this.fieldSeparator && c != -1; c = this.lineParser.peek(0)) {
			this.valueBuffer.append((char) this.lineParser.pop());
		}
		String value = this.valueBuffer.toString();
		if (this.trimFields) {
			value = value.trim();
		}
		return (value.length() == 0) ? null : value;
	}
}
