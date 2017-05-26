package com.northconcepts.datapipeline.internal.xpath.parser;

import javax.xml.namespace.QName;

import antlr.NoViableAltException;
import antlr.RecognitionException;
import antlr.TreeParser;
import antlr.collections.AST;

import com.northconcepts.datapipeline.internal.xpath.Axis;
import com.northconcepts.datapipeline.internal.xpath.LocationPath;
import com.northconcepts.datapipeline.internal.xpath.LocationPathType;
import com.northconcepts.datapipeline.internal.xpath.NameTest;
import com.northconcepts.datapipeline.internal.xpath.NodeTest;
import com.northconcepts.datapipeline.internal.xpath.NodeTypeTest;
import com.northconcepts.datapipeline.internal.xpath.Predicate;
import com.northconcepts.datapipeline.internal.xpath.Step;

public class XPathTreeParser extends TreeParser implements XPathTreeParserTokenTypes {
	public static final String[] _tokenNames;

	public XPathTreeParser() {
		super();
		this.tokenNames = XPathTreeParser._tokenNames;
	}

	public final LocationPath locationPathPart(AST _t) throws RecognitionException {
		LocationPath l = null;
		final AST locationPathPart_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		if (_t == null) {
			_t = TreeParser.ASTNULL;
		}
		switch (_t.getType()) {
		case 4:
		case 5: {
			l = this.absoluteLocationPath(_t);
			_t = this._retTree;
			break;
		}
		case 8:
		case 9:
		case 10:
		case 11:
		case 25:
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
		case 35: {
			l = this.relativeLocationPath(_t);
			_t = this._retTree;
			break;
		}
		default: {
			throw new NoViableAltException(_t);
		}
		}
		this._retTree = _t;
		return l;
	}

	public final LocationPath absoluteLocationPath(AST _t) throws RecognitionException {
		LocationPath l = null;
		final AST absoluteLocationPath_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		if (_t == null) {
			_t = TreeParser.ASTNULL;
		}
		switch (_t.getType()) {
		case 4: {
			final AST __t3 = _t;
			final AST tmp59_AST_in = _t;
			this.match(_t, 4);
			_t = _t.getFirstChild();
			l = this.relativeLocationPath(_t);
			_t = this._retTree;
			_t = __t3;
			_t = _t.getNextSibling();
			l.setType(LocationPathType.ABSOLUTE);
			break;
		}
		case 5: {
			final AST __t2 = _t;
			final AST tmp60_AST_in = _t;
			this.match(_t, 5);
			_t = _t.getFirstChild();
			l = this.relativeLocationPath(_t);
			_t = this._retTree;
			_t = __t2;
			_t = _t.getNextSibling();
			l.setType(LocationPathType.ABSOLUTE_DESCENDANTS);
			break;
		}
		default: {
			throw new NoViableAltException(_t);
		}
		}
		this._retTree = _t;
		return l;
	}

	public final LocationPath relativeLocationPath(AST _t) throws RecognitionException {
		final LocationPath l = new LocationPath();
		final AST relativeLocationPath_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		final AST ast = _t;
		l.setType(LocationPathType.RELATIVE);
		Step s = this.step(_t);
		_t = this._retTree;
		l.addStep(s);
		while (true) {
			if (_t == null) {
				_t = TreeParser.ASTNULL;
			}
			switch (_t.getType()) {
			case 6: {
				final AST tmp61_AST_in = _t;
				this.match(_t, 6);
				_t = _t.getNextSibling();
				final Axis a = Axis.CHILD;
				s = this.step(_t);
				_t = this._retTree;
				l.addStep(s);
				s.setDefaultAxis(a);
				continue;
			}
			case 7: {
				final AST tmp62_AST_in = _t;
				this.match(_t, 7);
				_t = _t.getNextSibling();
				final Axis a = Axis.DESCENDANT;
				s = this.step(_t);
				_t = this._retTree;
				l.addStep(s);
				s.setDefaultAxis(a);
				continue;
			}
			default: {
				this._retTree = _t;
				return l;
			}
			}
		}
	}

