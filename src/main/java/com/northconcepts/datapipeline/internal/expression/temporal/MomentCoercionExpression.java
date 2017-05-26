package com.northconcepts.datapipeline.internal.expression.temporal;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.expression.Expression;
import com.northconcepts.datapipeline.internal.expression.ExpressionContext;
import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class MomentCoercionExpression extends MomentExpression {
	private final Expression expression;

	public MomentCoercionExpression(final Expression expression) {
		super();
		this.expression = expression;
	}

	public Expression getExpression() {
		return this.expression;
	}

	@Override
	public Moment evaluateMoment(final ExpressionContext expressionContext) {
		try {
			return (Moment) TypeUtil.convertToType(this.expression.evaluate(expressionContext), this.getType(expressionContext));
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
