package com.northconcepts.datapipeline.transform;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.FieldDeclarations;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.RecordExpressionContext;
import com.northconcepts.datapipeline.internal.parser.Parser;

public class SetCalculatedField extends Transformer
{
    private final String name;
    private final Expression expression;
    private final RecordExpressionContext expressionContext;
    private final boolean overwrite;
    
    public SetCalculatedField(final String name, final String expression) {
        this(name, expression, true);
    }
    
    public SetCalculatedField(final String name, final String expression, final boolean overwrite) {
        super();
        this.name = name;
        this.expression = Parser.parseExpression(expression);
        this.expressionContext = new RecordExpressionContext();
        this.overwrite = overwrite;
    }
    
    public SetCalculatedField(final String name, final String expression, final FieldDeclarations fieldDeclarations) {
        this(name, expression, fieldDeclarations, true);
    }
    
    public SetCalculatedField(final String name, final String expression, final FieldDeclarations fieldDeclarations, final boolean overwrite) {
        super();
        this.name = name;
        this.expression = Parser.parseExpression(expression);
        this.expressionContext = new RecordExpressionContext(fieldDeclarations);
        this.overwrite = overwrite;
    }
    
    public String getExpression() {
        return this.expression.getSourceString();
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean getOverwrite() {
        return this.overwrite;
    }
    
    @Override
	public String toString() {
        return "set calculated field \"" + this.name + "\" to expression \"" + this.getExpression() + "\"" + (this.overwrite ? " allow overwrite" : " no overwrite");
    }
    
    @Override
	public boolean transform(final Record record) throws Throwable {
        try {
            if (!this.overwrite && record.containsField(this.name)) {
                return true;
            }
            this.expressionContext.setRecord(record);
            record.getField(this.name, true).setValue(this.expression.evaluate(this.expressionContext));
            return true;
        }
        catch (Throwable e) {
            throw DataException.wrap(e).set("name", this.name).set("overwrite", this.overwrite).set("expression", this.getExpression());
        }
    }
}
