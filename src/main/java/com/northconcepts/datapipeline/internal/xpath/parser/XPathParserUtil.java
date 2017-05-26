package com.northconcepts.datapipeline.internal.xpath.parser;

import java.io.StringReader;

import org.apache.log4j.Logger;

import antlr.TokenStream;
import antlr.collections.AST;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.xpath.LocationPath;

public class XPathParserUtil
{
    public static final Logger log;
    
    public static LocationPath parseLocationPath(final String locationPathAsString) {
        try {
            if (locationPathAsString == null) {
                return null;
            }
            final StringReader input = new StringReader(locationPathAsString);
            final XPathLexer lexer = new XPathLexer(input);
            final XPathParser parser = new XPathParser(lexer);
            parser.locationPathPart();
            final AST ast = parser.getAST();
            final XPathTreeParser treeParser = new XPathTreeParser();
            final LocationPath path = treeParser.locationPathPart(ast);
            path.setSourceString(locationPathAsString);
            return path;
        }
        catch (Throwable e) {
            throw DataException.wrap(e).set("expression", locationPathAsString);
        }
    }
    
    static {
        log = DataEndpoint.log;
    }
}
