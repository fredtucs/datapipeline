package com.northconcepts.datapipeline.internal.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.internal.lang.comparator.BaseComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.BigDecimalComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.BigIntegerComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.BooleanComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.ByteComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.CaseInsensitiveStringComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.CharacterComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.DateComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.DoubleComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.FloatComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.IntegerComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.LongComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.SQLDateComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.SQLTimeComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.SQLTimestampComparator;
import com.northconcepts.datapipeline.internal.lang.comparator.ShortComparator;

public final class TypeUtil {
	private static final HashMap<Class<?>, FieldType> CLASS_TO_TYPE;
	private static final int[] JDBC_TYPES;
	private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_OBJECT_TYPE;
	private static final Map<String, String> PRIMITIVE_INTERNAL_NAME;
	private static final Map<String, Class<?>> PRIMITIVE_TYPE;
	public static final BigDecimalComparator BIGDECIMAL_COMPARATOR;
	public static final BigIntegerComparator BIGINTEGER_COMPARATOR;
	public static final BooleanComparator BOOLEAN_COMPARATOR;
	public static final ByteComparator BYTE_COMPARATOR;
	public static final CharacterComparator CHARACTER_COMPARATOR;
	public static final DateComparator DATE_COMPARATOR;
	public static final DoubleComparator DOUBLE_COMPARATOR;
	public static final FloatComparator FLOAT_COMPARATOR;
	public static final IntegerComparator INTEGER_COMPARATOR;
	public static final LongComparator LONG_COMPARATOR;
	public static final CaseInsensitiveStringComparator OBJECT_COMPARATOR;
	public static final ShortComparator SHORT_COMPARATOR;
	public static final SQLDateComparator SQL_DATE_COMPARATOR;
	public static final SQLTimeComparator SQL_TIME_COMPARATOR;
	public static final SQLTimestampComparator SQL_TIMESTAMP_COMPARATOR;
	public static final CaseInsensitiveStringComparator STRING_COMPARATOR;
	public static final SQLTimestampComparator TIMESTAMP_COMPARATOR;
	private static final Map<Class<?>, BaseComparator<?>> DEFAULT_COMPARATOR;
	private static final Map<String, String> METHOD_SUFFIX;
	private static final List<Class<?>> NUMBER_TYPES_LIST;

	public static FieldType getFieldTypeForClass(final Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		final FieldType fieldType = TypeUtil.CLASS_TO_TYPE.get(clazz);
		return fieldType;
	}

	public static BaseComparator<?> getDefaultComparator(final Class<?> type) {
		BaseComparator<?> c = TypeUtil.DEFAULT_COMPARATOR.get(type);
		if (c == null) {
			final Class<?> superType = type.getSuperclass();
			if (superType == null) {
				c = TypeUtil.OBJECT_COMPARATOR;
			} else {
				c = getDefaultComparator(superType);
			}
		}
		return c;
	}

	public static Class<?> primitiveToObjectType(final Class<?> primitive) {
		Class<?> c = TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.get(primitive);
		if (c == null) {
			c = primitive;
		}
		return c;
	}

	public static Class<?> objectToPrimitiveType(final Class<?> type) {
		if (type.isPrimitive()) {
			return type;
		}
		return Util.getKeyForValue(type, TypeUtil.PRIMITIVE_TO_OBJECT_TYPE);
	}

	public static boolean isPrimitiveObjectType(final Class<?> type) {
		return !type.isArray() && TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.containsValue(type);
	}

	public static boolean isPrimitiveType(final String className) {
		return TypeUtil.PRIMITIVE_TYPE.containsKey(className);
	}

	private static String getInternalClassName(final String className) {
		if (className == null) {
			return null;
		}
		final int arrayPartIndex = className.indexOf("[]");
		if (arrayPartIndex < 1) {
			return className;
		}
		final int arrayDimensions = (className.length() - arrayPartIndex) / 2;
		final String componentPart = className.substring(0, arrayPartIndex);
		final String arrrayPart = Util.repeat("[", arrayDimensions);
		if (isPrimitiveType(componentPart)) {
			return arrrayPart + TypeUtil.PRIMITIVE_INTERNAL_NAME.get(componentPart);
		}
		return arrrayPart + "L" + componentPart + ";";
	}

	public static Class<?> getClass(String name) throws ClassNotFoundException {
		Class<?> c = TypeUtil.PRIMITIVE_TYPE.get(name);
		if (c == null) {
			name = getInternalClassName(name);
			c = Class.forName(name);
		}
		return c;
	}

