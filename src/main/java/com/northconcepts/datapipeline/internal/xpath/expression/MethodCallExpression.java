package com.northconcepts.datapipeline.internal.xpath.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Functions;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class MethodCallExpression extends Expression
{
    private final Method method;
    private final List<Expression> arguments;
    
    public MethodCallExpression(final String methodSignature) throws NoSuchMethodException, ClassNotFoundException {
        this(getMethodFromString(methodSignature));
    }
    
    public MethodCallExpression(final Method method) {
        super();
        this.arguments = new ArrayList<Expression>();
        if (!Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
            throw new DataException(TypeUtil.getJavaName(method.getDeclaringClass()) + "." + TypeUtil.getSignature(method, false) + " is not \"public\" and \"static\"");
        }
        this.method = method;
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        return Expression.mapType(this.method.getReturnType());
    }
    
    public void addArgument(final Expression argument) {
        this.arguments.add(argument);
    }
    
    public void addArguments(final Expression[] arguments) {
        for (int i = 0; i < arguments.length; ++i) {
            this.arguments.add(arguments[i]);
        }
    }
    
    public void addArgument(final long argument) {
        throw new AbstractMethodError("TODO");
    }
    
    public void addArgument(final double argument) {
        throw new AbstractMethodError("TODO");
    }
    
    public void addArgument(final String argument) {
        throw new AbstractMethodError("TODO");
    }
    
    private Expression getArgument(final int index) {
        return this.arguments.get(index);
    }
    
    private Expression[] getExpressions() {
        return this.arguments.toArray(new Expression[this.arguments.size()]);
    }
    
    private Object[] evaluateArguments(final ExpressionContext expressionContext) throws Throwable {
        final Class<?>[] argumentTypes = this.method.getParameterTypes();
        final Object[] result = new Object[this.arguments.size()];
        for (int i = 0; i < this.arguments.size(); ++i) {
            result[i] = TypeUtil.convertToType(this.getArgument(i).evaluate(expressionContext), argumentTypes[i]);
        }
        return result;
    }
    
    @Override
	public Object evaluate(final ExpressionContext expressionContext) {
        try {
            Object result = this.method.invoke(null, this.evaluateArguments(expressionContext));
            result = TypeUtil.convertToType(result, this.getType(expressionContext));
            return result;
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    @Override
	public String getSourceString() {
        String s = TypeUtil.getJavaName(this.method.getDeclaringClass()) + "." + this.method.getName() + "(";
        final Expression[] e = this.getExpressions();
        if (e.length > 0) {
            s += e[0].getSourceString();
        }
        for (int i = 1; i < e.length; ++i) {
            s = s + ", " + e[i].getSourceString();
        }
        s += ")";
        return s;
    }
    
    private static Method getMethodFromString(final String alias) throws NoSuchMethodException, ClassNotFoundException {
        final String function = Functions.get(alias);
        final String methodSignature = (function == null) ? alias : function;
        try {
            final int index = methodSignature.lastIndexOf(46);
            if (index < 1) {
                throw new DataException("no class name specified, expected something like java.lang.Integer.parseInt");
            }
            final String className = methodSignature.substring(0, index);
            final String methodName = methodSignature.substring(index + 1);
            final Class<?> clazz = Class.forName(className);
            final Method[] methods = clazz.getMethods();
            for (int i = 0; i < methods.length; ++i) {
                final Method m = methods[i];
                if (m.getName().equals(methodName)) {
                    return m;
                }
            }
            throw new NoSuchMethodException(methodSignature);
        }
        catch (Throwable e) {
            throw DataException.wrap("invalid method: " + methodSignature, e).set("MethodCallExpression.alias", methodSignature).set("MethodCallExpression.methodSignature", methodSignature);
        }
    }
}
