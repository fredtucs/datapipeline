package com.northconcepts.datapipeline.internal.parser;

import java.util.ArrayList;

import antlr.NoViableAltException;
import antlr.RecognitionException;
import antlr.TreeParser;
import antlr.collections.AST;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.MethodCallExpression;
import com.northconcepts.datapipeline.internal.expression.QuantityExpression;
import com.northconcepts.datapipeline.internal.expression.arithmetic.ArithmeticCoercionExpression;
import com.northconcepts.datapipeline.internal.expression.arithmetic.DivisionExpression;
import com.northconcepts.datapipeline.internal.expression.arithmetic.ExponentialExpression;
import com.northconcepts.datapipeline.internal.expression.arithmetic.LiteralArithmeticExpression;
import com.northconcepts.datapipeline.internal.expression.arithmetic.ModulusExpression;
import com.northconcepts.datapipeline.internal.expression.arithmetic.MultiplicationExpression;
import com.northconcepts.datapipeline.internal.expression.arithmetic.NegativeQuantityExpression;
import com.northconcepts.datapipeline.internal.expression.arithmetic.PositiveQuantityExpression;
import com.northconcepts.datapipeline.internal.expression.character.CharacterCoercionExpression;
import com.northconcepts.datapipeline.internal.expression.character.LiteralCharacterExpression;
import com.northconcepts.datapipeline.internal.expression.logical.AndExpression;
import com.northconcepts.datapipeline.internal.expression.logical.EqualToExpression;
import com.northconcepts.datapipeline.internal.expression.logical.LiteralLogicalExpression;
import com.northconcepts.datapipeline.internal.expression.logical.LogicalCoercionExpression;
import com.northconcepts.datapipeline.internal.expression.logical.NotEqualToExpression;
import com.northconcepts.datapipeline.internal.expression.logical.NotExpression;
import com.northconcepts.datapipeline.internal.expression.logical.OrExpression;
import com.northconcepts.datapipeline.internal.expression.logical.XorExpression;
import com.northconcepts.datapipeline.internal.expression.temporal.LiteralIntervalExpression;
import com.northconcepts.datapipeline.internal.expression.temporal.LiteralMomentExpression;
import com.northconcepts.datapipeline.internal.expression.untyped.UntypedAdditionExpression;
import com.northconcepts.datapipeline.internal.expression.untyped.UntypedGreaterThanExpression;
import com.northconcepts.datapipeline.internal.expression.untyped.UntypedGreaterThanOrEqualToExpression;
import com.northconcepts.datapipeline.internal.expression.untyped.UntypedLessThanExpression;
import com.northconcepts.datapipeline.internal.expression.untyped.UntypedLessThanOrEqualToExpression;
import com.northconcepts.datapipeline.internal.expression.untyped.UntypedNullExpression;
import com.northconcepts.datapipeline.internal.expression.untyped.UntypedSubtractionExpression;
import com.northconcepts.datapipeline.internal.expression.untyped.UntypedVariableExpression;

public class DataPipelineTreeParser extends TreeParser implements DataPipelineTreeParserTokenTypes
{
    public static final String[] _tokenNames;
    
    public DataPipelineTreeParser() {
        super();
        this.tokenNames = DataPipelineTreeParser._tokenNames;
    }
    