	public final Step step(AST _t) throws RecognitionException {
		Step step = new Step();
		AST step_AST_in = _t != ASTNULL ? _t : null;
		AST ast = _t;
		if (_t == null)
			_t = ASTNULL;
		label0: switch (_t.getType()) {
		case 10: // '\n'
		case 11: // '\013'
		{
			Axis a = axisSpecifier(_t);
			_t = _retTree;
			NodeTest n = nodeTest(_t);
			_t = _retTree;
			do {
				if (_t == null)
					_t = ASTNULL;
				if (_t.getType() != 36)
					break;
				Predicate p = predicate(_t);
				_t = _retTree;
				step.getPredicates().add(p);
			} while (true);
			step.setAxis(a);
			step.setNodeTest(n);
			break;
		}

		case 25: // '\031'
		case 30: // '\036'
		case 31: // '\037'
		case 32: // ' '
		case 33: // '!'
		case 34: // '"'
		case 35: // '#'
		{
			NodeTest n = nodeTest(_t);
			_t = _retTree;
			do {
				if (_t == null)
					_t = ASTNULL;
				if (_t.getType() != 36)
					break;
				Predicate p = predicate(_t);
				_t = _retTree;
				step.getPredicates().add(p);
			} while (true);
			step.setNodeTest(n);
			break;
		}

		case 8: // '\b'
		case 9: // '\t'
		{
			if (_t == null)
				_t = ASTNULL;
			switch (_t.getType()) {
			case 8: // '\b'
				AST tmp63_AST_in = _t;
				match(_t, 8);
				_t = _t.getNextSibling();
				step.setAxis(Axis.PARENT);
				break;

			case 9: // '\t'
				AST tmp64_AST_in = _t;
				match(_t, 9);
				_t = _t.getNextSibling();
				step.setAxis(Axis.SELF);
				break;

			default:
				throw new NoViableAltException(_t);
			}
			do {
				if (_t == null)
					_t = ASTNULL;
				if (_t.getType() != 36)
					break label0;
				Predicate p = predicate(_t);
				_t = _retTree;
				step.getPredicates().add(p);
			} while (true);
		}

		case 12: // '\f'
		case 13: // '\r'
		case 14: // '\016'
		case 15: // '\017'
		case 16: // '\020'
		case 17: // '\021'
		case 18: // '\022'
		case 19: // '\023'
		case 20: // '\024'
		case 21: // '\025'
		case 22: // '\026'
		case 23: // '\027'
		case 24: // '\030'
		case 26: // '\032'
		case 27: // '\033'
		case 28: // '\034'
		case 29: // '\035'
		default: {
			throw new NoViableAltException(_t);
		}
		}
		_retTree = _t;
		return step;
	}

	public final Axis axisSpecifier(AST _t) throws RecognitionException {
		Axis axis = null;
		final AST axisSpecifier_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		final AST ast = _t;
		if (_t == null) {
			_t = TreeParser.ASTNULL;
		}
		switch (_t.getType()) {
		case 10: {
			final AST __t19 = _t;
			final AST tmp65_AST_in = _t;
			this.match(_t, 10);
			_t = _t.getFirstChild();
			final String a = this.axisName(_t);
			_t = this._retTree;
			_t = __t19;
			_t = _t.getNextSibling();
			axis = Axis.lookup(a);
			break;
		}
		case 11: {
			final AST tmp66_AST_in = _t;
			this.match(_t, 11);
			_t = _t.getNextSibling();
			axis = Axis.ATTRIBUTE;
			break;
		}
		default: {
			throw new NoViableAltException(_t);
		}
		}
		this._retTree = _t;
		return axis;
	}

	public final NodeTest nodeTest(AST _t) throws RecognitionException {
		NodeTest test = null;
		final AST nodeTest_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		final AST ast = _t;
		if (_t == null) {
			_t = TreeParser.ASTNULL;
		}
		switch (_t.getType()) {
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
		case 35: {
			test = this.nameTest(_t);
			_t = this._retTree;
			break;
		}
		case 25: {
			final AST __t23 = _t;
			final AST tmp67_AST_in = _t;
			this.match(_t, 25);
			_t = _t.getFirstChild();
			final String a = this.nodeTypeName(_t);
			_t = this._retTree;
			_t = __t23;
			_t = _t.getNextSibling();
			test = new NodeTypeTest(a);
			break;
		}
		default: {
			throw new NoViableAltException(_t);
		}
		}
		this._retTree = _t;
		return test;
	}

