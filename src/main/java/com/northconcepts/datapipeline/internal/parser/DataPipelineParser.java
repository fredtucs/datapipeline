package com.northconcepts.datapipeline.internal.parser;

import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.LLkParser;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.AST;
import antlr.collections.impl.BitSet;

public class DataPipelineParser extends LLkParser implements DataPipelineTreeParserTokenTypes
{
    public static final String[] _tokenNames;
    public static final BitSet _tokenSet_0;
    
    protected DataPipelineParser(final TokenBuffer tokenBuf, final int k) {
        super(tokenBuf, k);
        this.tokenNames = DataPipelineParser._tokenNames;
        this.buildTokenTypeASTClassMap();
        this.astFactory = new ASTFactory(this.getTokenTypeToASTClassMap());
    }
    
    public DataPipelineParser(final TokenBuffer tokenBuf) {
        this(tokenBuf, 2);
    }
    
    protected DataPipelineParser(final TokenStream lexer, final int k) {
        super(lexer, k);
        this.tokenNames = DataPipelineParser._tokenNames;
        this.buildTokenTypeASTClassMap();
        this.astFactory = new ASTFactory(this.getTokenTypeToASTClassMap());
    }
    
    public DataPipelineParser(final TokenStream lexer) {
        this(lexer, 2);
    }
    
    public DataPipelineParser(final ParserSharedInputState state) {
        super(state, 2);
        this.tokenNames = DataPipelineParser._tokenNames;
        this.buildTokenTypeASTClassMap();
        this.astFactory = new ASTFactory(this.getTokenTypeToASTClassMap());
    }
    
