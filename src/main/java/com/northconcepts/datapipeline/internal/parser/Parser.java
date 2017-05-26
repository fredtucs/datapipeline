package com.northconcepts.datapipeline.internal.parser;

import java.io.StringReader;

import antlr.TokenStream;
import antlr.collections.AST;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.logical.EqualToExpression;
import com.northconcepts.datapipeline.internal.expression.logical.LiteralLogicalExpression;
import com.northconcepts.datapipeline.internal.expression.logical.LogicalExpression;
import com.northconcepts.datapipeline.internal.expression.untyped.UntypedRelationalExpression;

public class Parser
{
    public static Expression parseExpression(final String expressionAsString) {
        try {
            if (expressionAsString == null) {
                return null;
            }
            final StringReader input = new StringReader(expressionAsString);
            final DataPipelineLexer lexer = new DataPipelineLexer(input);
            final DataPipelineParser parser = new DataPipelineParser(lexer);
            parser.expression();
            final AST ast = parser.getAST();
            final DataPipelineTreeParser treeParser = new DataPipelineTreeParser();
            final Expression expression = treeParser.expression(ast);
            return expression;
        }
        catch (Throwable e) {
            throw DataException.wrap(e).set("expression", expressionAsString);
        }
    }
    
    public static LogicalExpression parseLogicalExpression(final String expressionAsString) {
        final Expression expression = parseExpression(expressionAsString);
        if (expression instanceof LogicalExpression) {
            return (LogicalExpression)expression;
        }
        if (expression instanceof UntypedRelationalExpression) {
            return new EqualToExpression(expression, LiteralLogicalExpression.TRUE, "=");
        }
        throw new DataException("expected a logical (true/false) expression (like x > 23)").set("expressionAsString", expressionAsString).set("expression", expression);
    }
}
