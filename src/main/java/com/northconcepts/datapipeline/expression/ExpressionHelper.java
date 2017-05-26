package com.northconcepts.datapipeline.expression;

import com.northconcepts.datapipeline.internal.parser.Parser;

public class ExpressionHelper
{
    public static ASTNode parseExpression(final String expressionAsString) throws ParseException {
        try {
            if (expressionAsString == null) {
                throw new NullPointerException("expression is null");
            }
            if (expressionAsString.trim().length() == 0) {
                throw new NullPointerException("expression is empty");
            }
            return Parser.parseExpression(expressionAsString);
        }
        catch (Throwable e) {
            throw new ParseException(e).setExpressionAsString(expressionAsString);
        }
    }
    
    public static ASTNode parseLogicalExpression(final String expressionAsString) throws ParseException {
        try {
            if (expressionAsString == null) {
                throw new NullPointerException("expression is null");
            }
            if (expressionAsString.trim().length() == 0) {
                throw new NullPointerException("expression is empty");
            }
            return Parser.parseLogicalExpression(expressionAsString);
        }
        catch (Throwable e) {
            throw new ParseException(e).setExpressionAsString(expressionAsString);
        }
    }
}
