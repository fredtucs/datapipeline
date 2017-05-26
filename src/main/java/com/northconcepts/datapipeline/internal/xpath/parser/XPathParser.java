package com.northconcepts.datapipeline.internal.xpath.parser;

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

public class XPathParser extends LLkParser implements XPathTreeParserTokenTypes
{
    public static final String[] _tokenNames;
    public static final BitSet _tokenSet_0;
    public static final BitSet _tokenSet_1;
    public static final BitSet _tokenSet_2;
    public static final BitSet _tokenSet_3;
    public static final BitSet _tokenSet_4;
    
    protected XPathParser(final TokenBuffer tokenBuf, final int k) {
        super(tokenBuf, k);
        this.tokenNames = XPathParser._tokenNames;
        this.buildTokenTypeASTClassMap();
        this.astFactory = new ASTFactory(this.getTokenTypeToASTClassMap());
    }
    
    public XPathParser(final TokenBuffer tokenBuf) {
        this(tokenBuf, 4);
    }
    
    protected XPathParser(final TokenStream lexer, final int k) {
        super(lexer, k);
        this.tokenNames = XPathParser._tokenNames;
        this.buildTokenTypeASTClassMap();
        this.astFactory = new ASTFactory(this.getTokenTypeToASTClassMap());
    }
    
    public XPathParser(final TokenStream lexer) {
        this(lexer, 4);
    }
    
    public XPathParser(final ParserSharedInputState state) {
        super(state, 4);
        this.tokenNames = XPathParser._tokenNames;
        this.buildTokenTypeASTClassMap();
        this.astFactory = new ASTFactory(this.getTokenTypeToASTClassMap());
    }
    
