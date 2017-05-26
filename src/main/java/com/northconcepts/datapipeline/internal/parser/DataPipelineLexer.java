package com.northconcepts.datapipeline.internal.parser;

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

public class DataPipelineLexer extends CharScanner implements DataPipelineTreeParserTokenTypes, TokenStream {
	public static final BitSet _tokenSet_0;
	public static final BitSet _tokenSet_1;
	public static final BitSet _tokenSet_2;
	public static final BitSet _tokenSet_3;

	public DataPipelineLexer(final InputStream in) {
		this(new ByteBuffer(in));
	}

	public DataPipelineLexer(final Reader in) {
		this(new CharBuffer(in));
	}

	public DataPipelineLexer(final InputBuffer ib) {
		this(new LexerSharedInputState(ib));
	}

	public DataPipelineLexer(LexerSharedInputState state) {
		super(state);
		caseSensitiveLiterals = true;
		setCaseSensitive(false);
		literals = new Hashtable();
		literals.put(new ANTLRHashString("weeks", this), new Integer(46));
		literals.put(new ANTLRHashString("minutes", this), new Integer(43));
		literals.put(new ANTLRHashString("days", this), new Integer(45));
		literals.put(new ANTLRHashString("year", this), new Integer(40));
		literals.put(new ANTLRHashString("seconds", this), new Integer(42));
		literals.put(new ANTLRHashString("millisecond", this), new Integer(33));
		literals.put(new ANTLRHashString("date", this), new Integer(9));
		literals.put(new ANTLRHashString("minute", this), new Integer(35));
		literals.put(new ANTLRHashString("hours", this), new Integer(44));
		literals.put(new ANTLRHashString("timestamp", this), new Integer(11));
		literals.put(new ANTLRHashString("null", this), new Integer(8));
		literals.put(new ANTLRHashString("month", this), new Integer(39));
		literals.put(new ANTLRHashString("week", this), new Integer(38));
		literals.put(new ANTLRHashString("hour", this), new Integer(36));
		literals.put(new ANTLRHashString("or", this), new Integer(19));
		literals.put(new ANTLRHashString("xor", this), new Integer(22));
		literals.put(new ANTLRHashString("mod", this), new Integer(31));
		literals.put(new ANTLRHashString("years", this), new Integer(48));
		literals.put(new ANTLRHashString("no", this), new Integer(6));
		literals.put(new ANTLRHashString("day", this), new Integer(37));
		literals.put(new ANTLRHashString("true", this), new Integer(5));
		literals.put(new ANTLRHashString("milliseconds", this), new Integer(41));
		literals.put(new ANTLRHashString("not", this), new Integer(57));
		literals.put(new ANTLRHashString("and", this), new Integer(24));
		literals.put(new ANTLRHashString("yes", this), new Integer(7));
		literals.put(new ANTLRHashString("months", this), new Integer(47));
		literals.put(new ANTLRHashString("second", this), new Integer(34));
		literals.put(new ANTLRHashString("false", this), new Integer(4));
		literals.put(new ANTLRHashString("time", this), new Integer(10));
	}

