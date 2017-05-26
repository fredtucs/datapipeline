package com.northconcepts.datapipeline.internal.lang.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import com.northconcepts.datapipeline.internal.lang.TypeUtil;
import com.northconcepts.datapipeline.internal.lang.Util;

public class ObjectReflector
{
    private static final Class<?> OBJECT_CLASS;
    private static final int[] EMPTY_INTEGER_ARRAY;
    private final Object excludedPrefixesLock;
    private Set<String> excludedPrefixes;
    private boolean excludeConstants;
    public static final Set<String> PRIMITIVE_TYPES;
    
    public ObjectReflector() {
        super();
        this.excludedPrefixesLock = new Object();
    }
    
    public boolean getExcludeConstants() {
        return this.excludeConstants;
    }
    
    public void setExcludeConstants(final boolean excludeConstants) {
        this.excludeConstants = excludeConstants;
    }
    
    public Set<String> getExcludedPrefixes() {
        return this.excludedPrefixes;
    }
    
    public void setExcludedPrefixes(final Set<String> excludedLoggingPrefixes) {
        synchronized (this.excludedPrefixesLock) {
            this.excludedPrefixes = excludedLoggingPrefixes;
        }
    }
    
    public void addExcludedPrefixes(final String prefix) {
        synchronized (this.excludedPrefixesLock) {
            if (this.excludedPrefixes == null) {
                this.excludedPrefixes = new HashSet<String>();
            }
            this.excludedPrefixes.add(prefix);
        }
    }
    
    public ReflectionHandler inspect(final Object object, final ReflectionHandler handler) throws Throwable {
        try {
            handler.startReflection(object);
            final Set<String> knownObjects = new HashSet<String>(4096);
            if (object == null) {
                handler.enterField(null, "this", null, 0, object, ObjectReflector.EMPTY_INTEGER_ARRAY, false);
                handler.leaveField(null, "this", null, 0, object, ObjectReflector.EMPTY_INTEGER_ARRAY, false);
                return handler;
            }
            final Class<?> objectType = object.getClass();
            this.inspect(null, "this", objectType, 0, object, ObjectReflector.EMPTY_INTEGER_ARRAY, knownObjects, handler);
            return handler;
        }
        finally {
            handler.endReflection(object);
        }
    }
    
    private void inspect(final Object objectParent, final String objectName, final Class<?> objectType, final int objectDepth, final Object object, final int[] objectArrayIndex, final Set<String> knownObjects, final ReflectionHandler handler) throws Throwable {
        if (objectName.startsWith("class$")) {
            return;
        }
        final boolean knownObject = this.isKnown(object, objectName, objectType, knownObjects);
        try {
            handler.enterField(objectParent, objectName, objectType, objectDepth, object, objectArrayIndex, knownObject);
            if (knownObject || object == null || objectType == ObjectReflector.OBJECT_CLASS || isPrimitive(objectType)) {
                return;
            }
            if (objectType.isArray()) {
                final Class<?> componentType = objectType.getComponentType();
                final int arrayLength = Array.getLength(object);
                if (arrayLength > 0) {
                    final int[] index = Util.resizeArray(objectArrayIndex, 1);
                    for (int i = 0; i < arrayLength; ++i) {
                        final Object arrayElement = Array.get(object, i);
                        Class<?> elementType = componentType;
                        if (arrayElement != null && !isPrimitive(componentType)) {
                            elementType = arrayElement.getClass();
                        }
                        index[index.length - 1] = i;
                        handler.enterArrayElement(objectParent, objectName, elementType, objectDepth, arrayElement, index, arrayLength, object);
                        this.inspect(objectParent, objectName, elementType, objectDepth, arrayElement, index, knownObjects, handler);
                        handler.leaveArrayElement(objectParent, objectName, elementType, objectDepth, arrayElement, index, arrayLength, object);
                    }
                }
            }
            else if (!isPrimitive(objectType)) {
                handler.enterClass(objectParent, objectName, objectType, objectDepth, object, objectArrayIndex);
                final Field[] fields = objectType.getDeclaredFields();
                for (int j = 0; j < fields.length; ++j) {
                    final Field field = fields[j];
                    if (!this.excludeConstants || !Modifier.isFinal(field.getModifiers()) || !Modifier.isStatic(field.getModifiers())) {
                        Class<?> fieldType = field.getType();
                        final String fieldName = field.getName();
                        field.setAccessible(true);
                        final Object fieldObject = field.get(object);
                        if (fieldObject != null && !isPrimitive(fieldType)) {
                            fieldType = fieldObject.getClass();
                        }
                        this.inspect(object, fieldName, fieldType, objectDepth + 1, fieldObject, ObjectReflector.EMPTY_INTEGER_ARRAY, knownObjects, handler);
                    }
                }
                if (!isPrimitive(objectType)) {
                    final Class<?> superClass = objectType.getSuperclass();
                    if (superClass != null && superClass != ObjectReflector.OBJECT_CLASS) {
                        this.inspect(objectParent, "super", superClass, objectDepth + 1, object, ObjectReflector.EMPTY_INTEGER_ARRAY, knownObjects, handler);
                    }
                }
                handler.leaveClass(objectParent, objectName, objectType, objectDepth, object, objectArrayIndex);
            }
        }
        finally {
            handler.leaveField(objectParent, objectName, objectType, objectDepth, object, objectArrayIndex, knownObject);
        }
    }
    