	public final Predicate predicate(AST _t) throws RecognitionException {
		final Predicate predicate = new Predicate() {
		};
		final AST predicate_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		final AST ast = _t;
		final AST tmp68_AST_in = _t;
		this.match(_t, 36);
		_t = _t.getNextSibling();
		this._retTree = _t;
		return predicate;
	}

	public final String axisName(AST _t) throws RecognitionException {
		String s = null;
		final AST axisName_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		final AST ast = _t;
		if (_t == null) {
			_t = TreeParser.ASTNULL;
		}
		switch (_t.getType()) {
		case 12: {
			final AST tmp69_AST_in = _t;
			this.match(_t, 12);
			_t = _t.getNextSibling();
			break;
		}
		case 13: {
			final AST tmp70_AST_in = _t;
			this.match(_t, 13);
			_t = _t.getNextSibling();
			break;
		}
		case 14: {
			final AST tmp71_AST_in = _t;
			this.match(_t, 14);
			_t = _t.getNextSibling();
			break;
		}
		case 15: {
			final AST tmp72_AST_in = _t;
			this.match(_t, 15);
			_t = _t.getNextSibling();
			break;
		}
		case 16: {
			final AST tmp73_AST_in = _t;
			this.match(_t, 16);
			_t = _t.getNextSibling();
			break;
		}
		case 17: {
			final AST tmp74_AST_in = _t;
			this.match(_t, 17);
			_t = _t.getNextSibling();
			break;
		}
		case 18: {
			final AST tmp75_AST_in = _t;
			this.match(_t, 18);
			_t = _t.getNextSibling();
			break;
		}
		case 19: {
			final AST tmp76_AST_in = _t;
			this.match(_t, 19);
			_t = _t.getNextSibling();
			break;
		}
		case 20: {
			final AST tmp77_AST_in = _t;
			this.match(_t, 20);
			_t = _t.getNextSibling();
			break;
		}
		case 21: {
			final AST tmp78_AST_in = _t;
			this.match(_t, 21);
			_t = _t.getNextSibling();
			break;
		}
		case 22: {
			final AST tmp79_AST_in = _t;
			this.match(_t, 22);
			_t = _t.getNextSibling();
			break;
		}
		case 23: {
			final AST tmp80_AST_in = _t;
			this.match(_t, 23);
			_t = _t.getNextSibling();
			break;
		}
		case 24: {
			final AST tmp81_AST_in = _t;
			this.match(_t, 24);
			_t = _t.getNextSibling();
			break;
		}
		default: {
			throw new NoViableAltException(_t);
		}
		}
		s = ast.getText();
		this._retTree = _t;
		return s;
	}

	public final NameTest nameTest(AST _t) throws RecognitionException {
		NameTest name = null;
		final AST nameTest_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		final AST ast = _t;
		if (_t == null) {
			_t = TreeParser.ASTNULL;
		}
		switch (_t.getType()) {
		case 30: {
			final AST tmp82_AST_in = _t;
			this.match(_t, 30);
			_t = _t.getNextSibling();
			name = new NameTest(new QName("*"));
			break;
		}
		case 31: {
			final AST tmp83_AST_in = _t;
			this.match(_t, 31);
			_t = _t.getNextSibling();
			name = new NameTest(new QName(ast.getText()));
			break;
		}
		case 32: {
			final AST __t27 = _t;
			final AST tmp84_AST_in = _t;
			this.match(_t, 32);
			_t = _t.getFirstChild();
			final String a = this.identifier(_t);
			_t = this._retTree;
			final String b = this.identifier(_t);
			_t = this._retTree;
			_t = __t27;
			_t = _t.getNextSibling();
			name = new NameTest(new QName(a, b));
			break;
		}
		case 33: {
			final AST __t2 = _t;
			final AST tmp85_AST_in = _t;
			this.match(_t, 33);
			_t = _t.getFirstChild();
			final String a = this.identifier(_t);
			_t = this._retTree;
			_t = __t2;
			_t = _t.getNextSibling();
			name = new NameTest(new QName(null, a));
			break;
		}
		case 34: {
			final AST __t3 = _t;
			final AST tmp86_AST_in = _t;
			this.match(_t, 34);
			_t = _t.getFirstChild();
			final String a = this.identifier(_t);
			_t = this._retTree;
			final String b = this.identifier(_t);
			_t = this._retTree;
			_t = __t3;
			_t = _t.getNextSibling();
			name = new NameTest(new QName(a, b));
			break;
		}
		case 35: {
			final AST __t4 = _t;
			final AST tmp87_AST_in = _t;
			this.match(_t, 35);
			_t = _t.getFirstChild();
			final String a = this.identifier(_t);
			_t = this._retTree;
			_t = __t4;
			_t = _t.getNextSibling();
			name = new NameTest(new QName(null, a));
			break;
		}
		default: {
			throw new NoViableAltException(_t);
		}
		}
		this._retTree = _t;
		return name;
	}

