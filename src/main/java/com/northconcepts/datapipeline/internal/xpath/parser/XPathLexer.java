package com.northconcepts.datapipeline.internal.xpath.parser;

import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

import antlr.ANTLRHashString;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharScanner;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.NoViableAltForCharException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.collections.impl.BitSet;

public class XPathLexer extends CharScanner implements XPathTreeParserTokenTypes, TokenStream {
	public static final BitSet _tokenSet_0;
	public static final BitSet _tokenSet_1;

	public XPathLexer(final InputStream in) {
		this(new ByteBuffer(in));
	}

	public XPathLexer(final Reader in) {
		this(new CharBuffer(in));
	}

	public XPathLexer(final InputBuffer ib) {
		this(new LexerSharedInputState(ib));
	}

	public XPathLexer(final LexerSharedInputState state) {
		super(state);
		this.caseSensitiveLiterals = true;
		this.setCaseSensitive(false);
		(this.literals = new Hashtable()).put(new ANTLRHashString("ancestor", this), new Integer(12));
		this.literals.put(new ANTLRHashString("preceding-sibling", this), new Integer(23));
		this.literals.put(new ANTLRHashString("self", this), new Integer(24));
		this.literals.put(new ANTLRHashString("ancestor-or-self", this), new Integer(13));
		this.literals.put(new ANTLRHashString("text", this), new Integer(27));
		this.literals.put(new ANTLRHashString("processing-instruction", this), new Integer(28));
		this.literals.put(new ANTLRHashString("following-sibling", this), new Integer(19));
		this.literals.put(new ANTLRHashString("descendant", this), new Integer(16));
		this.literals.put(new ANTLRHashString("namespace", this), new Integer(20));
		this.literals.put(new ANTLRHashString("attribute", this), new Integer(14));
		this.literals.put(new ANTLRHashString("parent", this), new Integer(21));
		this.literals.put(new ANTLRHashString("child", this), new Integer(15));
		this.literals.put(new ANTLRHashString("descendant-or-self", this), new Integer(17));
		this.literals.put(new ANTLRHashString("node", this), new Integer(29));
		this.literals.put(new ANTLRHashString("following", this), new Integer(18));
		this.literals.put(new ANTLRHashString("preceding", this), new Integer(22));
		this.literals.put(new ANTLRHashString("comment", this), new Integer(26));
	}

