package com.northconcepts.datapipeline.internal.lang.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MethodFinder {
	private final Class<?> type;
	private final List<MethodCriteria> criteria;
	private final Method[] methods;

	public static MethodFinder from(final Class<?> type) {
		return new MethodFinder(type);
	}

	private MethodFinder(final Class<?> type) {
		super();
		this.criteria = new ArrayList<MethodCriteria>();
		this.type = type;
		this.methods = type.getMethods();
	}

	public MethodFinder reset() {
		this.criteria.clear();
		return this;
	}

	public MethodFinder where(final MethodCriteria... criteria) {
		if (criteria != null) {
			for (final MethodCriteria c : criteria) {
				this.criteria.add(c);
			}
		}
		return this;
	}

	public MethodFinder whereArgCount(final int count) {
		return this.where(new MethodCriteria() {
			public boolean matches(final Method method) {
				return method.getParameterTypes().length == count;
			}
		});
	}

	public MethodFinder whereNameStartsWith(final String... prefix) {
		return this.where(new MethodCriteria() {
			public boolean matches(final Method method) {
				final String name = method.getName();
				for (final String p : prefix) {
					if (name.startsWith(p)) {
						return true;
					}
				}
				return false;
			}
		});
	}

	public MethodFinder whereNameEquals(final String name, final boolean caseSensitive) {
		return this.where(new MethodCriteria() {
			public boolean matches(final Method method) {
				final String methodName = method.getName();
				if (caseSensitive) {
					return name.equals(methodName);
				}
				return name.equalsIgnoreCase(methodName);
			}
		});
	}

	public MethodFinder whereNameNotEquals(final String name, final boolean caseSensitive) {
		return this.where(new MethodCriteria() {
			public boolean matches(final Method method) {
				final String methodName = method.getName();
				if (caseSensitive) {
					return !name.equals(methodName);
				}
				return !name.equalsIgnoreCase(methodName);
			}
		});
	}

	public MethodFinder whereArgsMatch(final Class<?>... types) {
		return this.where(new MethodCriteria() {
			public boolean matches(final Method method) {
				final Class<?>[] actualTypes = method.getParameterTypes();
				if (actualTypes.length != types.length) {
					return false;
				}
				for (int i = 0; i < types.length; ++i) {
					if (!actualTypes[i].equals(types[i])) {
						return false;
					}
				}
				return true;
			}
		});
	}

	public MethodFinder whereArgsAssignableFrom(final Class<?>... types) {
		return this.where(new MethodCriteria() {
			public boolean matches(final Method method) {
				final Class<?>[] actualTypes = method.getParameterTypes();
				if (actualTypes.length != types.length) {
					return false;
				}
				for (int i = 0; i < types.length; ++i) {
					if (!actualTypes[i].isAssignableFrom(types[i])) {
						return false;
					}
				}
				return true;
			}
		});
	}

	public MethodFinder whereReturnAssignableTo(final Class<?>... types) {
		return this.where(new MethodCriteria() {
			public boolean matches(final Method method) {
				final Class<?> returnType = method.getReturnType();
				for (final Class<?> type : types) {
					if (type.isAssignableFrom(returnType)) {
						return true;
					}
				}
				return false;
			}
		});
	}

	public MethodFinder whereReturnMatches(final Class<?>... types) {
		return this.where(new MethodCriteria() {
			public boolean matches(final Method method) {
				final Class<?> returnType = method.getReturnType();
				for (final Class<?> type : types) {
					if (type.equals(returnType)) {
						return true;
					}
				}
				return false;
			}
		});
	}

	public MethodFinder whereReturnAssignableToCollectionOf(final Class<?> type) {
		return this.where(new MethodCriteria() {
			public boolean matches(final Method method) {
				final Type genericReturnType = method.getGenericReturnType();
				if (!(genericReturnType instanceof ParameterizedType)) {
					return false;
				}
				final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
				if (!Collection.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
					return false;
				}
				final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				return actualTypeArguments.length == 1 && type.isAssignableFrom((Class) actualTypeArguments[0]);
			}
		});
	}

	public MethodFinder orderBy() {
		return this;
	}

	public List<Method> find() {
		final List<Method> result = new ArrayList<Method>();
		final Method[] arr$ = this.methods;
		final int len$ = arr$.length;
		int i$ = 0;
		Label_0019: while (i$ < len$) {
			final Method method = arr$[i$];
			while (true) {
				for (final MethodCriteria criteria : this.criteria) {
					if (!criteria.matches(method)) {
						++i$;
						continue Label_0019;
					}
				}
				result.add(method);
				continue;
			}
		}
		return result;
	}

	public Method findOne() {
		final List<Method> list = this.find();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public interface MethodCriteria {
		boolean matches(Method p0);
	}
}