	public final String nodeTypeName(AST _t) throws RecognitionException {
		String s = null;
		final AST nodeTypeName_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		final AST ast = _t;
		if (_t == null) {
			_t = TreeParser.ASTNULL;
		}
		switch (_t.getType()) {
		case 26: {
			final AST tmp88_AST_in = _t;
			this.match(_t, 26);
			_t = _t.getNextSibling();
			break;
		}
		case 27: {
			final AST tmp89_AST_in = _t;
			this.match(_t, 27);
			_t = _t.getNextSibling();
			break;
		}
		case 28: {
			final AST tmp90_AST_in = _t;
			this.match(_t, 28);
			_t = _t.getNextSibling();
			break;
		}
		case 29: {
			final AST tmp91_AST_in = _t;
			this.match(_t, 29);
			_t = _t.getNextSibling();
			break;
		}
		default: {
			throw new NoViableAltException(_t);
		}
		}
		s = ast.getText();
		this._retTree = _t;
		return s;
	}

	public final String identifier(AST _t) throws RecognitionException {
		String id = null;
		final AST identifier_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
		final AST ast = _t;
		if (_t == null) {
			_t = TreeParser.ASTNULL;
		}
		switch (_t.getType()) {
		case 37: {
			final AST tmp92_AST_in = _t;
			this.match(_t, 37);
			_t = _t.getNextSibling();
			id = ast.getText();
			break;
		}
		case 38: {
			final AST tmp93_AST_in = _t;
			this.match(_t, 38);
			_t = _t.getNextSibling();
			id = ast.getText();
			break;
		}
		default: {
			throw new NoViableAltException(_t);
		}
		}
		this._retTree = _t;
		return id;
	}

	static {
		_tokenNames = new String[] { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "ABSOLUTE_LOCATION_PATH", "ABSOLUTE_DESCENDANT_LOCATION_PATH",
				"CHILD_AXIS", "DESCENDANT_AXIS", "REVERSE_AXIS", "SELF_AXIS", "AXIS_SPECIFIER", "ABBREVIATED_ATTRIBUTE_AXIS", "\"ancestor\"",
				"\"ancestor-or-self\"", "\"attribute\"", "\"child\"", "\"descendant\"", "\"descendant-or-self\"", "\"following\"",
				"\"following-sibling\"", "\"namespace\"", "\"parent\"", "\"preceding\"", "\"preceding-sibling\"", "\"self\"", "NODE_TYPE",
				"\"comment\"", "\"text\"", "\"processing-instruction\"", "\"node\"", "UNPREFIXED_WILDCARD", "UNPREFIXED_QNAME", "PREFIXED_WILDCARD",
				"NULL_PREFIXED_WILDCARD", "PREFIXED_QNAME", "NULL_PREFIXED_QNAME", "PREDICATE", "an identifier", "STAR", "NEGAIVE_QUANTITY",
				"PLUS_SIGN", "METHOD_CALL", "DECIMAL_LITERAL", "DOT_DOT", "MULTIPLY", "DIVIDE", "RELATIVE_LOCATION_PATH", "STEP",
				"PROCESSING_INSTRUCTION", "PIPE", "SLASH", "SLASH_SLASH", "DOT", "COLON_COLON", "AT", "LEFT_PAREN", "RIGHT_PAREN", "COLON",
				"LEFT_BRACKET", "RIGHT_BRACKET", "a string", "a string", "a number", "numbers (0-9)", "whitespace", "COMMA", "EQUAL", "POWER", "ADD",
				"SUBTRACT", "NE", "GT", "GTE", "LT", "LTE", "DOLLAR_SIGN" };
	}
}
