package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class VariableMomentExpression extends MomentExpression {
	private final String name;

	public VariableMomentExpression(final String name) {
		super();
		this.name = name;
	}

	@Override
	public Moment evaluateMoment(final ExpressionContext expressionContext) {
		try {
			return (Moment) TypeUtil.convertToType(ExpressionContext.Helper.getValue(expressionContext, this.name), this.getType(expressionContext));
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}
	}

	@Override
	public String getSourceString() {
		return this.name;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public boolean isVariable() {
		return true;
	}
}
