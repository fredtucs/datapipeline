package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Interval;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class VariableIntervalExpression extends IntervalExpression {
	private final String name;

	public VariableIntervalExpression(final String name) {
		super();
		this.name = name;
	}

	@Override
	public Interval evaluateInterval(final ExpressionContext expressionContext) {
		try {
			return (Interval) TypeUtil
					.convertToType(ExpressionContext.Helper.getValue(expressionContext, this.name), this.getType(expressionContext));
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