    public final Expression expression(AST _t) throws RecognitionException {
        Expression e = null;
        final AST expression_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
        AST method = null;
        final AST ast = _t;
        if (_t == null) {
            _t = TreeParser.ASTNULL;
        }
        Label_4179: {
            switch (_t.getType()) {
                case 4: {
                    final AST tmp1_AST_in = _t;
                    this.match(_t, 4);
                    _t = _t.getNextSibling();
                    e = new LiteralLogicalExpression(false, ast.getText());
                    break Label_4179;
                }
                case 5: {
                    final AST tmp2_AST_in = _t;
                    this.match(_t, 5);
                    _t = _t.getNextSibling();
                    e = new LiteralLogicalExpression(true, ast.getText());
                    break Label_4179;
                }
                case 6: {
                    final AST tmp3_AST_in = _t;
                    this.match(_t, 6);
                    _t = _t.getNextSibling();
                    e = new LiteralLogicalExpression(false, ast.getText());
                    break Label_4179;
                }
                case 7: {
                    final AST tmp4_AST_in = _t;
                    this.match(_t, 7);
                    _t = _t.getNextSibling();
                    e = new LiteralLogicalExpression(true, ast.getText());
                    break Label_4179;
                }
                case 8: {
                    final AST tmp5_AST_in = _t;
                    this.match(_t, 8);
                    _t = _t.getNextSibling();
                    e = new UntypedNullExpression();
                    break Label_4179;
                }
                case 9: {
                    final AST __t2 = _t;
                    final AST tmp6_AST_in = _t;
                    this.match(_t, 9);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    _t = __t2;
                    _t = _t.getNextSibling();
                    e = new LiteralMomentExpression("date", new CharacterCoercionExpression(a));
                    break Label_4179;
                }
                case 10: {
                    final AST __t2 = _t;
                    final AST tmp7_AST_in = _t;
                    this.match(_t, 10);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    _t = __t2;
                    _t = _t.getNextSibling();
                    e = new LiteralMomentExpression("time", new CharacterCoercionExpression(a));
                    break Label_4179;
                }
                case 11: {
                    final AST __t3 = _t;
                    final AST tmp8_AST_in = _t;
                    this.match(_t, 11);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    _t = __t3;
                    _t = _t.getNextSibling();
                    e = new LiteralMomentExpression("timestamp", new CharacterCoercionExpression(a));
                    break Label_4179;
                }
                case 12: {
                    final AST tmp9_AST_in = _t;
                    this.match(_t, 12);
                    _t = _t.getNextSibling();
                    e = new LiteralArithmeticExpression(Long.parseLong(ast.getText()), ast.getText());
                    break Label_4179;
                }
                case 13: {
                    final AST tmp10_AST_in = _t;
                    this.match(_t, 13);
                    _t = _t.getNextSibling();
                    e = new LiteralArithmeticExpression(Double.parseDouble(ast.getText()), ast.getText());
                    break Label_4179;
                }
                case 14: {
                    final AST tmp11_AST_in = _t;
                    this.match(_t, 14);
                    _t = _t.getNextSibling();
                    e = new LiteralCharacterExpression(ast.getText(), "\"");
                    break Label_4179;
                }
                case 15: {
                    final AST tmp12_AST_in = _t;
                    this.match(_t, 15);
                    _t = _t.getNextSibling();
                    e = new LiteralCharacterExpression(ast.getText(), "'");
                    break Label_4179;
                }
                case 16:
                case 17: {
                    if (_t == null) {
                        _t = TreeParser.ASTNULL;
                    }
                    switch (_t.getType()) {
                        case 16: {
                            final AST tmp13_AST_in = _t;
                            this.match(_t, 16);
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 17: {
                            final AST tmp14_AST_in = _t;
                            this.match(_t, 17);
                            _t = _t.getNextSibling();
                            break;
                        }
                        default: {
                            throw new NoViableAltException(_t);
                        }
                    }
                    e = new UntypedVariableExpression(ast.getText());
                    break Label_4179;
                }
                case 18: {
                    final AST __t4 = _t;
                    final AST tmp15_AST_in = _t;
                    this.match(_t, 18);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    _t = __t4;
                    _t = _t.getNextSibling();
                    e = new QuantityExpression(a);
                    break Label_4179;
                }
                case 19:
                case 20:
                case 21: {
                    if (_t == null) {
                        _t = TreeParser.ASTNULL;
                    }
                    Expression a = null;
                    Expression b = null;
                    switch (_t.getType()) {
                        case 19: {
                            final AST __t5 = _t;
                            final AST tmp16_AST_in = _t;
                            this.match(_t, 19);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t5;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 20: {
                            final AST __t6 = _t;
                            final AST tmp17_AST_in = _t;
                            this.match(_t, 20);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t6;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 21: {
                            final AST __t7 = _t;
                            final AST tmp18_AST_in = _t;
                            this.match(_t, 21);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t7;
                            _t = _t.getNextSibling();
                            break;
                        }
                        default: {
                            throw new NoViableAltException(_t);
                        }
                    }
                    e = new OrExpression(new LogicalCoercionExpression(a), new LogicalCoercionExpression(b), ast.getText());
                    break Label_4179;
                }
                case 22:
                case 23: {
                    if (_t == null) {
                        _t = TreeParser.ASTNULL;
                    }
                    Expression a = null;
                    Expression b = null;
                    switch (_t.getType()) {
                        case 22: {
                            final AST __t8 = _t;
                            final AST tmp19_AST_in = _t;
                            this.match(_t, 22);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t8;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 23: {
                            final AST __t9 = _t;
                            final AST tmp20_AST_in = _t;
                            this.match(_t, 23);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t9;
                            _t = _t.getNextSibling();
                            break;
                        }
                        default: {
                            throw new NoViableAltException(_t);
                        }
                    }
                    e = new XorExpression(new LogicalCoercionExpression(a), new LogicalCoercionExpression(b), ast.getText());
                    break Label_4179;
                }
                case 24:
                case 25:
                case 26: {
                    if (_t == null) {
                        _t = TreeParser.ASTNULL;
                    }
                    Expression a = null;
                    Expression b = null;
                    switch (_t.getType()) {
                        case 24: {
                            final AST __t10 = _t;
                            final AST tmp21_AST_in = _t;
                            this.match(_t, 24);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t10;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 25: {
                            final AST __t11 = _t;
                            final AST tmp22_AST_in = _t;
                            this.match(_t, 25);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t11;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 26: {
                            final AST __t12 = _t;
                            final AST tmp23_AST_in = _t;
                            this.match(_t, 26);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t12;
                            _t = _t.getNextSibling();
                            break;
                        }
                        default: {
                            throw new NoViableAltException(_t);
                        }
                    }
                    e = new AndExpression(new LogicalCoercionExpression(a), new LogicalCoercionExpression(b), ast.getText());
                    break Label_4179;
                }
                case 27: {
                    final AST __t13 = _t;
                    final AST tmp24_AST_in = _t;
                    this.match(_t, 27);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    final Expression b = this.expression(_t);
                    _t = this._retTree;
                    _t = __t13;
                    _t = _t.getNextSibling();
                    e = new UntypedAdditionExpression(a, b);
                    break Label_4179;
                }
                case 28: {
                    final AST __t14 = _t;
                    final AST tmp25_AST_in = _t;
                    this.match(_t, 28);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    final Expression b = this.expression(_t);
                    _t = this._retTree;
                    _t = __t14;
                    _t = _t.getNextSibling();
                    e = new UntypedSubtractionExpression(a, b);
                    break Label_4179;
                }
                case 29: {
                    final AST __t15 = _t;
                    final AST tmp26_AST_in = _t;
                    this.match(_t, 29);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    final Expression b = this.expression(_t);
                    _t = this._retTree;
                    _t = __t15;
                    _t = _t.getNextSibling();
                    e = new MultiplicationExpression(new ArithmeticCoercionExpression(a), new ArithmeticCoercionExpression(b));
                    break Label_4179;
                }
                case 30: {
                    final AST __t16 = _t;
                    final AST tmp27_AST_in = _t;
                    this.match(_t, 30);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    final Expression b = this.expression(_t);
                    _t = this._retTree;
                    _t = __t16;
                    _t = _t.getNextSibling();
                    e = new DivisionExpression(new ArithmeticCoercionExpression(a), new ArithmeticCoercionExpression(b));
                    break Label_4179;
                }
                case 31:
                case 32: {
                    if (_t == null) {
                        _t = TreeParser.ASTNULL;
                    }
                    Expression a = null;
                    Expression b = null;
                    switch (_t.getType()) {
                        case 31: {
                            final AST __t17 = _t;
                            final AST tmp28_AST_in = _t;
                            this.match(_t, 31);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t17;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 32: {
                            final AST __t18 = _t;
                            final AST tmp29_AST_in = _t;
                            this.match(_t, 32);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t18;
                            _t = _t.getNextSibling();
                            break;
                        }
                        default: {
                            throw new NoViableAltException(_t);
                        }
                    }
                    e = new ModulusExpression(new ArithmeticCoercionExpression(a), new ArithmeticCoercionExpression(b));
                    break Label_4179;
                }
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48: {
                    if (_t == null) {
                        _t = TreeParser.ASTNULL;
                    }
                    Expression a = null;
                    switch (_t.getType()) {
                        case 33: {
                            final AST __t19 = _t;
                            final AST tmp30_AST_in = _t;
                            this.match(_t, 33);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t19;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 34: {
                            final AST __t20 = _t;
                            final AST tmp31_AST_in = _t;
                            this.match(_t, 34);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t20;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 35: {
                            final AST __t21 = _t;
                            final AST tmp32_AST_in = _t;
                            this.match(_t, 35);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t21;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 36: {
                            final AST __t22 = _t;
                            final AST tmp33_AST_in = _t;
                            this.match(_t, 36);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t22;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 37: {
                            final AST __t23 = _t;
                            final AST tmp34_AST_in = _t;
                            this.match(_t, 37);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t23;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 38: {
                            final AST __t24 = _t;
                            final AST tmp35_AST_in = _t;
                            this.match(_t, 38);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t24;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 39: {
                            final AST __t25 = _t;
                            final AST tmp36_AST_in = _t;
                            this.match(_t, 39);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t25;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 40: {
                            final AST __t26 = _t;
                            final AST tmp37_AST_in = _t;
                            this.match(_t, 40);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t26;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 41: {
                            final AST __t27 = _t;
                            final AST tmp38_AST_in = _t;
                            this.match(_t, 41);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t27;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 42: {
                            final AST __t28 = _t;
                            final AST tmp39_AST_in = _t;
                            this.match(_t, 42);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t28;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 43: {
                            final AST __t29 = _t;
                            final AST tmp40_AST_in = _t;
                            this.match(_t, 43);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t29;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 44: {
                            final AST __t30 = _t;
                            final AST tmp41_AST_in = _t;
                            this.match(_t, 44);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t30;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 45: {
                            final AST __t31 = _t;
                            final AST tmp42_AST_in = _t;
                            this.match(_t, 45);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t31;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 46: {
                            final AST __t32 = _t;
                            final AST tmp43_AST_in = _t;
                            this.match(_t, 46);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t32;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 47: {
                            final AST __t33 = _t;
                            final AST tmp44_AST_in = _t;
                            this.match(_t, 47);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t33;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 48: {
                            final AST __t34 = _t;
                            final AST tmp45_AST_in = _t;
                            this.match(_t, 48);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t34;
                            _t = _t.getNextSibling();
                            break;
                        }
                        default: {
                            throw new NoViableAltException(_t);
                        }
                    }
                    e = new LiteralIntervalExpression(new ArithmeticCoercionExpression(a), ast.getText());
                    break Label_4179;
                }
                case 49:
                case 50: {
                    if (_t == null) {
                        _t = TreeParser.ASTNULL;
                    }
                    Expression a = null;
                    Expression b = null;
                    switch (_t.getType()) {
                        case 49: {
                            final AST __t35 = _t;
                            final AST tmp46_AST_in = _t;
                            this.match(_t, 49);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t35;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 50: {
                            final AST __t36 = _t;
                            final AST tmp47_AST_in = _t;
                            this.match(_t, 50);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t36;
                            _t = _t.getNextSibling();
                            break;
                        }
                        default: {
                            throw new NoViableAltException(_t);
                        }
                    }
                    e = new EqualToExpression(a, b, ast.getText());
                    break Label_4179;
                }
                case 51:
                case 52: {
                    if (_t == null) {
                        _t = TreeParser.ASTNULL;
                    }
                    Expression a = null;
                    Expression b = null;
                    switch (_t.getType()) {
                        case 51: {
                            final AST __t37 = _t;
                            final AST tmp48_AST_in = _t;
                            this.match(_t, 51);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t37;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 52: {
                            final AST __t38 = _t;
                            final AST tmp49_AST_in = _t;
                            this.match(_t, 52);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            b = this.expression(_t);
                            _t = this._retTree;
                            _t = __t38;
                            _t = _t.getNextSibling();
                            break;
                        }
                        default: {
                            throw new NoViableAltException(_t);
                        }
                    }
                    e = new NotEqualToExpression(a, b, ast.getText());
                    break Label_4179;
                }
                case 53: {
                    final AST __t39 = _t;
                    final AST tmp50_AST_in = _t;
                    this.match(_t, 53);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    final Expression b = this.expression(_t);
                    _t = this._retTree;
                    _t = __t39;
                    _t = _t.getNextSibling();
                    e = new UntypedGreaterThanExpression(a, b);
                    break Label_4179;
                }
                case 54: {
                    final AST __t40 = _t;
                    final AST tmp51_AST_in = _t;
                    this.match(_t, 54);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    final Expression b = this.expression(_t);
                    _t = this._retTree;
                    _t = __t40;
                    _t = _t.getNextSibling();
                    e = new UntypedGreaterThanOrEqualToExpression(a, b);
                    break Label_4179;
                }
                case 55: {
                    final AST __t41 = _t;
                    final AST tmp52_AST_in = _t;
                    this.match(_t, 55);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    final Expression b = this.expression(_t);
                    _t = this._retTree;
                    _t = __t41;
                    _t = _t.getNextSibling();
                    e = new UntypedLessThanExpression(a, b);
                    break Label_4179;
                }
                case 56: {
                    final AST __t42 = _t;
                    final AST tmp53_AST_in = _t;
                    this.match(_t, 56);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    final Expression b = this.expression(_t);
                    _t = this._retTree;
                    _t = __t42;
                    _t = _t.getNextSibling();
                    e = new UntypedLessThanOrEqualToExpression(a, b);
                    break Label_4179;
                }
                case 57:
                case 58: {
                    if (_t == null) {
                        _t = TreeParser.ASTNULL;
                    }
                    Expression a = null;
                    switch (_t.getType()) {
                        case 57: {
                            final AST __t43 = _t;
                            final AST tmp54_AST_in = _t;
                            this.match(_t, 57);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t43;
                            _t = _t.getNextSibling();
                            break;
                        }
                        case 58: {
                            final AST __t44 = _t;
                            final AST tmp55_AST_in = _t;
                            this.match(_t, 58);
                            _t = _t.getFirstChild();
                            a = this.expression(_t);
                            _t = this._retTree;
                            _t = __t44;
                            _t = _t.getNextSibling();
                            break;
                        }
                        default: {
                            throw new NoViableAltException(_t);
                        }
                    }
                    e = new NotExpression(new LogicalCoercionExpression(a), ast.getText());
                    break Label_4179;
                }
                case 59: {
                    final AST __t45 = _t;
                    final AST tmp56_AST_in = _t;
                    this.match(_t, 59);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    _t = __t45;
                    _t = _t.getNextSibling();
                    e = new PositiveQuantityExpression(new ArithmeticCoercionExpression(a));
                    break Label_4179;
                }
                case 60: {
                    final AST __t46 = _t;
                    final AST tmp57_AST_in = _t;
                    this.match(_t, 60);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    _t = __t46;
                    _t = _t.getNextSibling();
                    e = new NegativeQuantityExpression(new ArithmeticCoercionExpression(a));
                    break Label_4179;
                }
                case 61: {
                    final AST __t47 = _t;
                    final AST tmp58_AST_in = _t;
                    this.match(_t, 61);
                    _t = _t.getFirstChild();
                    final Expression a = this.expression(_t);
                    _t = this._retTree;
                    final Expression b = this.expression(_t);
                    _t = this._retTree;
                    _t = __t47;
                    _t = _t.getNextSibling();
                    e = new ExponentialExpression(new ArithmeticCoercionExpression(a), new ArithmeticCoercionExpression(b));
                    break Label_4179;
                }
                case 62: {
                    final AST __t48 = _t;
                    final AST tmp59_AST_in = _t;
                    this.match(_t, 62);
                    _t = (method = _t.getFirstChild());
                    this.match(_t, 16);
                    _t = _t.getNextSibling();
                    final ArrayList list = this.expressionList(_t);
                    _t = this._retTree;
                    _t = __t48;
                    _t = _t.getNextSibling();
                    try {
                        final MethodCallExpression methodCall = new MethodCallExpression(method.getText());
                        if (list.size() > 0) {
                            methodCall.addArguments((Expression[])list.toArray(new Expression[list.size()]));
                        }
                        e = methodCall;                        
                    }
                    catch (Throwable throwable) {
                        throw DataException.wrap(throwable);
                    }
                    break Label_4179;
                }
            }
            throw new NoViableAltException(_t);
        }
        this._retTree = _t;
        return e;
    }
    
    public final ArrayList expressionList(AST _t) throws RecognitionException {
        final AST expressionList_AST_in = (_t == TreeParser.ASTNULL) ? null : _t;
        final ArrayList list = new ArrayList();
        while (true) {
            if (_t == null) {
                _t = TreeParser.ASTNULL;
            }
            if (_t.getType() < 4 || _t.getType() > 62) {
                break;
            }
            final Expression e = this.expression(_t);
            _t = this._retTree;
            list.add(e);
        }
        this._retTree = _t;
        return list;
    }
    
    static {
        _tokenNames = new String[] { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "\"false\"", "\"true\"", "\"no\"", "\"yes\"", "\"null\"", "\"date\"", "\"time\"", "\"timestamp\"", "a number", "DECIMAL_LITERAL", "a string", "a string", "an identifier", "QUESTION_MARK", "LPAREN", "\"or\"", "BITWISE_OR", "LOGICAL_OR", "\"xor\"", "XOR", "\"and\"", "BITWISE_AND", "LOGICAL_AND", "ADD", "SUBTRACT", "MULTIPLY", "DIVIDE", "\"mod\"", "REMAINDER", "\"millisecond\"", "\"second\"", "\"minute\"", "\"hour\"", "\"day\"", "\"week\"", "\"month\"", "\"year\"", "\"milliseconds\"", "\"seconds\"", "\"minutes\"", "\"hours\"", "\"days\"", "\"weeks\"", "\"months\"", "\"years\"", "EQUAL", "EQUAL_EQUAL", "NE", "GTLT", "GT", "GTE", "LT", "LTE", "\"not\"", "NOT", "PLUS_SIGN", "MINUS_SIGN", "POWER", "METHOD_CALL", "COMMA", "RPAREN", "COLON", "numbers (0-9)", "a space", "ESCAPE_SEQUENCE", "a hexadecimal digit (0-9 or a-f)" };
    }
}
