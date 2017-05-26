package com.northconcepts.datapipeline.internal.expression.arithmetic;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class ArithmeticCoercionExpression extends ArithmeticExpression {
	private final Expression expression;

	public ArithmeticCoercionExpression(final Expression expression) {
		super();
		this.expression = expression;
	}

	@Override
	public Class<?> getType(final ExpressionContext expressionContext) {
		if (this.expression instanceof ArithmeticExpression) {
			return ((ArithmeticExpression) this.expression).getType(expressionContext);
		}
		final Class<?> type = this.expression.getType(expressionContext);
		if (Expression.NUMBER_TYPE.isAssignableFrom(type)) {
			return type;
		}
		throw new DataException("cannot coerce [" + this.expression.getClass() + "] -> [" + type + "]");
	}

	public Expression getExpression() {
		return this.expression;
	}

	@Override
	public double evaluateDouble(final ExpressionContext expressionContext) {
		try {
			final Number o = (Number)TypeUtil.convertToType(this.expression.evaluate(expressionContext), this.getType(expressionContext));
			if (o == null) {
				return 0.0;
			}
			return o.doubleValue();
		} catch (Throwable e) {
			throw DataException.wrap(e);
		}
	}

	@Override
	public long evaluateLong(final ExpressionContext expressionContext) {
		try {
			final Number o = (Number)TypeUtil.convertToType(this.expression.evaluate(expressionContext), this.getType(expressionContext));
			if (o == null) {
				return 0L;
			}
			return o.longValue();
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
