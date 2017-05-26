package com.northconcepts.datapipeline.internal.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Functions;
import com.northconcepts.datapipeline.internal.expression.arithmetic.LiteralArithmeticExpression;
import com.northconcepts.datapipeline.internal.expression.character.LiteralCharacterExpression;
import com.northconcepts.datapipeline.internal.lang.Pointer;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;

public class MethodCallExpression extends Expression
{
    private final List<Method> methods;
    private final List<Expression> arguments;
    
    public MethodCallExpression(final String methodSignature) throws NoSuchMethodException, ClassNotFoundException {
        this(getMethodFromString(methodSignature));
    }
    
    public MethodCallExpression(final List<Method> methods) {
        super();
        this.arguments = new ArrayList<Expression>();
        for (final Method m : methods) {
            if (!Modifier.isStatic(m.getModifiers()) || !Modifier.isPublic(m.getModifiers())) {
                throw new DataException(TypeUtil.getJavaName(m.getDeclaringClass()) + "." + TypeUtil.getSignature(m, false) + " is not \"public\" and \"static\"");
            }
        }
        this.methods = methods;
    }
    
    public List<Method> getMethods() {
        return this.methods;
    }
    
    @Override
	public Class<?> getType(final ExpressionContext expressionContext) {
        try {
            final Object[] arguments = this.evaluateArguments(expressionContext);
            final Method method = findMethod(this.methods, arguments);
            return Expression.mapType(method.getReturnType());
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
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
        this.addArgument(new LiteralArithmeticExpression(argument, String.valueOf(argument)));
    }
    
    public void addArgument(final double argument) {
        this.addArgument(new LiteralArithmeticExpression(argument, String.valueOf(argument)));
    }
    
    public void addArgument(final String argument) {
        this.addArgument(new LiteralCharacterExpression(argument, "\""));
    }
    
    private Expression getArgument(final int index) {
        return this.arguments.get(index);
    }
    
    private Expression[] getExpressions() {
        return this.arguments.toArray(new Expression[this.arguments.size()]);
    }
    
    private Object[] evaluateArguments(final ExpressionContext expressionContext) throws Throwable {
        final Object[] result = new Object[this.arguments.size()];
        for (int i = 0; i < this.arguments.size(); ++i) {
            result[i] = this.getArgument(i).evaluate(expressionContext);
        }
        return result;
    }
    
    @Override
	public Object evaluate(final ExpressionContext expressionContext) {
        try {
            final Object[] arguments = this.evaluateArguments(expressionContext);
            final Method method = findMethod(this.methods, arguments);
            final Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < arguments.length; ++i) {
                arguments[i] = TypeUtil.convertToType(arguments[i], parameterTypes[i]);
            }
            Object result = method.invoke(null, arguments);
            result = TypeUtil.convertToType(result, this.getType(expressionContext));
            return result;
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    @Override
	public String getSourceString() {
        final Method method = this.methods.get(0);
        String s = TypeUtil.getJavaName(method.getDeclaringClass()) + "." + method.getName() + "(";
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
    
    private static List<Method> getMethodFromString(final String alias) throws NoSuchMethodException, ClassNotFoundException {
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
            final List<Method> result = new ArrayList<Method>();
            for (int i = 0; i < methods.length; ++i) {
                final Method m = methods[i];
                if (m.getName().equals(methodName) && Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers())) {
                    result.add(m);
                }
            }
            if (result.size() == 0) {
                throw new NoSuchMethodException(methodSignature);
            }
            return result;
        }
        catch (Throwable e) {
            throw DataException.wrap("invalid method: " + methodSignature, e).set("MethodCallExpression.alias", alias).set("MethodCallExpression.methodSignature", methodSignature);
        }
    }
    
    private static Pointer<Integer> diff(Class<?> type, final Object value) {
        if (value == null) {
            if (type.isPrimitive()) {
                return new Pointer<Integer>(1000);
            }
            return new Pointer<Integer>(0);
        }
        else {
            type = TypeUtil.primitiveToObjectType(type);
            if (Number.class.isAssignableFrom(type) && Number.class.isAssignableFrom(value.getClass())) {
                return new Pointer<Integer>(TypeUtil.diffNumberTypes(type, value.getClass()));
            }
            return TypeUtil.diffTypes(type, value.getClass());
        }
    }
    
    private static Pointer<Integer> score(final Method method, final Object[] arguments) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != arguments.length) {
            return null;
        }
        final Pointer<Integer> score = new Pointer<Integer>(0);
        for (int i = 0; i < parameterTypes.length; ++i) {
            final Pointer<Integer> methodScore = diff(parameterTypes[i], arguments[i]);
            if (methodScore == null) {
                return null;
            }
            final Pointer<Integer> pointer = score;
            pointer.value += methodScore.value;
        }
        return score;
    }
    
    private static Method findMethod(final List<Method> methods, final Object[] arguments) {
        int lowestValue = 0;
        Method bestMethod = null;
        for (int i = 0; i < methods.size(); ++i) {
            final Method method = methods.get(i);
            final Pointer<Integer> score = score(method, arguments);
            if (score != null) {
                final int valueMagnitude = Math.abs(score.value);
                if (bestMethod == null || valueMagnitude < lowestValue) {
                    bestMethod = method;
                    lowestValue = valueMagnitude;
                }
            }
        }
        if (bestMethod == null) {
            throw new DataException("no method found matching arguments").set("methods", methods).set("arguments", arguments);
        }
        return bestMethod;
    }
}