	public static Class<?> getClass(String name, final boolean initialize, final ClassLoader classLoader) throws ClassNotFoundException {
		Class<?> c = TypeUtil.PRIMITIVE_TYPE.get(name);
		if (c == null) {
			name = getInternalClassName(name);
			c = Class.forName(name, initialize, classLoader);
		}
		return c;
	}

	public static Class<?> getComponentType(Class<?> clazz) throws ClassNotFoundException {
		if (clazz == null) {
			throw new NullPointerException("clazz is null");
		}
		while (clazz.isArray()) {
			clazz = clazz.getComponentType();
		}
		return clazz;
	}

	public static int getComponentDimensions(Class<?> clazz) throws ClassNotFoundException {
		if (clazz == null) {
			throw new NullPointerException("clazz is null");
		}
		int arrayDimensions;
		for (arrayDimensions = 0; clazz.isArray(); clazz = clazz.getComponentType(), ++arrayDimensions) {
		}
		return arrayDimensions;
	}

	public static URL getClassURL(Class<?> clazz) throws ClassNotFoundException {
		if (clazz == null) {
			throw new NullPointerException("clazz is null");
		}
		while (clazz.isArray()) {
			clazz = clazz.getComponentType();
		}
		final String className = clazz.getName();
		if (TypeUtil.PRIMITIVE_TYPE.containsKey(className)) {
			throw new ClassNotFoundException("\"" + className + "\" is a primitive type");
		}
		final String classFile = className.replace('.', '/') + ".class";
		return clazz.getClassLoader().getResource(classFile);
	}

	public static URL getClassURL(final String className, final ClassLoader classLoader) throws ClassNotFoundException {
		if (className == null) {
			throw new NullPointerException("className is null");
		}
		if (TypeUtil.PRIMITIVE_TYPE.containsKey(className)) {
			throw new ClassNotFoundException("\"" + className + "\" is a primitive type");
		}
		final String classFile = className.replace('.', '/') + ".class";
		return classLoader.getResource(classFile);
	}

	public static String getJavaName(Class<?> type) {
		if (type == null) {
			return "";
		}
		if (type.isArray()) {
			final StringBuilder s = new StringBuilder();
			while (type.isArray()) {
				s.append("[]");
				type = type.getComponentType();
			}
			s.insert(0, type.getName());
			return s.toString();
		}
		return type.getName();
	}

	public static String getClassName(final Class<?> type) {
		if (type == null) {
			return "";
		}
		final Package p = type.getPackage();
		return (p == null) ? type.getName() : type.getName().substring(p.getName().length() + 1);
	}

	public static String getPackageName(final Class<?> type) {
		if (type == null) {
			return "";
		}
		final Package p = type.getPackage();
		return (p == null) ? "" : p.getName();
	}

	public static String getDefaultValueAsString(final Class<?> type) {
		if (type == null || type == Void.TYPE) {
			return "";
		}
		if (!type.isPrimitive()) {
			return "null";
		}
		if (type == Double.TYPE || type == Float.TYPE) {
			return "0.0";
		}
		if (type == Long.TYPE || type == Integer.TYPE || type == Short.TYPE || type == Byte.TYPE) {
			return "0";
		}
		if (type == Character.TYPE) {
			return "0";
		}
		if (type == Boolean.TYPE) {
			return "false";
		}
		throw new RuntimeException("unknown type: " + type.getName());
	}

	public static Object getDefaultValue(final Class<?> type) {
		if (type == null || type == Void.TYPE || !type.isPrimitive()) {
			return null;
		}
		if (type == Integer.TYPE) {
			return new Integer(0);
		}
		if (type == Boolean.TYPE) {
			return new Boolean(false);
		}
		if (type == Long.TYPE) {
			return new Long(0L);
		}
		if (type == Double.TYPE) {
			return new Double(0.0);
		}
		if (type == Float.TYPE) {
			return new Float(0.0f);
		}
		if (type == Character.TYPE) {
			return new Character('\0');
		}
		if (type == Byte.TYPE) {
			return new Byte((byte) 0);
		}
		if (type == Short.TYPE) {
			return new Short((short) 0);
		}
		throw new RuntimeException("unknown type: " + type.getName());
	}