    public static final boolean isPrimitive(final Class<?> clazz) throws Throwable {
        return clazz.isPrimitive() || ObjectReflector.PRIMITIVE_TYPES.contains(clazz.getName());
    }
    
    private final boolean isKnown(final Object object, final String objectName, final Class<?> objectType, final Set<String> knownObjects) throws Throwable {
        if (object == null || objectType == ObjectReflector.OBJECT_CLASS || isPrimitive(objectType)) {
            return false;
        }
        final String objectTypeName = TypeUtil.getJavaName(objectType);
        if (this.excludedPrefixes != null) {
            for (final String prefix : this.excludedPrefixes) {
                if (objectTypeName.startsWith(prefix)) {
                    return true;
                }
            }
        }
        final String objectId = objectTypeName + "@" + System.identityHashCode(object);
        final boolean known = knownObjects.contains(objectId);
        if (!known) {
            knownObjects.add(objectId);
        }
        return known;
    }
    
    static {
        OBJECT_CLASS = Object.class;
        EMPTY_INTEGER_ARRAY = new int[0];
        (PRIMITIVE_TYPES = new HashSet<String>(32)).add("java.io.File");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Boolean");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Byte");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Character");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Class");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.ClassLoader");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Double");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Float");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Integer");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Long");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Package");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Short");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.StackTraceElement");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.String");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.StringBuffer");
        ObjectReflector.PRIMITIVE_TYPES.add("java.lang.Void");
        ObjectReflector.PRIMITIVE_TYPES.add("java.math.BigDecimal");
        ObjectReflector.PRIMITIVE_TYPES.add("java.math.BigInteger");
        ObjectReflector.PRIMITIVE_TYPES.add("java.net.Inet4Address");
        ObjectReflector.PRIMITIVE_TYPES.add("java.net.Inet6Address");
        ObjectReflector.PRIMITIVE_TYPES.add("java.net.InetAddress");
        ObjectReflector.PRIMITIVE_TYPES.add("java.net.InetSocketAddress");
        ObjectReflector.PRIMITIVE_TYPES.add("java.net.SocketAddress");
        ObjectReflector.PRIMITIVE_TYPES.add("java.net.URI");
        ObjectReflector.PRIMITIVE_TYPES.add("java.net.URL");
        ObjectReflector.PRIMITIVE_TYPES.add("java.nio.CharBuffer");
        ObjectReflector.PRIMITIVE_TYPES.add("java.sql.Date");
        ObjectReflector.PRIMITIVE_TYPES.add("java.sql.Time");
        ObjectReflector.PRIMITIVE_TYPES.add("java.sql.Timestamp");
        ObjectReflector.PRIMITIVE_TYPES.add("java.util.Calendar");
        ObjectReflector.PRIMITIVE_TYPES.add("java.util.Currency");
        ObjectReflector.PRIMITIVE_TYPES.add("java.util.Date");
        ObjectReflector.PRIMITIVE_TYPES.add("java.util.GregorianCalendar");
        ObjectReflector.PRIMITIVE_TYPES.add("java.util.Locale");
        ObjectReflector.PRIMITIVE_TYPES.add("java.util.SimpleTimeZone");
    }
}
