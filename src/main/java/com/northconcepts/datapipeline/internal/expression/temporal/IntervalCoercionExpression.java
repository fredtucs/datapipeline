package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Interval;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class IntervalCoercionExpression extends IntervalExpression {
	private final Expression expression;

	public IntervalCoercionExpression(final Expression expression) {
		super();
		this.expression = expression;
	}

	public Expression getExpression() {
		return this.expression;
	}

	@Override
	public Interval evaluateInterval(final ExpressionContext expressionContext) {
		try {
			return (Interval) TypeUtil.convertToType(this.expression.evaluate(expressionContext), this.getType(expressionContext));
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}
	}

	@Override
	public String getSourceString() {
		return this.expression.getSourceString();
	}

	@Override
	public boolean isLiteral() {
		return this.expression.isLiteral();
	}

	@Override
	public boolean isVariable() {
		return this.expression.isVariable();
	}

	@Override
	public boolean isQuantity() {
		return this.expression.isQuantity();
	}
}