	public Token nextToken() throws TokenStreamException {
		Token theRetToken=null;
		tryAgain:
			for (;;) {
				Token _token = null;
				int _ttype = Token.INVALID_TYPE;
				resetText();
				try {   // for char stream error handling
					try {   // for lexical error handling
						switch ( LA(1)) {
						case '(':
						{
							mLPAREN(true);
							theRetToken=_returnToken;
							break;
						}
						case ')':
						{
							mRPAREN(true);
							theRetToken=_returnToken;
							break;
						}
						case ',':
						{
							mCOMMA(true);
							theRetToken=_returnToken;
							break;
						}
						case ':':
						{
							mCOLON(true);
							theRetToken=_returnToken;
							break;
						}
						case '?':
						{
							mQUESTION_MARK(true);
							theRetToken=_returnToken;
							break;
						}
						case '/':
						{
							mDIVIDE(true);
							theRetToken=_returnToken;
							break;
						}
						case '%':
						{
							mREMAINDER(true);
							theRetToken=_returnToken;
							break;
						}
						case '+':
						{
							mADD(true);
							theRetToken=_returnToken;
							break;
						}
						case '-':
						{
							mSUBTRACT(true);
							theRetToken=_returnToken;
							break;
						}
						case '^':
						{
							mXOR(true);
							theRetToken=_returnToken;
							break;
						}
						case '$':  case '_':  case 'a':  case 'b':
						case 'c':  case 'd':  case 'e':  case 'f':
						case 'g':  case 'h':  case 'i':  case 'j':
						case 'k':  case 'l':  case 'm':  case 'n':
						case 'o':  case 'p':  case 'q':  case 'r':
						case 's':  case 't':  case 'u':  case 'v':
						case 'w':  case 'x':  case 'y':  case 'z':
						{
							mIDENTIFIER(true);
							theRetToken=_returnToken;
							break;
						}
						case '"':
						{
							mDOUBLE_QUOTED_STRING_LITERAL(true);
							theRetToken=_returnToken;
							break;
						}
						case '\'':
						{
							mSINGLE_QUOTED_STRING_LITERAL(true);
							theRetToken=_returnToken;
							break;
						}
						case '.':  case '0':  case '1':  case '2':
						case '3':  case '4':  case '5':  case '6':
						case '7':  case '8':  case '9':
						{
							mINTEGER_LITERAL(true);
							theRetToken=_returnToken;
							break;
						}
						case '\t':  case '\n':  case '\u000c':  case '\r':
						case ' ':
						{
							mWHITE_SPACE(true);
							theRetToken=_returnToken;
							break;
						}
						default:
							if ((LA(1)=='=') && (LA(2)=='=')) {
								mEQUAL_EQUAL(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='*') && (LA(2)=='*')) {
								mPOWER(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='!') && (LA(2)=='=')) {
								mNE(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='<') && (LA(2)=='>')) {
								mGTLT(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='>') && (LA(2)=='=')) {
								mGTE(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='<') && (LA(2)=='=')) {
								mLTE(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='|') && (LA(2)=='|')) {
								mLOGICAL_OR(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='&') && (LA(2)=='&')) {
								mLOGICAL_AND(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='=') && (true)) {
								mEQUAL(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='!') && (true)) {
								mNOT(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='*') && (true)) {
								mMULTIPLY(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='>') && (true)) {
								mGT(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='<') && (true)) {
								mLT(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='|') && (true)) {
								mBITWISE_OR(true);
								theRetToken=_returnToken;
							}
							else if ((LA(1)=='&') && (true)) {
								mBITWISE_AND(true);
								theRetToken=_returnToken;
							}
							else {
								if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
								else {throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());}
							}
						}
						if ( _returnToken==null ) continue tryAgain; // found SKIP token
						_ttype = _returnToken.getType();
						_returnToken.setType(_ttype);
						return _returnToken;
					}
					catch (RecognitionException e) {
						throw new TokenStreamRecognitionException(e);
					}
				}
				catch (CharStreamException cse) {
					if ( cse instanceof CharStreamIOException ) {
						throw new TokenStreamIOException(((CharStreamIOException)cse).io);
					}
					else {
						throw new TokenStreamException(cse.getMessage());
					}
				}
			}
	}


	public final void mLPAREN(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 18;
		this.match('(');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mRPAREN(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 64;
		this.match(')');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mCOMMA(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 63;
		this.match(',');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mCOLON(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 65;
		this.match(':');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mQUESTION_MARK(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 17;
		this.match('?');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mEQUAL(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 49;
		this.match('=');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mEQUAL_EQUAL(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 50;
		this.match("==");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mNOT(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 58;
		this.match('!');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mMULTIPLY(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 29;
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
		final int _ttype = 61;
		this.match("**");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mDIVIDE(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 30;
		this.match('/');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mREMAINDER(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 32;
		this.match('%');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mADD(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 27;
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
		final int _ttype = 28;
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
		final int _ttype = 51;
		this.match("!=");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mGTLT(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 52;
		this.match("<>");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mGT(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 53;
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
		final int _ttype = 54;
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
		final int _ttype = 55;
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
		final int _ttype = 56;
		this.match("<=");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mXOR(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 23;
		this.match('^');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mBITWISE_OR(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 20;
		this.match('|');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mLOGICAL_OR(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 21;
		this.match("||");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mBITWISE_AND(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 25;
		this.match('&');
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mLOGICAL_AND(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 26;
		this.match("&&");
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	public final void mIDENTIFIER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = IDENTIFIER;
		
		switch ( LA(1)) {
		case '_':  case 'a':  case 'b':  case 'c':
		case 'd':  case 'e':  case 'f':  case 'g':
		case 'h':  case 'i':  case 'j':  case 'k':
		case 'l':  case 'm':  case 'n':  case 'o':
		case 'p':  case 'q':  case 'r':  case 's':
		case 't':  case 'u':  case 'v':  case 'w':
		case 'x':  case 'y':  case 'z':
		{
			{
				switch ( LA(1)) {
				case 'a':  case 'b':  case 'c':  case 'd':
				case 'e':  case 'f':  case 'g':  case 'h':
				case 'i':  case 'j':  case 'k':  case 'l':
				case 'm':  case 'n':  case 'o':  case 'p':
				case 'q':  case 'r':  case 's':  case 't':
				case 'u':  case 'v':  case 'w':  case 'x':
				case 'y':  case 'z':
				{
					matchRange('a','z');
					break;
				}
				case '_':
				{
					match('_');
					break;
				}
				default:
				{
					throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
				}
				}
			}
			{
				_loop134:
					do {
						switch ( LA(1)) {
						case 'a':  case 'b':  case 'c':  case 'd':
						case 'e':  case 'f':  case 'g':  case 'h':
						case 'i':  case 'j':  case 'k':  case 'l':
						case 'm':  case 'n':  case 'o':  case 'p':
						case 'q':  case 'r':  case 's':  case 't':
						case 'u':  case 'v':  case 'w':  case 'x':
						case 'y':  case 'z':
						{
							matchRange('a','z');
							break;
						}
						case '_':
						{
							match('_');
							break;
						}
						case '.':
						{
							match('.');
							break;
						}
						case '0':  case '1':  case '2':  case '3':
						case '4':  case '5':  case '6':  case '7':
						case '8':  case '9':
						{
							matchRange('0','9');
							break;
						}
						default:
						{
							break _loop134;
						}
						}
					} while (true);
			}
			break;
		}
		case '$':
		{
			{
				match('$');
				match('{');
			}
			{
				_loop137:
					do {
						if ((_tokenSet_0.member(LA(1)))) {
							matchNot('}');
					}
					else {
						break _loop137;
					}

					} while (true);
			}
			match('}');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
		}
		}
		_ttype = testLiteralsTable(_ttype);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}


	public final void mDOUBLE_QUOTED_STRING_LITERAL(final boolean _createToken) throws RecognitionException, CharStreamException,
			TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 14;
		int _saveIndex = this.text.length();
		this.match('\"');
		this.text.setLength(_saveIndex);
		while (true) {
			if (this.LA(1) == '\\') {
				this.mESCAPE_SEQUENCE(false);
			} else {
				if (!DataPipelineLexer._tokenSet_1.member(this.LA(1))) {
					break;
				}
				this.match(DataPipelineLexer._tokenSet_1);
			}
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

	protected final void mESCAPE_SEQUENCE(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 68;
		this.match('\\');
		switch (this.LA(1)) {
		case 'n': {
			this.match('n');
			break;
		}
		case 'r': {
			this.match('r');
			break;
		}
		case 't': {
			this.match('t');
			break;
		}
		case 'b': {
			this.match('b');
			break;
		}
		case 'f': {
			this.match('f');
			break;
		}
		case '\"': {
			this.match('\"');
			break;
		}
		case '\'': {
			this.match('\'');
			break;
		}
		case '\\': {
			this.match('\\');
			break;
		}
		case 'u': {
			int _cnt161 = 0;
			while (this.LA(1) == 'u') {
				this.match('u');
				++_cnt161;
			}
			if (_cnt161 >= 1) {
				this.mHEX_DIGIT(false);
				this.mHEX_DIGIT(false);
				this.mHEX_DIGIT(false);
				this.mHEX_DIGIT(false);
				break;
			}
			throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
		}
		case '0':
		case '1':
		case '2':
		case '3': {
			this.matchRange('0', '3');
			if (this.LA(1) >= '0' && this.LA(1) <= '7' && DataPipelineLexer._tokenSet_2.member(this.LA(2))) {
				this.matchRange('0', '7');
				if (this.LA(1) >= '0' && this.LA(1) <= '7' && DataPipelineLexer._tokenSet_2.member(this.LA(2))) {
					this.matchRange('0', '7');
					break;
				}
				if (DataPipelineLexer._tokenSet_2.member(this.LA(1))) {
					break;
				}
				throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
			} else {
				if (DataPipelineLexer._tokenSet_2.member(this.LA(1))) {
					break;
				}
				throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
			}
		}
		case '4':
		case '5':
		case '6':
		case '7': {
			this.matchRange('4', '7');
			if (this.LA(1) >= '0' && this.LA(1) <= '7' && DataPipelineLexer._tokenSet_2.member(this.LA(2))) {
				this.matchRange('0', '7');
				break;
			}
			if (DataPipelineLexer._tokenSet_2.member(this.LA(1))) {
				break;
			}
			throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
		}
		default: {
			throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
		}
		}
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
		final int _ttype = 15;
		int _saveIndex = this.text.length();
		this.match('\'');
		this.text.setLength(_saveIndex);
		while (true) {
			if (this.LA(1) == '\\') {
				this.mESCAPE_SEQUENCE(false);
			} else {
				if (!DataPipelineLexer._tokenSet_3.member(this.LA(1))) {
					break;
				}
				this.match(DataPipelineLexer._tokenSet_3);
			}
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
		int _ttype = 12;
		switch (this.LA(1)) {
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
			if (this.LA(1) == '.') {
				this.match('.');
				this.mDIGITS(false);
				_ttype = 13;
				break;
			}
			break;
		}
		case '.': {
			this.match('.');
			this.mDIGITS(false);
			_ttype = 13;
			break;
		}
		default: {
			throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
		}
		}
		if (this.LA(1) == 'e') {
			this.match('e');
			switch (this.LA(1)) {
			case '+': {
				this.match('+');
				break;
			}
			case '-': {
				this.match('-');
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
				break;
			}
			default: {
				throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
			}
			}
			this.mDIGITS(false);
			_ttype = 13;
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
		final int _ttype = 66;
		int _cnt154 = 0;
		while (this.LA(1) >= '0' && this.LA(1) <= '9') {
			this.matchRange('0', '9');
			++_cnt154;
		}
		if (_cnt154 >= 1) {
			if (_createToken && _token == null && _ttype != -1) {
				_token = this.makeToken(_ttype);
				_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
			}
			this._returnToken = _token;
			return;
		}
		throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
	}

	public final void mWHITE_SPACE(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		int _ttype = 67;
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

	protected final void mHEX_DIGIT(final boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		Token _token = null;
		final int _begin = this.text.length();
		final int _ttype = 69;
		switch (this.LA(1)) {
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
			break;
		}
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f': {
			this.matchRange('a', 'f');
			break;
		}
		default: {
			throw new NoViableAltForCharException(this.LA(1), this.getFilename(), this.getLine(), this.getColumn());
		}
		}
		if (_createToken && _token == null && _ttype != -1) {
			_token = this.makeToken(_ttype);
			_token.setText(new String(this.text.getBuffer(), _begin, this.text.length() - _begin));
		}
		this._returnToken = _token;
	}

	private static final long[] mk_tokenSet_0() {
		final long[] data = new long[8];
		data[0] = -1L;
		data[1] = -2305843009213693953L;
		for (int i = 2; i <= 3; ++i) {
			data[i] = -1L;
		}
		return data;
	}

	private static final long[] mk_tokenSet_1() {
		final long[] data = new long[8];
		data[0] = -17179878401L;
		data[1] = -268435457L;
		for (int i = 2; i <= 3; ++i) {
			data[i] = -1L;
		}
		return data;
	}

	private static final long[] mk_tokenSet_2() {
		final long[] data = new long[8];
		data[0] = -9217L;
		for (int i = 1; i <= 3; ++i) {
			data[i] = -1L;
		}
		return data;
	}

	private static final long[] mk_tokenSet_3() {
		final long[] data = new long[8];
		data[0] = -549755823105L;
		data[1] = -268435457L;
		for (int i = 2; i <= 3; ++i) {
			data[i] = -1L;
		}
		return data;
	}

	static {
		_tokenSet_0 = new BitSet(mk_tokenSet_0());
		_tokenSet_1 = new BitSet(mk_tokenSet_1());
		_tokenSet_2 = new BitSet(mk_tokenSet_2());
		_tokenSet_3 = new BitSet(mk_tokenSet_3());
	}
}