    public final void imaginaryTokens() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST imaginaryTokens_AST = null;
        AST tmp60_AST = null;
        tmp60_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp60_AST);
        this.match(60);
        AST tmp61_AST = null;
        tmp61_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp61_AST);
        this.match(59);
        AST tmp62_AST = null;
        tmp62_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp62_AST);
        this.match(62);
        AST tmp63_AST = null;
        tmp63_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp63_AST);
        this.match(13);
        imaginaryTokens_AST = currentAST.root;
        this.returnAST = imaginaryTokens_AST;
    }
    
    public final void expression2() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST expression2_AST = null;
        this.expression();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        this.match(1);
        expression2_AST = currentAST.root;
        this.returnAST = expression2_AST;
    }
    
    public final void expression() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST expression_AST = null;
        this.booleanExpression();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        expression_AST = currentAST.root;
        this.returnAST = expression_AST;
    }
    
    public final void expressionList() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST expressionList_AST = null;
        this.expression();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        while (this.LA(1) == 63) {
            this.match(63);
            this.expression();
            this.astFactory.addASTChild(currentAST, this.returnAST);
        }
        expressionList_AST = currentAST.root;
        this.returnAST = expressionList_AST;
    }
    
    public final void booleanExpression() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST booleanExpression_AST = null;
        this.andExpression();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        while (this.LA(1) >= 19 && this.LA(1) <= 23) {
            switch (this.LA(1)) {
                case 20: {
                    AST tmp66_AST = null;
                    tmp66_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp66_AST);
                    this.match(20);
                    break;
                }
                case 21: {
                    AST tmp67_AST = null;
                    tmp67_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp67_AST);
                    this.match(21);
                    break;
                }
                case 19: {
                    AST tmp68_AST = null;
                    tmp68_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp68_AST);
                    this.match(19);
                    break;
                }
                case 22: {
                    AST tmp69_AST = null;
                    tmp69_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp69_AST);
                    this.match(22);
                    break;
                }
                case 23: {
                    AST tmp70_AST = null;
                    tmp70_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp70_AST);
                    this.match(23);
                    break;
                }
                default: {
                    throw new NoViableAltException(this.LT(1), this.getFilename());
                }
            }
            this.andExpression();
            this.astFactory.addASTChild(currentAST, this.returnAST);
        }
        booleanExpression_AST = currentAST.root;
        this.returnAST = booleanExpression_AST;
    }
    
    public final void andExpression() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST andExpression_AST = null;
        this.relationalExpression();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        while (this.LA(1) >= 24 && this.LA(1) <= 26) {
            switch (this.LA(1)) {
                case 25: {
                    AST tmp71_AST = null;
                    tmp71_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp71_AST);
                    this.match(25);
                    break;
                }
                case 26: {
                    AST tmp72_AST = null;
                    tmp72_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp72_AST);
                    this.match(26);
                    break;
                }
                case 24: {
                    AST tmp73_AST = null;
                    tmp73_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp73_AST);
                    this.match(24);
                    break;
                }
                default: {
                    throw new NoViableAltException(this.LT(1), this.getFilename());
                }
            }
            this.relationalExpression();
            this.astFactory.addASTChild(currentAST, this.returnAST);
        }
        andExpression_AST = currentAST.root;
        this.returnAST = andExpression_AST;
    }
    
    public final void relationalExpression() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST relationalExpression_AST = null;
        this.numericExpression();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        while (this.LA(1) >= 49 && this.LA(1) <= 56) {
            switch (this.LA(1)) {
                case 49: {
                    AST tmp74_AST = null;
                    tmp74_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp74_AST);
                    this.match(49);
                    break;
                }
                case 50: {
                    AST tmp75_AST = null;
                    tmp75_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp75_AST);
                    this.match(50);
                    break;
                }
                case 51: {
                    AST tmp76_AST = null;
                    tmp76_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp76_AST);
                    this.match(51);
                    break;
                }
                case 52: {
                    AST tmp77_AST = null;
                    tmp77_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp77_AST);
                    this.match(52);
                    break;
                }
                case 53: {
                    AST tmp78_AST = null;
                    tmp78_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp78_AST);
                    this.match(53);
                    break;
                }
                case 54: {
                    AST tmp79_AST = null;
                    tmp79_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp79_AST);
                    this.match(54);
                    break;
                }
                case 55: {
                    AST tmp80_AST = null;
                    tmp80_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp80_AST);
                    this.match(55);
                    break;
                }
                case 56: {
                    AST tmp81_AST = null;
                    tmp81_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp81_AST);
                    this.match(56);
                    break;
                }
                default: {
                    throw new NoViableAltException(this.LT(1), this.getFilename());
                }
            }
            this.numericExpression();
            this.astFactory.addASTChild(currentAST, this.returnAST);
        }
        relationalExpression_AST = currentAST.root;
        this.returnAST = relationalExpression_AST;
    }
    
    public final void numericExpression() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST numericExpression_AST = null;
        this.multiplicationExpression();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        while (this.LA(1) == 27 || this.LA(1) == 28) {
            switch (this.LA(1)) {
                case 27: {
                    AST tmp82_AST = null;
                    tmp82_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp82_AST);
                    this.match(27);
                    break;
                }
                case 28: {
                    AST tmp83_AST = null;
                    tmp83_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp83_AST);
                    this.match(28);
                    break;
                }
                default: {
                    throw new NoViableAltException(this.LT(1), this.getFilename());
                }
            }
            this.multiplicationExpression();
            this.astFactory.addASTChild(currentAST, this.returnAST);
        }
        numericExpression_AST = currentAST.root;
        this.returnAST = numericExpression_AST;
    }
    
    public final void multiplicationExpression() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST multiplicationExpression_AST = null;
        this.exponentialExpression();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        while (this.LA(1) >= 29 && this.LA(1) <= 32) {
            switch (this.LA(1)) {
                case 29: {
                    AST tmp84_AST = null;
                    tmp84_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp84_AST);
                    this.match(29);
                    break;
                }
                case 30: {
                    AST tmp85_AST = null;
                    tmp85_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp85_AST);
                    this.match(30);
                    break;
                }
                case 32: {
                    AST tmp86_AST = null;
                    tmp86_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp86_AST);
                    this.match(32);
                    break;
                }
                case 31: {
                    AST tmp87_AST = null;
                    tmp87_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp87_AST);
                    this.match(31);
                    break;
                }
                default: {
                    throw new NoViableAltException(this.LT(1), this.getFilename());
                }
            }
            this.exponentialExpression();
            this.astFactory.addASTChild(currentAST, this.returnAST);
        }
        multiplicationExpression_AST = currentAST.root;
        this.returnAST = multiplicationExpression_AST;
    }
    
    public final void exponentialExpression() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST exponentialExpression_AST = null;
        this.signedQuantity();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        switch (this.LA(1)) {
            case 61: {
                AST tmp88_AST = null;
                tmp88_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp88_AST);
                this.match(61);
                this.signedQuantity();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                break;
            }
            case 1:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 63:
            case 64: {
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        exponentialExpression_AST = currentAST.root;
        this.returnAST = exponentialExpression_AST;
    }
    
    public final void signedQuantity() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST signedQuantity_AST = null;
        Token s = null;
        AST s_AST = null;
        Token a = null;
        AST a_AST = null;
        switch (this.LA(1)) {
            case 28: {
                s = this.LT(1);
                s_AST = this.astFactory.create(s);
                this.astFactory.makeASTRoot(currentAST, s_AST);
                this.match(28);
                s_AST.setType(60);
                break;
            }
            case 27: {
                a = this.LT(1);
                a_AST = this.astFactory.create(a);
                this.astFactory.makeASTRoot(currentAST, a_AST);
                this.match(27);
                a_AST.setType(59);
                break;
            }
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 57:
            case 58: {
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.negationExpression();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        signedQuantity_AST = currentAST.root;
        this.returnAST = signedQuantity_AST;
    }
    
    public final void negationExpression() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST negationExpression_AST = null;
        while (true) {
            switch (this.LA(1)) {
                case 57: {
                    AST tmp89_AST = null;
                    tmp89_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp89_AST);
                    this.match(57);
                    continue;
                }
                case 58: {
                    AST tmp90_AST = null;
                    tmp90_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.makeASTRoot(currentAST, tmp90_AST);
                    this.match(58);
                    continue;
                }
                default: {
                    this.quantity();
                    this.astFactory.addASTChild(currentAST, this.returnAST);
                    negationExpression_AST = currentAST.root;
                    this.returnAST = negationExpression_AST;
                }
            }
        }
    }
    
    public final void quantity() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST quantity_AST = null;
        switch (this.LA(1)) {
            case 13: {
                AST tmp91_AST = null;
                tmp91_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp91_AST);
                this.match(13);
                quantity_AST = currentAST.root;
                break;
            }
            case 14:
            case 15: {
                this.stringLiteral();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                quantity_AST = currentAST.root;
                break;
            }
            case 9:
            case 10:
            case 11: {
                this.momentLiteral();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                quantity_AST = currentAST.root;
                break;
            }
            case 5: {
                AST tmp92_AST = null;
                tmp92_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp92_AST);
                this.match(5);
                quantity_AST = currentAST.root;
                break;
            }
            case 4: {
                AST tmp93_AST = null;
                tmp93_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp93_AST);
                this.match(4);
                quantity_AST = currentAST.root;
                break;
            }
            case 7: {
                AST tmp94_AST = null;
                tmp94_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp94_AST);
                this.match(7);
                quantity_AST = currentAST.root;
                break;
            }
            case 6: {
                AST tmp95_AST = null;
                tmp95_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp95_AST);
                this.match(6);
                quantity_AST = currentAST.root;
                break;
            }
            case 8: {
                AST tmp96_AST = null;
                tmp96_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp96_AST);
                this.match(8);
                quantity_AST = currentAST.root;
                break;
            }
            case 17: {
                AST tmp97_AST = null;
                tmp97_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp97_AST);
                this.match(17);
                quantity_AST = currentAST.root;
                break;
            }
            case 16: {
                this.reference();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                quantity_AST = currentAST.root;
                break;
            }
            case 18: {
                AST tmp98_AST = null;
                tmp98_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp98_AST);
                this.match(18);
                this.expression();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                this.match(64);
                quantity_AST = currentAST.root;
                break;
            }
            default: {
                if (this.LA(1) == 12 && DataPipelineParser._tokenSet_0.member(this.LA(2))) {
                    AST tmp100_AST = null;
                    tmp100_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.addASTChild(currentAST, tmp100_AST);
                    this.match(12);
                    quantity_AST = currentAST.root;
                    break;
                }
                if (this.LA(1) == 12 && this.LA(2) >= 33 && this.LA(2) <= 48) {
                    this.intervalLiteral();
                    this.astFactory.addASTChild(currentAST, this.returnAST);
                    quantity_AST = currentAST.root;
                    break;
                }
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = quantity_AST;
    }
    
    public final void stringLiteral() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST stringLiteral_AST = null;
        switch (this.LA(1)) {
            case 15: {
                AST tmp101_AST = null;
                tmp101_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp101_AST);
                this.match(15);
                stringLiteral_AST = currentAST.root;
                break;
            }
            case 14: {
                AST tmp102_AST = null;
                tmp102_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp102_AST);
                this.match(14);
                stringLiteral_AST = currentAST.root;
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = stringLiteral_AST;
    }
    
    public final void momentLiteral() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST momentLiteral_AST = null;
        switch (this.LA(1)) {
            case 9: {
                this.dateLiteral();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                momentLiteral_AST = currentAST.root;
                break;
            }
            case 10: {
                this.timeLiteral();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                momentLiteral_AST = currentAST.root;
                break;
            }
            case 11: {
                this.timestampLiteral();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                momentLiteral_AST = currentAST.root;
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = momentLiteral_AST;
    }
    
    public final void intervalLiteral() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST intervalLiteral_AST = null;
        AST tmp103_AST = null;
        tmp103_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp103_AST);
        this.match(12);
        switch (this.LA(1)) {
            case 33: {
                AST tmp104_AST = null;
                tmp104_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp104_AST);
                this.match(33);
                break;
            }
            case 34: {
                AST tmp105_AST = null;
                tmp105_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp105_AST);
                this.match(34);
                break;
            }
            case 35: {
                AST tmp106_AST = null;
                tmp106_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp106_AST);
                this.match(35);
                break;
            }
            case 36: {
                AST tmp107_AST = null;
                tmp107_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp107_AST);
                this.match(36);
                break;
            }
            case 37: {
                AST tmp108_AST = null;
                tmp108_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp108_AST);
                this.match(37);
                break;
            }
            case 38: {
                AST tmp109_AST = null;
                tmp109_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp109_AST);
                this.match(38);
                break;
            }
            case 39: {
                AST tmp110_AST = null;
                tmp110_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp110_AST);
                this.match(39);
                break;
            }
            case 40: {
                AST tmp111_AST = null;
                tmp111_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp111_AST);
                this.match(40);
                break;
            }
            case 41: {
                AST tmp112_AST = null;
                tmp112_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp112_AST);
                this.match(41);
                break;
            }
            case 42: {
                AST tmp113_AST = null;
                tmp113_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp113_AST);
                this.match(42);
                break;
            }
            case 43: {
                AST tmp114_AST = null;
                tmp114_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp114_AST);
                this.match(43);
                break;
            }
            case 44: {
                AST tmp115_AST = null;
                tmp115_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp115_AST);
                this.match(44);
                break;
            }
            case 45: {
                AST tmp116_AST = null;
                tmp116_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp116_AST);
                this.match(45);
                break;
            }
            case 46: {
                AST tmp117_AST = null;
                tmp117_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp117_AST);
                this.match(46);
                break;
            }
            case 47: {
                AST tmp118_AST = null;
                tmp118_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp118_AST);
                this.match(47);
                break;
            }
            case 48: {
                AST tmp119_AST = null;
                tmp119_AST = this.astFactory.create(this.LT(1));
                this.astFactory.makeASTRoot(currentAST, tmp119_AST);
                this.match(48);
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        intervalLiteral_AST = currentAST.root;
        this.returnAST = intervalLiteral_AST;
    }
    
    public final void reference() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST reference_AST = null;
        if (this.LA(1) == 16 && this.LA(2) == 18) {
            this.methodCall();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            reference_AST = currentAST.root;
        }
        else {
            if (this.LA(1) != 16 || !DataPipelineParser._tokenSet_0.member(this.LA(2))) {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
            AST tmp120_AST = null;
            tmp120_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp120_AST);
            this.match(16);
            reference_AST = currentAST.root;
        }
        this.returnAST = reference_AST;
    }
    
    public final void dateLiteral() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST dateLiteral_AST = null;
        AST tmp121_AST = null;
        tmp121_AST = this.astFactory.create(this.LT(1));
        this.astFactory.makeASTRoot(currentAST, tmp121_AST);
        this.match(9);
        this.stringLiteral();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        dateLiteral_AST = currentAST.root;
        this.returnAST = dateLiteral_AST;
    }
    
    public final void timeLiteral() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST timeLiteral_AST = null;
        AST tmp122_AST = null;
        tmp122_AST = this.astFactory.create(this.LT(1));
        this.astFactory.makeASTRoot(currentAST, tmp122_AST);
        this.match(10);
        this.stringLiteral();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        timeLiteral_AST = currentAST.root;
        this.returnAST = timeLiteral_AST;
    }
    
    public final void timestampLiteral() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST timestampLiteral_AST = null;
        AST tmp123_AST = null;
        tmp123_AST = this.astFactory.create(this.LT(1));
        this.astFactory.makeASTRoot(currentAST, tmp123_AST);
        this.match(11);
        this.stringLiteral();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        timestampLiteral_AST = currentAST.root;
        this.returnAST = timestampLiteral_AST;
    }
    
    public final void methodCall() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST methodCall_AST = null;
        Token l = null;
        AST l_AST = null;
        AST tmp124_AST = null;
        tmp124_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp124_AST);
        this.match(16);
        l = this.LT(1);
        l_AST = this.astFactory.create(l);
        this.astFactory.makeASTRoot(currentAST, l_AST);
        this.match(18);
        l_AST.setType(62);
        switch (this.LA(1)) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 27:
            case 28:
            case 57:
            case 58: {
                this.expressionList();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                break;
            }
            case 64: {
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.match(64);
        methodCall_AST = currentAST.root;
        this.returnAST = methodCall_AST;
    }
    
    protected void buildTokenTypeASTClassMap() {
        this.tokenTypeToASTClassMap = null;
    }
    
    private static final long[] mk_tokenSet_0() {
        final long[] data = { -6773976780929236990L, 1L, 0L, 0L };
        return data;
    }
    
    static {
        _tokenNames = new String[] { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "\"false\"", "\"true\"", "\"no\"", "\"yes\"", "\"null\"", "\"date\"", "\"time\"", "\"timestamp\"", "a number", "DECIMAL_LITERAL", "a string", "a string", "an identifier", "QUESTION_MARK", "LPAREN", "\"or\"", "BITWISE_OR", "LOGICAL_OR", "\"xor\"", "XOR", "\"and\"", "BITWISE_AND", "LOGICAL_AND", "ADD", "SUBTRACT", "MULTIPLY", "DIVIDE", "\"mod\"", "REMAINDER", "\"millisecond\"", "\"second\"", "\"minute\"", "\"hour\"", "\"day\"", "\"week\"", "\"month\"", "\"year\"", "\"milliseconds\"", "\"seconds\"", "\"minutes\"", "\"hours\"", "\"days\"", "\"weeks\"", "\"months\"", "\"years\"", "EQUAL", "EQUAL_EQUAL", "NE", "GTLT", "GT", "GTE", "LT", "LTE", "\"not\"", "NOT", "PLUS_SIGN", "MINUS_SIGN", "POWER", "METHOD_CALL", "COMMA", "RPAREN", "COLON", "numbers (0-9)", "a space", "ESCAPE_SEQUENCE", "a hexadecimal digit (0-9 or a-f)" };
        _tokenSet_0 = new BitSet(mk_tokenSet_0());
    }
}
