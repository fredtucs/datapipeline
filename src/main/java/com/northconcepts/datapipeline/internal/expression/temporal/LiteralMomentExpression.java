package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class LiteralMomentExpression extends MomentExpression
{
    private final String type;
    private final Expression moment;
    
    public LiteralMomentExpression(final String type, final Expression moment) {
        super();
        this.type = type;
        this.moment = moment;
    }
    
    @Override
	public Moment evaluateMoment(final ExpressionContext expressionContext) {
        try {
            return (Moment)TypeUtil.convertToType(this.moment.evaluate(expressionContext), this.getType(expressionContext));
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    @Override
	public String toString() {
        if (this.moment == null) {
            return "";
        }
        return this.moment.toString();
    }
    
    @Override
	public String getSourceString() {
        return this.type + " " + this.moment.getSourceString();
    }
    
    @Override
	public boolean isLiteral() {
        return true;
    }
    
    @Override
	public boolean isVariable() {
        return false;
    }
}