    public final void imaginaryTokens() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST imaginaryTokens_AST = null;
        AST tmp1_AST = null;
        tmp1_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp1_AST);
        this.match(39);
        AST tmp2_AST = null;
        tmp2_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp2_AST);
        this.match(40);
        AST tmp3_AST = null;
        tmp3_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp3_AST);
        this.match(41);
        AST tmp4_AST = null;
        tmp4_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp4_AST);
        this.match(42);
        AST tmp5_AST = null;
        tmp5_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp5_AST);
        this.match(43);
        AST tmp6_AST = null;
        tmp6_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp6_AST);
        this.match(44);
        AST tmp7_AST = null;
        tmp7_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp7_AST);
        this.match(45);
        AST tmp8_AST = null;
        tmp8_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp8_AST);
        this.match(46);
        AST tmp9_AST = null;
        tmp9_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp9_AST);
        this.match(4);
        AST tmp10_AST = null;
        tmp10_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp10_AST);
        this.match(5);
        AST tmp11_AST = null;
        tmp11_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp11_AST);
        this.match(47);
        AST tmp12_AST = null;
        tmp12_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp12_AST);
        this.match(10);
        AST tmp13_AST = null;
        tmp13_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp13_AST);
        this.match(11);
        AST tmp14_AST = null;
        tmp14_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp14_AST);
        this.match(8);
        AST tmp15_AST = null;
        tmp15_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp15_AST);
        this.match(9);
        AST tmp16_AST = null;
        tmp16_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp16_AST);
        this.match(6);
        AST tmp17_AST = null;
        tmp17_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp17_AST);
        this.match(7);
        AST tmp18_AST = null;
        tmp18_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp18_AST);
        this.match(34);
        AST tmp19_AST = null;
        tmp19_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp19_AST);
        this.match(31);
        AST tmp20_AST = null;
        tmp20_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp20_AST);
        this.match(34);
        AST tmp21_AST = null;
        tmp21_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp21_AST);
        this.match(31);
        AST tmp22_AST = null;
        tmp22_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp22_AST);
        this.match(35);
        AST tmp23_AST = null;
        tmp23_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp23_AST);
        this.match(32);
        AST tmp24_AST = null;
        tmp24_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp24_AST);
        this.match(30);
        AST tmp25_AST = null;
        tmp25_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp25_AST);
        this.match(33);
        AST tmp26_AST = null;
        tmp26_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp26_AST);
        this.match(36);
        AST tmp27_AST = null;
        tmp27_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp27_AST);
        this.match(25);
        AST tmp28_AST = null;
        tmp28_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp28_AST);
        this.match(48);
        imaginaryTokens_AST = currentAST.root;
        this.returnAST = imaginaryTokens_AST;
    }
    
    public final void locationPath() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST locationPath_AST = null;
        this.locationPathPart();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        while (this.LA(1) == 49) {
            AST tmp29_AST = null;
            tmp29_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp29_AST);
            this.match(49);
            this.locationPathPart();
            this.astFactory.addASTChild(currentAST, this.returnAST);
        }
        locationPath_AST = currentAST.root;
        this.returnAST = locationPath_AST;
    }
    
    public final void locationPathPart() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST locationPathPart_AST = null;
        switch (this.LA(1)) {
            case 50:
            case 51: {
                this.absoluteLocationPath();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                locationPathPart_AST = currentAST.root;
                break;
            }
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 26:
            case 27:
            case 28:
            case 29:
            case 37:
            case 38:
            case 43:
            case 52:
            case 54:
            case 57: {
                this.relativeLocationPath();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                locationPathPart_AST = currentAST.root;
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = locationPathPart_AST;
    }
    
    public final void absoluteLocationPath() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST absoluteLocationPath_AST = null;
        Token t = null;
        AST t_AST = null;
        Token t2 = null;
        AST t2_AST = null;
        switch (this.LA(1)) {
            case 50: {
                t = this.LT(1);
                t_AST = this.astFactory.create(t);
                this.astFactory.makeASTRoot(currentAST, t_AST);
                this.match(50);
                if (this.inputState.guessing == 0) {
                    t_AST.setType(4);
                    break;
                }
                break;
            }
            case 51: {
                t2 = this.LT(1);
                t2_AST = this.astFactory.create(t2);
                this.astFactory.makeASTRoot(currentAST, t2_AST);
                this.match(51);
                if (this.inputState.guessing == 0) {
                    t2_AST.setType(5);
                    break;
                }
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.relativeLocationPath();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        absoluteLocationPath_AST = currentAST.root;
        this.returnAST = absoluteLocationPath_AST;
    }
    
    public final void relativeLocationPath() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST relativeLocationPath_AST = null;
        Token t = null;
        AST t_AST = null;
        Token t2 = null;
        AST t2_AST = null;
        this.step();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        while (this.LA(1) == 50 || this.LA(1) == 51) {
            switch (this.LA(1)) {
                case 50: {
                    t = this.LT(1);
                    t_AST = this.astFactory.create(t);
                    this.astFactory.addASTChild(currentAST, t_AST);
                    this.match(50);
                    if (this.inputState.guessing == 0) {
                        t_AST.setType(6);
                        break;
                    }
                    break;
                }
                case 51: {
                    t2 = this.LT(1);
                    t2_AST = this.astFactory.create(t2);
                    this.astFactory.addASTChild(currentAST, t2_AST);
                    this.match(51);
                    if (this.inputState.guessing == 0) {
                        t2_AST.setType(7);
                        break;
                    }
                    break;
                }
                default: {
                    throw new NoViableAltException(this.LT(1), this.getFilename());
                }
            }
            this.step();
            this.astFactory.addASTChild(currentAST, this.returnAST);
        }
        relativeLocationPath_AST = currentAST.root;
        this.returnAST = relativeLocationPath_AST;
    }
    
    public final void step() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST step_AST = null;
        switch (this.LA(1)) {
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 54: {
                this.axisSpecifier();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                this.nodeTest();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                while (this.LA(1) == 58) {
                    this.predicate();
                    this.astFactory.addASTChild(currentAST, this.returnAST);
                }
                step_AST = currentAST.root;
                break;
            }
            case 26:
            case 27:
            case 28:
            case 29:
            case 37:
            case 38:
            case 57: {
                this.nodeTest();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                while (this.LA(1) == 58) {
                    this.predicate();
                    this.astFactory.addASTChild(currentAST, this.returnAST);
                }
                step_AST = currentAST.root;
                break;
            }
            case 43:
            case 52: {
                this.abbreviatedStep();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                step_AST = currentAST.root;
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = step_AST;
    }
    
    protected final void axisSpecifier() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST axisSpecifier_AST = null;
        Token t = null;
        AST t_AST = null;
        Token t2 = null;
        AST t2_AST = null;
        switch (this.LA(1)) {
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24: {
                this.axisName();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                t = this.LT(1);
                t_AST = this.astFactory.create(t);
                this.astFactory.makeASTRoot(currentAST, t_AST);
                this.match(53);
                if (this.inputState.guessing == 0) {
                    t_AST.setType(10);
                }
                axisSpecifier_AST = currentAST.root;
                break;
            }
            case 54: {
                t2 = this.LT(1);
                t2_AST = this.astFactory.create(t2);
                this.astFactory.makeASTRoot(currentAST, t2_AST);
                this.match(54);
                if (this.inputState.guessing == 0) {
                    t2_AST.setType(11);
                }
                axisSpecifier_AST = currentAST.root;
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = axisSpecifier_AST;
    }
    
    public final void nodeTest() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST nodeTest_AST = null;
        Token t = null;
        AST t_AST = null;
        Token t2 = null;
        AST t2_AST = null;
        if (this.LA(1) == 37 || this.LA(1) == 38 || this.LA(1) == 57) {
            this.nameTest();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            nodeTest_AST = currentAST.root;
        }
        else if (this.LA(1) >= 26 && this.LA(1) <= 29 && this.LA(2) == 55 && this.LA(3) == 56) {
            this.nodeType();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            t = this.LT(1);
            t_AST = this.astFactory.create(t);
            this.astFactory.makeASTRoot(currentAST, t_AST);
            this.match(55);
            this.match(56);
            if (this.inputState.guessing == 0) {
                t_AST.setType(25);
            }
            nodeTest_AST = currentAST.root;
        }
        else {
            if (this.LA(1) != 28 || this.LA(2) != 55 || (this.LA(3) != 60 && this.LA(3) != 61)) {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
            this.match(28);
            t2 = this.LT(1);
            t2_AST = this.astFactory.create(t2);
            this.astFactory.makeASTRoot(currentAST, t2_AST);
            this.match(55);
            this.stringLiteral();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            this.match(56);
            if (this.inputState.guessing == 0) {
                t2_AST.setType(48);
            }
            nodeTest_AST = currentAST.root;
        }
        this.returnAST = nodeTest_AST;
    }
    
    public final void predicate() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST predicate_AST = null;
        Token t = null;
        AST t_AST = null;
        t = this.LT(1);
        t_AST = this.astFactory.create(t);
        this.astFactory.makeASTRoot(currentAST, t_AST);
        this.match(58);
        if (this.inputState.guessing == 0) {
            t_AST.setType(36);
        }
        while (XPathParser._tokenSet_0.member(this.LA(1))) {
            AST tmp33_AST = null;
            tmp33_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp33_AST);
            this.match(XPathParser._tokenSet_0);
        }
        this.match(59);
        predicate_AST = currentAST.root;
        this.returnAST = predicate_AST;
    }
    
    public final void abbreviatedStep() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST abbreviatedStep_AST = null;
        Token t = null;
        AST t_AST = null;
        Token t2 = null;
        AST t2_AST = null;
        switch (this.LA(1)) {
            case 43: {
                t = this.LT(1);
                t_AST = this.astFactory.create(t);
                this.astFactory.makeASTRoot(currentAST, t_AST);
                this.match(43);
                if (this.inputState.guessing == 0) {
                    t_AST.setType(8);
                    break;
                }
                break;
            }
            case 52: {
                t2 = this.LT(1);
                t2_AST = this.astFactory.create(t2);
                this.astFactory.makeASTRoot(currentAST, t2_AST);
                this.match(52);
                if (this.inputState.guessing == 0) {
                    t2_AST.setType(9);
                    break;
                }
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        while (this.LA(1) == 58) {
            this.predicate();
            this.astFactory.addASTChild(currentAST, this.returnAST);
        }
        abbreviatedStep_AST = currentAST.root;
        this.returnAST = abbreviatedStep_AST;
    }
    
    public final void axisName() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST axisName_AST = null;
        switch (this.LA(1)) {
            case 12: {
                AST tmp35_AST = null;
                tmp35_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp35_AST);
                this.match(12);
                axisName_AST = currentAST.root;
                break;
            }
            case 13: {
                AST tmp36_AST = null;
                tmp36_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp36_AST);
                this.match(13);
                axisName_AST = currentAST.root;
                break;
            }
            case 14: {
                AST tmp37_AST = null;
                tmp37_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp37_AST);
                this.match(14);
                axisName_AST = currentAST.root;
                break;
            }
            case 15: {
                AST tmp38_AST = null;
                tmp38_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp38_AST);
                this.match(15);
                axisName_AST = currentAST.root;
                break;
            }
            case 16: {
                AST tmp39_AST = null;
                tmp39_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp39_AST);
                this.match(16);
                axisName_AST = currentAST.root;
                break;
            }
            case 17: {
                AST tmp40_AST = null;
                tmp40_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp40_AST);
                this.match(17);
                axisName_AST = currentAST.root;
                break;
            }
            case 18: {
                AST tmp41_AST = null;
                tmp41_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp41_AST);
                this.match(18);
                axisName_AST = currentAST.root;
                break;
            }
            case 19: {
                AST tmp42_AST = null;
                tmp42_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp42_AST);
                this.match(19);
                axisName_AST = currentAST.root;
                break;
            }
            case 20: {
                AST tmp43_AST = null;
                tmp43_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp43_AST);
                this.match(20);
                axisName_AST = currentAST.root;
                break;
            }
            case 21: {
                AST tmp44_AST = null;
                tmp44_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp44_AST);
                this.match(21);
                axisName_AST = currentAST.root;
                break;
            }
            case 22: {
                AST tmp45_AST = null;
                tmp45_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp45_AST);
                this.match(22);
                axisName_AST = currentAST.root;
                break;
            }
            case 23: {
                AST tmp46_AST = null;
                tmp46_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp46_AST);
                this.match(23);
                axisName_AST = currentAST.root;
                break;
            }
            case 24: {
                AST tmp47_AST = null;
                tmp47_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp47_AST);
                this.match(24);
                axisName_AST = currentAST.root;
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = axisName_AST;
    }
    
    public final void nameTest() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST nameTest_AST = null;
        boolean synPredMatched59 = false;
        if ((this.LA(1) == 37 || this.LA(1) == 57) && XPathParser._tokenSet_1.member(this.LA(2)) && XPathParser._tokenSet_2.member(this.LA(3)) && XPathParser._tokenSet_2.member(this.LA(4))) {
            final int _m59 = this.mark();
            synPredMatched59 = true;
            final ParserSharedInputState inputState = this.inputState;
            ++inputState.guessing;
            try {
                switch (this.LA(1)) {
                    case 37: {
                        this.match(37);
                        this.match(57);
                        this.match(37);
                        break;
                    }
                    case 57: {
                        this.match(57);
                        this.match(37);
                        break;
                    }
                    default: {
                        throw new NoViableAltException(this.LT(1), this.getFilename());
                    }
                }
            }
            catch (RecognitionException pe) {
                synPredMatched59 = false;
            }
            this.rewind(_m59);
            final ParserSharedInputState inputState2 = this.inputState;
            --inputState2.guessing;
        }
        if (synPredMatched59) {
            this.qName();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            nameTest_AST = currentAST.root;
        }
        else {
            boolean synPredMatched2 = false;
            if ((this.LA(1) == 37 || this.LA(1) == 38 || this.LA(1) == 57) && XPathParser._tokenSet_3.member(this.LA(2)) && XPathParser._tokenSet_2.member(this.LA(3)) && XPathParser._tokenSet_2.member(this.LA(4))) {
                final int _m2 = this.mark();
                synPredMatched2 = true;
                final ParserSharedInputState inputState3 = this.inputState;
                ++inputState3.guessing;
                try {
                    switch (this.LA(1)) {
                        case 38: {
                            this.match(38);
                            break;
                        }
                        case 57: {
                            this.match(57);
                            break;
                        }
                        case 37: {
                            this.match(37);
                            this.match(57);
                            break;
                        }
                        default: {
                            throw new NoViableAltException(this.LT(1), this.getFilename());
                        }
                    }
                }
                catch (RecognitionException pe2) {
                    synPredMatched2 = false;
                }
                this.rewind(_m2);
                final ParserSharedInputState inputState4 = this.inputState;
                --inputState4.guessing;
            }
            if (synPredMatched2) {
                this.wildcard();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                nameTest_AST = currentAST.root;
            }
            else {
                if ((this.LA(1) != 37 && this.LA(1) != 57) || !XPathParser._tokenSet_1.member(this.LA(2)) || !XPathParser._tokenSet_2.member(this.LA(3)) || !XPathParser._tokenSet_2.member(this.LA(4))) {
                    throw new NoViableAltException(this.LT(1), this.getFilename());
                }
                this.qName();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                nameTest_AST = currentAST.root;
            }
        }
        this.returnAST = nameTest_AST;
    }
    
    public final void nodeType() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST nodeType_AST = null;
        switch (this.LA(1)) {
            case 26: {
                AST tmp48_AST = null;
                tmp48_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp48_AST);
                this.match(26);
                nodeType_AST = currentAST.root;
                break;
            }
            case 27: {
                AST tmp49_AST = null;
                tmp49_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp49_AST);
                this.match(27);
                nodeType_AST = currentAST.root;
                break;
            }
            case 28: {
                AST tmp50_AST = null;
                tmp50_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp50_AST);
                this.match(28);
                nodeType_AST = currentAST.root;
                break;
            }
            case 29: {
                AST tmp51_AST = null;
                tmp51_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp51_AST);
                this.match(29);
                nodeType_AST = currentAST.root;
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = nodeType_AST;
    }
    
    public final void stringLiteral() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST stringLiteral_AST = null;
        switch (this.LA(1)) {
            case 60: {
                AST tmp52_AST = null;
                tmp52_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp52_AST);
                this.match(60);
                stringLiteral_AST = currentAST.root;
                break;
            }
            case 61: {
                AST tmp53_AST = null;
                tmp53_AST = this.astFactory.create(this.LT(1));
                this.astFactory.addASTChild(currentAST, tmp53_AST);
                this.match(61);
                stringLiteral_AST = currentAST.root;
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = stringLiteral_AST;
    }
    
    public final void qName() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST qName_AST = null;
        if ((this.LA(1) == 37 || this.LA(1) == 57) && (this.LA(2) == 37 || this.LA(2) == 57)) {
            this.prefixedName();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            qName_AST = currentAST.root;
        }
        else {
            if (this.LA(1) != 37 || !XPathParser._tokenSet_4.member(this.LA(2))) {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
            this.unprefixedName();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            qName_AST = currentAST.root;
        }
        this.returnAST = qName_AST;
    }
    
    public final void wildcard() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST wildcard_AST = null;
        Token t = null;
        AST t_AST = null;
        Token t2 = null;
        AST t2_AST = null;
        Token t3 = null;
        AST t3_AST = null;
        Token t4 = null;
        AST t4_AST = null;
        Token t5 = null;
        AST t5_AST = null;
        if (this.LA(1) == 38 && XPathParser._tokenSet_4.member(this.LA(2))) {
            t = this.LT(1);
            t_AST = this.astFactory.create(t);
            this.astFactory.addASTChild(currentAST, t_AST);
            this.match(38);
            if (this.inputState.guessing == 0) {
                t_AST.setType(30);
            }
            wildcard_AST = currentAST.root;
        }
        else if (this.LA(1) == 38 && this.LA(2) == 57) {
            AST tmp54_AST = null;
            tmp54_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp54_AST);
            this.match(38);
            t2 = this.LT(1);
            t2_AST = this.astFactory.create(t2);
            this.astFactory.makeASTRoot(currentAST, t2_AST);
            this.match(57);
            switch (this.LA(1)) {
                case 38: {
                    AST tmp55_AST = null;
                    tmp55_AST = this.astFactory.create(this.LT(1));
                    this.astFactory.addASTChild(currentAST, tmp55_AST);
                    this.match(38);
                    break;
                }
                case 37: {
                    this.ncName();
                    this.astFactory.addASTChild(currentAST, this.returnAST);
                    break;
                }
                default: {
                    throw new NoViableAltException(this.LT(1), this.getFilename());
                }
            }
            if (this.inputState.guessing == 0) {
                t2_AST.setType(32);
            }
            wildcard_AST = currentAST.root;
        }
        else if (this.LA(1) == 37) {
            this.ncName();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            t3 = this.LT(1);
            t3_AST = this.astFactory.create(t3);
            this.astFactory.makeASTRoot(currentAST, t3_AST);
            this.match(57);
            AST tmp56_AST = null;
            tmp56_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp56_AST);
            this.match(38);
            if (this.inputState.guessing == 0) {
                t3_AST.setType(32);
            }
            wildcard_AST = currentAST.root;
        }
        else if (this.LA(1) == 57 && this.LA(2) == 37) {
            t4 = this.LT(1);
            t4_AST = this.astFactory.create(t4);
            this.astFactory.makeASTRoot(currentAST, t4_AST);
            this.match(57);
            this.ncName();
            this.astFactory.addASTChild(currentAST, this.returnAST);
            if (this.inputState.guessing == 0) {
                t4_AST.setType(33);
            }
            wildcard_AST = currentAST.root;
        }
        else {
            if (this.LA(1) != 57 || this.LA(2) != 38) {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
            t5 = this.LT(1);
            t5_AST = this.astFactory.create(t5);
            this.astFactory.makeASTRoot(currentAST, t5_AST);
            this.match(57);
            AST tmp57_AST = null;
            tmp57_AST = this.astFactory.create(this.LT(1));
            this.astFactory.addASTChild(currentAST, tmp57_AST);
            this.match(38);
            if (this.inputState.guessing == 0) {
                t5_AST.setType(33);
            }
            wildcard_AST = currentAST.root;
        }
        this.returnAST = wildcard_AST;
    }
    
    protected final void ncName() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST ncName_AST = null;
        AST tmp58_AST = null;
        tmp58_AST = this.astFactory.create(this.LT(1));
        this.astFactory.addASTChild(currentAST, tmp58_AST);
        this.match(37);
        ncName_AST = currentAST.root;
        this.returnAST = ncName_AST;
    }
    
    public final void prefixedName() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST prefixedName_AST = null;
        Token t = null;
        AST t_AST = null;
        Token t2 = null;
        AST t2_AST = null;
        switch (this.LA(1)) {
            case 37: {
                this.prefix();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                t = this.LT(1);
                t_AST = this.astFactory.create(t);
                this.astFactory.makeASTRoot(currentAST, t_AST);
                this.match(57);
                if (this.inputState.guessing == 0) {
                    t_AST.setType(34);
                }
                this.localPart();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                prefixedName_AST = currentAST.root;
                break;
            }
            case 57: {
                t2 = this.LT(1);
                t2_AST = this.astFactory.create(t2);
                this.astFactory.makeASTRoot(currentAST, t2_AST);
                this.match(57);
                if (this.inputState.guessing == 0) {
                    t2_AST.setType(35);
                }
                this.localPart();
                this.astFactory.addASTChild(currentAST, this.returnAST);
                prefixedName_AST = currentAST.root;
                break;
            }
            default: {
                throw new NoViableAltException(this.LT(1), this.getFilename());
            }
        }
        this.returnAST = prefixedName_AST;
    }
    
    public final void unprefixedName() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST unprefixedName_AST = null;
        AST t_AST = null;
        this.localPart();
        t_AST = this.returnAST;
        this.astFactory.addASTChild(currentAST, this.returnAST);
        if (this.inputState.guessing == 0) {
            t_AST.setType(31);
        }
        unprefixedName_AST = currentAST.root;
        this.returnAST = unprefixedName_AST;
    }
    
    protected final void prefix() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST prefix_AST = null;
        this.ncName();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        prefix_AST = currentAST.root;
        this.returnAST = prefix_AST;
    }
    
    protected final void localPart() throws RecognitionException, TokenStreamException {
        this.returnAST = null;
        final ASTPair currentAST = new ASTPair();
        AST localPart_AST = null;
        this.ncName();
        this.astFactory.addASTChild(currentAST, this.returnAST);
        localPart_AST = currentAST.root;
        this.returnAST = localPart_AST;
    }
    
    protected void buildTokenTypeASTClassMap() {
        this.tokenTypeToASTClassMap = null;
    }
    
    private static final long[] mk_tokenSet_0() {
        final long[] data = { -576460752303423504L, 4095L, 0L, 0L };
        return data;
    }
    
    private static final long[] mk_tokenSet_1() {
        final long[] data = { 436286351340470274L, 0L };
        return data;
    }
    
    private static final long[] mk_tokenSet_2() {
        final long[] data = { -14L, 4095L, 0L, 0L };
        return data;
    }
    
    private static final long[] mk_tokenSet_3() {
        final long[] data = { 436286626218377218L, 0L };
        return data;
    }
    
    private static final long[] mk_tokenSet_4() {
        final long[] data = { 292171025825660930L, 0L };
        return data;
    }
    
    static {
        _tokenNames = new String[] { "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "ABSOLUTE_LOCATION_PATH", "ABSOLUTE_DESCENDANT_LOCATION_PATH", "CHILD_AXIS", "DESCENDANT_AXIS", "REVERSE_AXIS", "SELF_AXIS", "AXIS_SPECIFIER", "ABBREVIATED_ATTRIBUTE_AXIS", "\"ancestor\"", "\"ancestor-or-self\"", "\"attribute\"", "\"child\"", "\"descendant\"", "\"descendant-or-self\"", "\"following\"", "\"following-sibling\"", "\"namespace\"", "\"parent\"", "\"preceding\"", "\"preceding-sibling\"", "\"self\"", "NODE_TYPE", "\"comment\"", "\"text\"", "\"processing-instruction\"", "\"node\"", "UNPREFIXED_WILDCARD", "UNPREFIXED_QNAME", "PREFIXED_WILDCARD", "NULL_PREFIXED_WILDCARD", "PREFIXED_QNAME", "NULL_PREFIXED_QNAME", "PREDICATE", "an identifier", "STAR", "NEGAIVE_QUANTITY", "PLUS_SIGN", "METHOD_CALL", "DECIMAL_LITERAL", "DOT_DOT", "MULTIPLY", "DIVIDE", "RELATIVE_LOCATION_PATH", "STEP", "PROCESSING_INSTRUCTION", "PIPE", "SLASH", "SLASH_SLASH", "DOT", "COLON_COLON", "AT", "LEFT_PAREN", "RIGHT_PAREN", "COLON", "LEFT_BRACKET", "RIGHT_BRACKET", "a string", "a string", "a number", "numbers (0-9)", "whitespace", "COMMA", "EQUAL", "POWER", "ADD", "SUBTRACT", "NE", "GT", "GTE", "LT", "LTE", "DOLLAR_SIGN" };
        _tokenSet_0 = new BitSet(mk_tokenSet_0());
        _tokenSet_1 = new BitSet(mk_tokenSet_1());
        _tokenSet_2 = new BitSet(mk_tokenSet_2());
        _tokenSet_3 = new BitSet(mk_tokenSet_3());
        _tokenSet_4 = new BitSet(mk_tokenSet_4());
    }
}