	public Token nextToken() throws TokenStreamException {
		Token theRetToken = null;
		while (true) {
			final Token _token = null;
			int _ttype = 0;
			this.resetText();
			try {
				try {
					switch (this.LA(1)) {
					case '_':
					case 'a':
					case 'b':
					case 'c':
					case 'd':
					case 'e':
					case 'f':
					case 'g':
					case 'h':
					case 'i':
					case 'j':
					case 'k':
					case 'l':
					case 'm':
					case 'n':
					case 'o':
					case 'p':
					case 'q':
					case 'r':
					case 's':
					case 't':
					case 'u':
					case 'v':
					case 'w':
					case 'x':
					case 'y':
					case 'z':
					case '¡':
					case '¢':
					case '£':
					case '¤':
					case '¥':
					case '¦':
					case '§':
					case '¨':
					case '©':
					case 'ª':
					case '«':
					case '¬':
					case '\u00ad':
					case '®':
					case '¯':
					case '°':
					case '±':
					case '²':
					case '³':
					case '´':
					case 'µ':
					case '¶':
					case '·':
					case '¸':
					case '¹':
					case 'º':
					case '»':
					case '¼':
					case '½':
					case '¾':
					case '¿':
					case '\u00c0':
					case '\u00c1':
					case '\u00c2':
					case '\u00c3':
					case '\u00c4':
					case '\u00c5':
					case '\u00c6':
					case '\u00c7':
					case '\u00c8':
					case '\u00c9':
					case '\u00ca':
					case '\u00cb':
					case '\u00cc':
					case '\u00cd':
					case '\u00ce':
					case '\u00cf':
					case '\u00d0':
					case '\u00d1':
					case '\u00d2':
					case '\u00d3':
					case '\u00d4':
					case '\u00d5':
					case '\u00d6':
					case '\u00d7':
					case '\u00d8':
					case '\u00d9':
					case '\u00da':
					case '\u00db':
					case '\u00dc':
					case '\u00dd':
					case '\u00de':
					case '\u00df':
					case '\u00e0':
					case '\u00e1':
					case '\u00e2':
					case '\u00e3':
					case '\u00e4':
					case '\u00e5':
					case '\u00e6':
					case '\u00e7':
					case '\u00e8':
					case '\u00e9':
					case '\u00ea':
					case '\u00eb':
					case '\u00ec':
					case '\u00ed':
					case '\u00ee':
					case '\u00ef':
					case '\u00f0':
					case '\u00f1':
					case '\u00f2':
					case '\u00f3':
					case '\u00f4':
					case '\u00f5':
					case '\u00f6':
					case '\u00f7':
					case '\u00f8':
					case '\u00f9':
					case '\u00fa':
					case '\u00fb':
					case '\u00fc':
					case '\u00fd':
					case '\u00fe':
					case '\u00ff': {
						this.mIDENTIFIER(true);
						theRetToken = this._returnToken;
						break;
					}
					case '\"': {
						this.mDOUBLE_QUOTED_STRING_LITERAL(true);
						theRetToken = this._returnToken;
						break;
					}
					case '\'': {
						this.mSINGLE_QUOTED_STRING_LITERAL(true);
						theRetToken = this._returnToken;
						break;
					}
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9': {
						this.mINTEGER_LITERAL(true);
						theRetToken = this._returnToken;
						break;
					}
					case '.': {
						this.mDOT(true);
						theRetToken = this._returnToken;
						break;
					}
					case '\t':
					case '\n':
					case '\f':
					case '\r':
					case ' ': {
						this.mWHITE_SPACE(true);
						theRetToken = this._returnToken;
						break;
					}
					case '(': {
						this.mLEFT_PAREN(true);
						theRetToken = this._returnToken;
						break;
					}
					case ')': {
						this.mRIGHT_PAREN(true);
						theRetToken = this._returnToken;
						break;
					}
					case '[': {
						this.mLEFT_BRACKET(true);
						theRetToken = this._returnToken;
						break;
					}
					case ']': {
						this.mRIGHT_BRACKET(true);
						theRetToken = this._returnToken;
						break;
					}
					case ',': {
						this.mCOMMA(true);
						theRetToken = this._returnToken;
						break;
					}
					case '=': {
						this.mEQUAL(true);
						theRetToken = this._returnToken;
						break;
					}
					case '+': {
						this.mADD(true);
						theRetToken = this._returnToken;
						break;
					}
					case '-': {
						this.mSUBTRACT(true);
						theRetToken = this._returnToken;
						break;
					}
					case '!': {
						this.mNE(true);
						theRetToken = this._returnToken;
						break;
					}
					case '|': {
						this.mPIPE(true);
						theRetToken = this._returnToken;
						break;
					}
					case '@': {
						this.mAT(true);
						theRetToken = this._returnToken;
						break;
					}
					case '$': {
						this.mDOLLAR_SIGN(true);
						theRetToken = this._returnToken;
						break;
					}
					default: {
						if (this.LA(1) == '*' && this.LA(2) == '*') {
							this.mPOWER(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == '>' && this.LA(2) == '=') {
							this.mGTE(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == '<' && this.LA(2) == '=') {
							this.mLTE(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == ':' && this.LA(2) == ':') {
							this.mCOLON_COLON(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == '/' && this.LA(2) == '/') {
							this.mSLASH_SLASH(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == '*') {
							this.mSTAR(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == '>') {
							this.mGT(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == '<') {
							this.mLT(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == ':') {
							this.mCOLON(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == '/') {
							this.mSLASH(true);
							theRetToken = this._returnToken;
							break;
						}
						if (this.LA(1) == '\uffff') {
							this.uponEOF();
							this._returnToken = this.makeToken(1);
							break;
						}
						throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
					}
					}
					if (this._returnToken == null) {
						continue;
					}
					_ttype = this._returnToken.getType();
					this._returnToken.setType(_ttype);
					return this._returnToken;
				} catch (RecognitionException e) {
					throw new TokenStreamRecognitionException(e);
				}
			} catch (CharStreamException cse) {
				if (cse instanceof CharStreamIOException) {
					throw new TokenStreamIOException(((CharStreamIOException) cse).io);
				}
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}

	public final void mIDENTIFIER(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		int _ttype = 37;
		switch (this.LA(1)) {
		case '¡':
		case '¢':
		case '£':
		case '¤':
		case '¥':
		case '¦':
		case '§':
		case '¨':
		case '©':
		case 'ª':
		case '«':
		case '¬':
		case '\u00ad':
		case '®':
		case '¯':
		case '°':
		case '±':
		case '²':
		case '³':
		case '´':
		case 'µ':
		case '¶':
		case '·':
		case '¸':
		case '¹':
		case 'º':
		case '»':
		case '¼':
		case '½':
		case '¾':
		case '¿':
		case '\u00c0':
		case '\u00c1':
		case '\u00c2':
		case '\u00c3':
		case '\u00c4':
		case '\u00c5':
		case '\u00c6':
		case '\u00c7':
		case '\u00c8':
		case '\u00c9':
		case '\u00ca':
		case '\u00cb':
		case '\u00cc':
		case '\u00cd':
		case '\u00ce':
		case '\u00cf':
		case '\u00d0':
		case '\u00d1':
		case '\u00d2':
		case '\u00d3':
		case '\u00d4':
		case '\u00d5':
		case '\u00d6':
		case '\u00d7':
		case '\u00d8':
		case '\u00d9':
		case '\u00da':
		case '\u00db':
		case '\u00dc':
		case '\u00dd':
		case '\u00de':
		case '\u00df':
		case '\u00e0':
		case '\u00e1':
		case '\u00e2':
		case '\u00e3':
		case '\u00e4':
		case '\u00e5':
		case '\u00e6':
		case '\u00e7':
		case '\u00e8':
		case '\u00e9':
		case '\u00ea':
		case '\u00eb':
		case '\u00ec':
		case '\u00ed':
		case '\u00ee':
		case '\u00ef':
		case '\u00f0':
		case '\u00f1':
		case '\u00f2':
		case '\u00f3':
		case '\u00f4':
		case '\u00f5':
		case '\u00f6':
		case '\u00f7':
		case '\u00f8':
		case '\u00f9':
		case '\u00fa':
		case '\u00fb':
		case '\u00fc':
		case '\u00fd':
		case '\u00fe':
		case '\u00ff': {
			this.matchRange('¡', '\u00ff');
			break;
		}
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z': {
			this.matchRange('a', 'z');
			break;
		}
		case '_': {
			this.match('_');
			break;
		}
		default: {
			throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
		}
		}
		while (true) {
			switch (this.LA(1)) {
			case '¡':
			case '¢':
			case '£':
			case '¤':
			case '¥':
			case '¦':
			case '§':
			case '¨':
			case '©':
			case 'ª':
			case '«':
			case '¬':
			case '\u00ad':
			case '®':
			case '¯':
			case '°':
			case '±':
			case '²':
			case '³':
			case '´':
			case 'µ':
			case '¶':
			case '·':
			case '¸':
			case '¹':
			case 'º':
			case '»':
			case '¼':
			case '½':
			case '¾':
			case '¿':
			case '\u00c0':
			case '\u00c1':
			case '\u00c2':
			case '\u00c3':
			case '\u00c4':
			case '\u00c5':
			case '\u00c6':
			case '\u00c7':
			case '\u00c8':
			case '\u00c9':
			case '\u00ca':
			case '\u00cb':
			case '\u00cc':
			case '\u00cd':
			case '\u00ce':
			case '\u00cf':
			case '\u00d0':
			case '\u00d1':
			case '\u00d2':
			case '\u00d3':
			case '\u00d4':
			case '\u00d5':
			case '\u00d6':
			case '\u00d7':
			case '\u00d8':
			case '\u00d9':
			case '\u00da':
			case '\u00db':
			case '\u00dc':
			case '\u00dd':
			case '\u00de':
			case '\u00df':
			case '\u00e0':
			case '\u00e1':
			case '\u00e2':
			case '\u00e3':
			case '\u00e4':
			case '\u00e5':
			case '\u00e6':
			case '\u00e7':
			case '\u00e8':
			case '\u00e9':
			case '\u00ea':
			case '\u00eb':
			case '\u00ec':
			case '\u00ed':
			case '\u00ee':
			case '\u00ef':
			case '\u00f0':
			case '\u00f1':
			case '\u00f2':
			case '\u00f3':
			case '\u00f4':
			case '\u00f5':
			case '\u00f6':
			case '\u00f7':
			case '\u00f8':
			case '\u00f9':
			case '\u00fa':
			case '\u00fb':
			case '\u00fc':
			case '\u00fd':
			case '\u00fe':
			case '\u00ff': {
				this.matchRange('¡', '\u00ff');
				continue;
			}
			case 'a':
			case 'b':
			case 'c':
			case 'd':
			case 'e':
			case 'f':
			case 'g':
			case 'h':
			case 'i':
			case 'j':
			case 'k':
			case 'l':
			case 'm':
			case 'n':
			case 'o':
			case 'p':
			case 'q':
			case 'r':
			case 's':
			case 't':
			case 'u':
			case 'v':
			case 'w':
			case 'x':
			case 'y':
			case 'z': {
				this.matchRange('a', 'z');
				continue;
			}
			case '-': {
				this.match('-');
				continue;
			}
			case '_': {
				this.match('_');
				continue;
			}
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9': {
				this.matchRange('0', '9');
				continue;
			}
			case '.': {
				this.match('.');
				continue;
			}
			default: {
				_ttype = this.testLiteralsTable(_ttype);
				if (_createToken && _token == null && _ttype != -1) {
					_token = this.makeToken(_ttype);
					_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
				}
				this._returnToken = _token;
			}
			}
		}
	}

	public final void mDOUBLE_QUOTED_STRING_LITERAL(final boolean _createToken) throws RecognitionException, CharStreamException,
			TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 61;
		int _saveIndex = this.text.length();
		this.match('\"');
		this.text.setLength(_saveIndex);
		while (XPathLexer._tokenSet_0.member(this.LA(1))) {
			this.match(XPathLexer._tokenSet_0);
		}
		_saveIndex = this.text.length();
		this.match('\"');
		this.text.setLength(_saveIndex);
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mSINGLE_QUOTED_STRING_LITERAL(final boolean _createToken) throws RecognitionException, CharStreamException,
			TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 60;
		int _saveIndex = this.text.length();
		this.match('\'');
		this.text.setLength(_saveIndex);
		while (XPathLexer._tokenSet_1.member(this.LA(1))) {
			this.match(XPathLexer._tokenSet_1);
		}
		_saveIndex = this.text.length();
		this.match('\'');
		this.text.setLength(_saveIndex);
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mINTEGER_LITERAL(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		int _ttype = 62;
		this.mDIGITS(false);
		if (this.LA(1) == '.') {
			this.match('.');
			this.mDIGITS(false);
			_ttype = 42;
		}
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	protected final void mDIGITS(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 63;
		int _cnt94 = 0;
		while (this.LA(1) >= '0' && this.LA(1) <= '9') {
			this.matchRange('0', '9');
			++_cnt94;
		}
		if (_cnt94 >= 1) {
			if (_createToken && _token == null && _ttype != -1) {
				_token = this.makeToken(_ttype);
				_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
			}
			this._returnToken = _token;
			return;
		}
		throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
	}

	public final void mDOT(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		int _ttype = 52;
		this.match('.');
		switch (this.LA(1)) {
		case '.': {
			this.match('.');
			_ttype = 43;
			break;
		}
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9': {
			this.mDIGITS(false);
			_ttype = 42;
			break;
		}
		}
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mWHITE_SPACE(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		int _ttype = 64;
		switch (this.LA(1)) {
		case ' ': {
			this.match(' ');
			break;
		}
		case '\t': {
			this.match('\t');
			break;
		}
		case '\f': {
			this.match('\f');
			break;
		}
		case '\n':
		case '\r': {
			if (this.LA(1) == '\r' && this.LA(2) == '\n') {
				this.match("\r\n");
			} else if (this.LA(1) == '\r') {
				this.match('\r');
			} else {
				if (this.LA(1) != '\n') {
					throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
				}
				this.match('\n');
			}
			this.newline();
			break;
		}
		default: {
			throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
		}
		}
		_ttype = -1;
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mLEFT_PAREN(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 55;
		this.match('(');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mRIGHT_PAREN(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 56;
		this.match(')');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mLEFT_BRACKET(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 58;
		this.match('[');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mRIGHT_BRACKET(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 59;
		this.match(']');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mCOMMA(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 65;
		this.match(',');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mEQUAL(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 66;
		this.match('=');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mSTAR(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 38;
		this.match("*");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mPOWER(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 67;
		this.match("**");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mADD(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 68;
		this.match('+');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mSUBTRACT(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 69;
		this.match('-');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mNE(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 70;
		this.match("!=");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mGT(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 71;
		this.match(">");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mGTE(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 72;
		this.match(">=");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mLT(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 73;
		this.match("<");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mLTE(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 74;
		this.match("<=");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mPIPE(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 49;
		this.match('|');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mAT(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 54;
		this.match('@');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mCOLON_COLON(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 53;
		this.match("::");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mCOLON(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 57;
		this.match(':');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mSLASH(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 50;
		this.match('/');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mSLASH_SLASH(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 51;
		this.match("//");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mDOLLAR_SIGN(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 75;
		this.match('$');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	private static final long[] mk_tokenSet_0() {
		final long[] data = new long[8];
		data[0] = -17179869185L;
		for (int i = 1; i <= 3; ++i) {
			data[i] = -1L;
		}
		return data;
	}

	private static final long[] mk_tokenSet_1() {
		final long[] data = new long[8];
		data[0] = -549755813889L;
		for (int i = 1; i <= 3; ++i) {
			data[i] = -1L;
		}
		return data;
	}

	static {
		_tokenSet_0 = new BitSet(mk_tokenSet_0());
		_tokenSet_1 = new BitSet(mk_tokenSet_1());
	}
}