	public static Class<?> sqlToJavaType(final int sqlTypeIndex, final int size, final int decimals, final boolean nullable) {
		String typeName = null;
		switch (sqlTypeIndex) {
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
		case Types.BIGINT:
		case Types.DECIMAL:
		case Types.NUMERIC:
		case Types.REAL:
		case Types.DOUBLE:
		case Types.FLOAT:
			if (decimals > 0) // Floating point numbers
			{
				if (size + decimals < 38) {
					// typeName = "float";
					typeName = "double";
				} else if (size + decimals < 308) {
					typeName = "double";
				} else {
					typeName = "java.math.BigDecimal";
				}
			} else // Integer numbers
			{
				if (size == 0) {
					typeName = "int";
				} else if (size < 3) {
					typeName = "byte";
				} else if (size < 5) {
					// typeName = "short";
					typeName = "int";
				} else if (size < 10) {
					typeName = "int";
				} else {
					typeName = "long";
				}

			}
			break;
		case Types.BIT:
			typeName = "boolean";
			break;
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			if (size == 1) {
				typeName = "char";
			} else {
				typeName = "java.lang.String";
			}
			break;
		case Types.NULL:
			typeName = "java.lang.String";
			break;
		case 11:
		case Types.DATE:
			typeName = "java.sql.Date";
			break;
		case Types.TIME:
			typeName = "java.sql.Time";
			break;
		case Types.TIMESTAMP:
			typeName = "java.sql.Timestamp";
			break;
		case Types.BINARY:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
			typeName = "byte[]";
			break;
		case Types.OTHER:
			typeName = "java.lang.Object";
			break;
		default:
			typeName = "java.lang.Object";
		}

		try {
			Class<?> defaultJavaType = getClass(typeName);
			if (nullable) {
				defaultJavaType = primitiveToObjectType(defaultJavaType);
			}
			return defaultJavaType;
		} catch (ClassNotFoundException e) {
			throw DataException.wrap(e);
		}
	}

	public static String getSQLMethodSuffix(final Class<?> type) {
		String methodSuffix = TypeUtil.METHOD_SUFFIX.get(type.getName());
		if (methodSuffix == null) {
			methodSuffix = getSQLMethodSuffix(type.getSuperclass());
		}
		return methodSuffix;
	}

	public static String getPrimitiveConverterMethod(final Class<?> type) {
		return getPrimitiveConverterMethod(type, true);
	}

	public static String getPrimitiveConverterMethod(final Class<?> type, final boolean includeParenthesis) {
		final Class<?> primitive = objectToPrimitiveType(type);
		return primitive.getName() + "Value" + (includeParenthesis ? "()" : "");
	}

	public static String getSignature(final Method method, final boolean includeReturnType) {
		final StringBuilder s = new StringBuilder();
		if (includeReturnType) {
			s.append(getJavaName(method.getReturnType()) + " ");
		}
		s.append(method.getName() + "(");
		final Class<?>[] params = method.getParameterTypes();
		for (int j = 0; j < params.length; ++j) {
			s.append(getJavaName(params[j]));
			if (j < params.length - 1) {
				s.append(",");
			}
		}
		s.append(")");
		return s.toString();
	}

	public static String getMethodAsField(final Method method) throws Throwable {
		String name = getSignature(method, false);
		name = name.replace(' ', '_');
		name = name.replace(',', '_');
		name = name.replace('(', '_');
		name = name.replace(')', '_');
		name = name.replace('.', '_');
		name = name.replace('<', '_');
		name = name.replace('>', '_');
		return name;
	}

	public static String getSignature(final Constructor<?> method) {
		final StringBuilder s = new StringBuilder();
		s.append(method.getName() + "(");
		final Class<?>[] params = method.getParameterTypes();
		for (int j = 0; j < params.length; ++j) {
			s.append(getJavaName(params[j]));
			if (j < params.length - 1) {
				s.append(",");
			}
		}
		s.append(")");
		return s.toString();
	}

	public static String getMethodAsField(final Constructor<?> method) throws Throwable {
		String name = getSignature(method);
		name = name.replace(' ', '_');
		name = name.replace('(', '_');
		name = name.replace(')', '_');
		name = name.replace('.', '_');
		name = name.replace('<', '_');
		name = name.replace('>', '_');
		return name;
	}

	private static Method getJavaBeanMethod0(final Class<?> clazz, final String prefix, final String property) {
		final String methodName = prefix + Character.toUpperCase(property.charAt(0)) + property.substring(1);
		try {
			if ("get".equals(prefix) || "is".equals(prefix)) {
				return clazz.getMethod(methodName, new Class[0]);
			}
			if ("set".equals(prefix)) {
				final Method[] methods = clazz.getMethods();
				for (int i = 0; i < methods.length; ++i) {
					if (methods[i].getParameterTypes().length == 1 && methodName.equals(methods[i].getName())) {
						return methods[i];
					}
				}
				return null;
			}
			throw new RuntimeException("unknown prefix [" + prefix + "], expected \"get\", \"is\", or \"set\"");
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	public static Method getJavaBeanReadMethod(final Class<?> clazz, final String property) {
		Method method = getJavaBeanMethod0(clazz, "get", property);
		if (method == null) {
			method = getJavaBeanMethod0(clazz, "is", property);
		}
		return method;
	}

	public static Method getJavaBeanWriteMethod(final Class<?> clazz, final String property) {
		final Method method = getJavaBeanMethod0(clazz, "set", property);
		return method;
	}

	public static String getJavaBeanProperty(final Method method) {
		final int parameterCount = method.getParameterTypes().length;
		final String methodName = method.getName();
		if (parameterCount == 0) {
			if (methodName.startsWith("get")) {
				return getJavaBeanName(methodName, 3);
			}
			if (methodName.startsWith("is")) {
				return getJavaBeanName(methodName, 2);
			}
		} else if (parameterCount == 1 && methodName.startsWith("set")) {
			return getJavaBeanName(methodName, 3);
		}
		return null;
	}

	public static Class<?> getJavaBeanPropertyType(final Method method, final boolean traverseArrays) {
		final int parameterCount = method.getParameterTypes().length;
		final String methodName = method.getName();
		Class<?> type = null;
		if (parameterCount == 0) {
			if (methodName.startsWith("get")) {
				type = method.getReturnType();
			} else if (methodName.startsWith("is")) {
				type = method.getReturnType();
			}
		} else if (parameterCount == 1 && methodName.startsWith("set")) {
			type = method.getParameterTypes()[0];
		}
		if (type != null && traverseArrays) {
			while (type.isArray()) {
				type = type.getComponentType();
			}
		}
		return type;
	}

	public static final String getJavaBeanName(final String name, final int offset) {
		if (name == null || offset + 1 >= name.length()) {
			System.err.println("couldn't create JavaBeanName for [" + name + "]");
			return name;
		}
		final StringBuilder s = new StringBuilder(name.length() - offset);
		s.append(Character.toLowerCase(name.charAt(offset)));
		s.append(name.substring(offset + 1));
		return s.toString();
	}

	public static Object[] invokeJavaBeanMethod(final Method method, final Object[] arguments, final Map<String, Object> map) throws Throwable {
		final int parameterCount = method.getParameterTypes().length;
		final String methodName = method.getName();
		Object result = null;
		boolean invoked = false;
		if (parameterCount == 0) {
			if (methodName.startsWith("get")) {
				invoked = true;
				result = map.get(getJavaBeanName(methodName, 3));
			} else if (methodName.startsWith("is")) {
				invoked = true;
				result = map.get(getJavaBeanName(methodName, 2));
			}
		} else if (parameterCount == 1 && methodName.startsWith("set")) {
			invoked = true;
			map.put(getJavaBeanName(methodName, 3), arguments[0]);
		}
		if (invoked) {
			return new Object[] { getValueOrDefault(method.getReturnType(), result) };
		}
		return null;
	}

	public static final Object getValueOrDefault(final Class<?> type, Object value) {
		if (value == null && type != Void.TYPE && type.isPrimitive()) {
			value = getDefaultValue(type);
		}
		return value;
	}

	// =======================================================================================================

	public static Object invoke(Object target, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
		Method method = target.getClass().getMethod(methodName, parameterTypes);
		return method.invoke(target, parameters);
	}

	public static Object invoke(Object target, String methodName, Class<?> parameterType, Object parameter) throws Throwable {
		return invoke(target, methodName, new Class[] { parameterType }, new Object[] { parameter });
	}

	public static Object invoke(Object target, String methodName) throws Throwable {
		return invoke(target, methodName, (Class[]) null, (Object[]) null);
	}

	// =======================================================================================================

	public static Object create(Class<?> clazz, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
		Constructor<?> constructor = clazz.getConstructor(parameterTypes);
		return constructor.newInstance(parameters);
	}

	public static Object create(Class<?> clazz, Class<?> parameterType, Object parameter) throws Throwable {
		return create(clazz, new Class[] { parameterType }, new Object[] { parameter });
	}

	public static Object create(Class<?> clazz) throws Throwable {
		return create(clazz, (Class[]) null, (Object[]) null);
	}

	// =======================================================================================================

	/**
	 * @see TypeUtil#convertToType(Object, Class)
	 */
	@SuppressWarnings("deprecation")
	private static Object convertToType(final Object value, final Class<?> newType, final boolean throwExceptionOnFailure) throws Throwable {
		if (value == null) {
			return null;
		}
		final Class<?> oldType = value.getClass();
		final Class<?> newObjectType = primitiveToObjectType(newType);
		try {
			if (newObjectType == oldType || newObjectType.isAssignableFrom(oldType)) {
				return value;
			}
			if (newObjectType == String.class) {
				return value.toString();
			}
			if (newObjectType == Character.class) {
				final String s = value.toString();
				if (s != null && s.length() > 0) {
					return new Character(s.charAt(0));
				}
				return new Character((char) 0);
			} else if (Number.class.isAssignableFrom(newObjectType)) {
				if (Number.class.isAssignableFrom(oldType)) {
					return invoke(value, getPrimitiveConverterMethod(newType, false));
				}
				final Number number = (Number) create(newObjectType, String.class, value.toString());
				return invoke(number, getPrimitiveConverterMethod(newType, false));
			} else if (newObjectType == Boolean.class) {
				final String stringValue = value.toString().toLowerCase();
				if ("yes".equals(stringValue) || "true".equals(stringValue)) {
					return new Boolean(true);
				}
				if ("no".equals(stringValue) || "false".equals(stringValue)) {
					return new Boolean(false);
				}
				return null;
			} else if (newObjectType == Moment.class) {
				if (Date.class.isAssignableFrom(oldType)) {
					return new Moment(((Date) value).getTime());
				}
				return Moment.parseMoment(value.toString());
			} else if (newObjectType == Interval.class) {
				if (Date.class.isAssignableFrom(oldType)) {
					return new Interval(((Date) value).getTime());
				}
				return Interval.parseInterval(value.toString());
			} else if (Date.class.isAssignableFrom(newObjectType)) {
				if (Date.class.isAssignableFrom(oldType)) {
					return create(newObjectType, Long.TYPE, new Long(((Date) value).getTime()));
				}
				if (oldType == Moment.class) {
					final Moment o = (Moment) value;
					final long datetimeInMillisecnds = o.getDate().getTime();
					return create(newObjectType, Long.TYPE, new Long(datetimeInMillisecnds));
				}
				if (oldType == Interval.class) {
					final Interval o2 = (Interval) value;
					final long datetimeInMillisecnds = o2.getDate().getTime();
					return create(newObjectType, Long.TYPE, new Long(datetimeInMillisecnds));
				}
				final Date date = new Date(value.toString());
				return create(newObjectType, Long.TYPE, new Long(date.getTime()));
			}
		} catch (Throwable e) {
			if (throwExceptionOnFailure) {
				throw e;
			}
			return null;
		}
		if (throwExceptionOnFailure) {
			throw new DataException("couldn't convert value to new type").set("source.type", getJavaName(oldType))
					.set("target.type", getJavaName(newType)).set("value", value);
		}
		return null;
	}

	public static <T> T convertToType(final Object value, final Class<T> newType) throws Throwable {
		return (T) convertToType(value, newType, true);
	}

	public static <T> T convertToTypeOrNull(final Object value, final Class<T> newType) {
		try {
			return (T) convertToType(value, newType, false);
		} catch (Throwable e) {
			return null;
		}
	}

	public static int getJdbcType(final FieldType type) {
		return TypeUtil.JDBC_TYPES[type.ordinal()];
	}

	public static void setParameterValue(final DataEndpoint endpoint, final Field field, final PreparedStatement statement, final int parameterIndex,
			final int jdbcType) throws SQLException {
		try {
			if (field.isNull()) {
				statement.setNull(parameterIndex, jdbcType);
			} else {
				switch (field.getType()) {
				case STRING: {
					statement.setString(parameterIndex, field.getValueAsString());
					break;
				}
				case INT: {
					statement.setInt(parameterIndex, field.getValueAsInteger());
					break;
				}
				case LONG: {
					statement.setLong(parameterIndex, field.getValueAsLong());
					break;
				}
				case DOUBLE: {
					statement.setDouble(parameterIndex, field.getValueAsDouble());
					break;
				}
				case DATETIME: {
					statement.setTimestamp(parameterIndex, new Timestamp(field.getValueAsDatetime().getTime()));
					break;
				}
				case BOOLEAN: {
					statement.setBoolean(parameterIndex, field.getValueAsBoolean());
					break;
				}
				case BYTE: {
					statement.setByte(parameterIndex, field.getValueAsByte());
					break;
				}
				case FLOAT: {
					statement.setFloat(parameterIndex, field.getValueAsFloat());
					break;
				}
				case SHORT: {
					statement.setShort(parameterIndex, field.getValueAsShort());
					break;
				}
				case CHAR: {
					statement.setString(parameterIndex, field.getValueAsString());
					break;
				}
				case DATE: {
					statement.setDate(parameterIndex, field.getValueAsDate());
					break;
				}
				case TIME: {
					statement.setTime(parameterIndex, field.getValueAsTime());
					break;
				}
				case BLOB: {
					statement.setBytes(parameterIndex, field.getValueAsBytes());
					break;
				}
				default: {
					statement.setString(parameterIndex, field.getValueAsString());
					break;
				}
				}
			}
		} catch (Throwable e) {
			final DataException exception = endpoint.exception(e);
			exception.set("JdbcWriter.parameterFieldName", field.getName());
			exception.set("JdbcWriter.parameterIndex", parameterIndex);
			exception.set("JdbcWriter.parameterJdbcType", jdbcType);
			exception.set("JdbcWriter.parameterValue", field);
			throw exception;
		}
	}

	public static int diffNumberTypes(final Class<?> type1, final Class<?> type2) {
		final int v1 = TypeUtil.NUMBER_TYPES_LIST.indexOf(type1);
		final int v2 = TypeUtil.NUMBER_TYPES_LIST.indexOf(type2);
		if (v1 < 0 || v2 < 0) {
			throw new DataException("types are not numbers").set("type1", type1).set("type2", type2);
		}
		return v1 - v2;
	}

	public static Pointer<Integer> diffTypes(Class<?> type1, Class<?> type2) {
		int direction = 1;
		if (type1 == type2) {
			return new Pointer<Integer>(0);
		}
		if (type2.isAssignableFrom(type1)) {
			final Class<?> temp = type2;
			type2 = type1;
			type1 = temp;
			direction = -1;
		} else if (!type1.isAssignableFrom(type2)) {
			return null;
		}
		final int diff = diffTypesImpl(type1, type2, 1);
		if (diff < 0) {
			return null;
		}
		return new Pointer<Integer>(diff * direction);
	}

	private static int diffTypesImpl(Class<?> type, Class<?> subtype, int depth) {
		final Class<?>[] arr$;
		final Class<?>[] interfaces = arr$ = subtype.getInterfaces();
		for (final Class<?> i : arr$) {
			if (i == type) {
				return depth;
			}
			final int diff = diffTypesImpl(type, i, depth + 1);
			if (diff >= 0) {
				return diff;
			}
		}
		final Class<?> superclass = subtype.getSuperclass();
		if (superclass == type) {
			return depth;
		}
		if (superclass == null) {
			return -1;
		}
		return diffTypesImpl(type, superclass, depth + 1);
	}

	static {
		(CLASS_TO_TYPE = new HashMap<Class<?>, FieldType>()).put(String.class, FieldType.STRING);
		TypeUtil.CLASS_TO_TYPE.put(Date.class, FieldType.DATETIME);
		TypeUtil.CLASS_TO_TYPE.put(Timestamp.class, FieldType.DATETIME);
		TypeUtil.CLASS_TO_TYPE.put(java.sql.Date.class, FieldType.DATE);
		TypeUtil.CLASS_TO_TYPE.put(Time.class, FieldType.TIME);
		TypeUtil.CLASS_TO_TYPE.put(Integer.class, FieldType.INT);
		TypeUtil.CLASS_TO_TYPE.put(Long.class, FieldType.LONG);
		TypeUtil.CLASS_TO_TYPE.put(Double.class, FieldType.DOUBLE);
		TypeUtil.CLASS_TO_TYPE.put(Boolean.class, FieldType.BOOLEAN);
		TypeUtil.CLASS_TO_TYPE.put(Byte.class, FieldType.BYTE);
		TypeUtil.CLASS_TO_TYPE.put(Float.class, FieldType.FLOAT);
		TypeUtil.CLASS_TO_TYPE.put(Short.class, FieldType.SHORT);
		TypeUtil.CLASS_TO_TYPE.put(Character.class, FieldType.CHAR);
		TypeUtil.CLASS_TO_TYPE.put(Integer.TYPE, FieldType.INT);
		TypeUtil.CLASS_TO_TYPE.put(Long.TYPE, FieldType.LONG);
		TypeUtil.CLASS_TO_TYPE.put(Double.TYPE, FieldType.DOUBLE);
		TypeUtil.CLASS_TO_TYPE.put(Boolean.TYPE, FieldType.BOOLEAN);
		TypeUtil.CLASS_TO_TYPE.put(Byte.TYPE, FieldType.BYTE);
		TypeUtil.CLASS_TO_TYPE.put(Float.TYPE, FieldType.FLOAT);
		TypeUtil.CLASS_TO_TYPE.put(Short.TYPE, FieldType.SHORT);
		TypeUtil.CLASS_TO_TYPE.put(Character.TYPE, FieldType.CHAR);
		TypeUtil.CLASS_TO_TYPE.put(BigDecimal.class, FieldType.DOUBLE);
		TypeUtil.CLASS_TO_TYPE.put(BigInteger.class, FieldType.LONG);
		TypeUtil.CLASS_TO_TYPE.put(byte[].class, FieldType.BLOB);
		JDBC_TYPES = new int[] { 12, 12, 4, -5, 3, 93, -7, 5, 7, 5, 1, 91, 92, 2004 };
		(PRIMITIVE_TO_OBJECT_TYPE = new HashMap<Class<?>, Class<?>>()).put(Boolean.TYPE, Boolean.class);
		TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.put(Character.TYPE, Character.class);
		TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.put(Byte.TYPE, Byte.class);
		TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.put(Short.TYPE, Short.class);
		TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.put(Integer.TYPE, Integer.class);
		TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.put(Long.TYPE, Long.class);
		TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.put(Float.TYPE, Float.class);
		TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.put(Double.TYPE, Double.class);
		TypeUtil.PRIMITIVE_TO_OBJECT_TYPE.put(Void.TYPE, Void.class);
		(PRIMITIVE_INTERNAL_NAME = new HashMap<String, String>(24)).put("boolean", "Z");
		TypeUtil.PRIMITIVE_INTERNAL_NAME.put("byte", "B");
		TypeUtil.PRIMITIVE_INTERNAL_NAME.put("char", "C");
		TypeUtil.PRIMITIVE_INTERNAL_NAME.put("double", "D");
		TypeUtil.PRIMITIVE_INTERNAL_NAME.put("float", "F");
		TypeUtil.PRIMITIVE_INTERNAL_NAME.put("int", "I");
		TypeUtil.PRIMITIVE_INTERNAL_NAME.put("long", "J");
		TypeUtil.PRIMITIVE_INTERNAL_NAME.put("short", "S");
		(PRIMITIVE_TYPE = new HashMap<String, Class<?>>(24)).put("double", Double.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put("float", Float.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put("long", Long.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put("short", Short.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put("int", Integer.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put("byte", Byte.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put("char", Character.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put("boolean", Boolean.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put(TypeUtil.PRIMITIVE_INTERNAL_NAME.get("double"), Double.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put(TypeUtil.PRIMITIVE_INTERNAL_NAME.get("float"), Float.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put(TypeUtil.PRIMITIVE_INTERNAL_NAME.get("long"), Long.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put(TypeUtil.PRIMITIVE_INTERNAL_NAME.get("short"), Short.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put(TypeUtil.PRIMITIVE_INTERNAL_NAME.get("int"), Integer.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put(TypeUtil.PRIMITIVE_INTERNAL_NAME.get("byte"), Byte.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put(TypeUtil.PRIMITIVE_INTERNAL_NAME.get("char"), Character.TYPE);
		TypeUtil.PRIMITIVE_TYPE.put(TypeUtil.PRIMITIVE_INTERNAL_NAME.get("boolean"), Boolean.TYPE);
		BIGDECIMAL_COMPARATOR = new BigDecimalComparator();
		BIGINTEGER_COMPARATOR = new BigIntegerComparator();
		BOOLEAN_COMPARATOR = new BooleanComparator();
		BYTE_COMPARATOR = new ByteComparator();
		CHARACTER_COMPARATOR = new CharacterComparator();
		DATE_COMPARATOR = new DateComparator();
		DOUBLE_COMPARATOR = new DoubleComparator();
		FLOAT_COMPARATOR = new FloatComparator();
		INTEGER_COMPARATOR = new IntegerComparator();
		LONG_COMPARATOR = new LongComparator();
		OBJECT_COMPARATOR = new CaseInsensitiveStringComparator();
		SHORT_COMPARATOR = new ShortComparator();
		SQL_DATE_COMPARATOR = new SQLDateComparator();
		SQL_TIME_COMPARATOR = new SQLTimeComparator();
		SQL_TIMESTAMP_COMPARATOR = new SQLTimestampComparator();
		STRING_COMPARATOR = TypeUtil.OBJECT_COMPARATOR;
		TIMESTAMP_COMPARATOR = TypeUtil.SQL_TIMESTAMP_COMPARATOR;
		(DEFAULT_COMPARATOR = new HashMap<Class<?>, BaseComparator<?>>()).put(BigDecimal.class, TypeUtil.BIGDECIMAL_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(BigInteger.class, TypeUtil.BIGINTEGER_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Boolean.class, TypeUtil.BOOLEAN_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Byte.class, TypeUtil.BYTE_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Character.class, TypeUtil.CHARACTER_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Date.class, TypeUtil.DATE_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Double.class, TypeUtil.DOUBLE_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Float.class, TypeUtil.FLOAT_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Integer.class, TypeUtil.INTEGER_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Long.class, TypeUtil.LONG_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Short.class, TypeUtil.SHORT_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(java.sql.Date.class, TypeUtil.SQL_DATE_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Time.class, TypeUtil.SQL_TIME_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Timestamp.class, TypeUtil.SQL_TIMESTAMP_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(String.class, TypeUtil.STRING_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Object.class, TypeUtil.STRING_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Boolean.TYPE, TypeUtil.BOOLEAN_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Byte.TYPE, TypeUtil.BYTE_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Character.TYPE, TypeUtil.CHARACTER_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Double.TYPE, TypeUtil.DOUBLE_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Float.TYPE, TypeUtil.FLOAT_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Integer.TYPE, TypeUtil.INTEGER_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Long.TYPE, TypeUtil.LONG_COMPARATOR);
		TypeUtil.DEFAULT_COMPARATOR.put(Short.TYPE, TypeUtil.SHORT_COMPARATOR);
		(METHOD_SUFFIX = new HashMap<String, String>(24)).put("double", "Double");
		TypeUtil.METHOD_SUFFIX.put("float", "Float");
		TypeUtil.METHOD_SUFFIX.put("long", "Long");
		TypeUtil.METHOD_SUFFIX.put("short", "Short");
		TypeUtil.METHOD_SUFFIX.put("int", "Int");
		TypeUtil.METHOD_SUFFIX.put("byte", "Byte");
		TypeUtil.METHOD_SUFFIX.put("char", "String");
		TypeUtil.METHOD_SUFFIX.put("boolean", "Boolean");
		TypeUtil.METHOD_SUFFIX.put("java.math.BigDecimal", "BigDecimal");
		TypeUtil.METHOD_SUFFIX.put("java.math.BigInteger", "BigInteger");
		TypeUtil.METHOD_SUFFIX.put("java.lang.String", "String");
		TypeUtil.METHOD_SUFFIX.put("java.sql.Date", "Date");
		TypeUtil.METHOD_SUFFIX.put("java.sql.Time", "Time");
		TypeUtil.METHOD_SUFFIX.put("java.sql.Timestamp", "Timestamp");
		TypeUtil.METHOD_SUFFIX.put("byte[]", "Bytes");
		TypeUtil.METHOD_SUFFIX.put("java.lang.Object", "Object");
		(NUMBER_TYPES_LIST = new ArrayList<Class<?>>()).add(Number.class);
		TypeUtil.NUMBER_TYPES_LIST.add(Byte.class);
		TypeUtil.NUMBER_TYPES_LIST.add(Short.class);
		TypeUtil.NUMBER_TYPES_LIST.add(Integer.class);
		TypeUtil.NUMBER_TYPES_LIST.add(Long.class);
		TypeUtil.NUMBER_TYPES_LIST.add(BigInteger.class);
		TypeUtil.NUMBER_TYPES_LIST.add(Float.class);
		TypeUtil.NUMBER_TYPES_LIST.add(Double.class);
		TypeUtil.NUMBER_TYPES_LIST.add(BigDecimal.class);
	}
}
