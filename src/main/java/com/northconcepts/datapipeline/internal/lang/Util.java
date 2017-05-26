package com.northconcepts.datapipeline.internal.lang;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.northconcepts.datapipeline.core.DataEndpoint;
import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.internal.lang.reflect.ObjectReflector;
import com.northconcepts.datapipeline.internal.lang.reflect.StringReflection;

public final class Util
{
    public static final Logger log;
    public static final SimpleDateFormat yyyyMMddHHmmssSSS;
    public static final String RANDOM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String LINE_SEPARATOR;
    
    public static void closeIO(final Closeable o) throws IOException {
        if (o instanceof Flushable) {
            final Flushable f = (Flushable)o;
            f.flush();
        }
        o.close();
    }
    
    public static void close(final DataEndpoint endpoint) throws DataException {
        if (endpoint != null && endpoint.isOpen()) {
            endpoint.close();
        }
    }
    
    public static void open(final DataEndpoint endpoint) throws DataException {
        if (endpoint != null && !endpoint.isOpen()) {
            endpoint.open();
        }
    }
    
    public static String getRandomString(final int length) {
        final StringBuilder s = new StringBuilder(length);
        final int randomCharsLength = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length();
        for (int i = 0; i < length; ++i) {
            s.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt((int)(randomCharsLength * Math.random())));
        }
        return s.toString();
    }
    
    public static void writeRandomString(final int length, Writer out, final int lineLengths, final boolean closeStream) throws IOException {
        try {
            out = new BufferedWriter(out, 65536);
            final int randomCharsLength = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length();
            for (int i = 0; i < length; ++i) {
                out.write("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt((int)(randomCharsLength * Math.random())));
                if (i > 0 && i % lineLengths == 0) {
                    out.append("\n");
                }
            }
        }
        finally {
            if (closeStream) {
                out.close();
            }
        }
    }
    
    public static Reader getRandomString(final int length, final int lineLengths) throws IOException {
        return new Reader() {
            private int remainingLength = length;
            
            @Override
			public int read(final char[] cbuf, final int off, int len) throws IOException {
                if (this.remainingLength < 1) {
                    return -1;
                }
                if (len > this.remainingLength) {
                    len = this.remainingLength;
                }
                final int randomCharsLength = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length();
                for (int i = 0; i < len; ++i) {
                    cbuf[off + i] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt((int)(randomCharsLength * Math.random()));
                    if (i > 0 && i % lineLengths == 0) {
                        cbuf[off + len - 1] = '\n';
                    }
                }
                this.remainingLength -= len;
                return len;
            }
            
            @Override
			public void close() throws IOException {
            }
        };
    }
    
    public static String intToColumnName(int column) {
        final StringBuilder s = new StringBuilder();
        ++column;
        while (column > 26) {
            final int c = column % 26;
            column /= 26;
            if (c == 0) {
                s.insert(0, 'Z');
                --column;
            }
            else {
                s.insert(0, (char)(c + 65 - 1));
            }
        }
        s.insert(0, (char)(column + 65 - 1));
        return s.toString();
    }
    
    public static int columnNameToInt(final String columnName) {
        final int length = columnName.length();
        int column = 0;
        for (int i = 0; i < length; ++i) {
            final char c = Character.toUpperCase(columnName.charAt(i));
            if (i < length - 1) {
                column += (int)(Math.pow(26.0, length - i - 1) * (c - 'A' + '\u0001'));
            }
            else {
                column += c - 'A';
            }
        }
        return column;
    }
    
    public static boolean namesMatch(final String name1, final String name2) {
        return name1 == name2 || (name1 != null && name2 != null && name1.equalsIgnoreCase(name2));
    }
    
    public static final String toPrintableString(final int c) {
        switch (c) {
            case -1: {
                return "end-of-file";
            }
            case 10: {
                return "end-of-line (\\n)";
            }
            case 13: {
                return "end-of-line (\\r)";
            }
            case 9: {
                return "tab (\\t)";
            }
            case 32: {
                return "space";
            }
            case 0: {
                return "null char (0)";
            }
            default: {
                return String.valueOf((char)c);
            }
        }
    }
    
    public static boolean isEndOfLineOrFile(final int c) {
        return c == -1 || c == 13 || c == 10;
    }
    
    public static boolean isWhitespace(final int c) {
        return c == 32 || c == 9;
    }
    
    public static boolean isWhitespace(final int c, final int exception) {
        return c != exception && (c == 32 || c == 9);
    }
    
    public static String getTypeName(final Object value) {
        if (value == null) {
            return "";
        }
        String type = value.getClass().getName();
        final int index = type.lastIndexOf(46);
        type = type.substring(index + 1);
        return type;
    }
    
    public static boolean isEmpty(final String s) {
        return s == null || s.trim().length() == 0;
    }
    
    public static boolean isNotEmpty(final String s) {
        return !isEmpty(s);
    }
    
    public static String getJavaIdentifier(final String s) {
        if (isEmpty(s)) {
            return null;
        }
        final StringBuilder id = new StringBuilder(s.length());
        char c = s.charAt(0);
        if (Character.isJavaIdentifierStart(c)) {
            id.append(c);
        }
        for (int i = 1; i < s.length(); ++i) {
            c = s.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                id.append(c);
            }
        }
        if (id.length() == 0) {
            return null;
        }
        return id.toString();
    }
    
    public static String getJavaIdentifierPart(final String s) {
        if (isEmpty(s)) {
            return null;
        }
        final StringBuilder id = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                id.append(c);
            }
        }
        if (id.length() == 0) {
            return null;
        }
        return id.toString();
    }
    
    public static int compare(final Object o1, final Object o2) {
        return compare(o1, o2, null);
    }
    
    public static <T> int compare(final T o1, final T o2, final Collator collator) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (collator != null) {
            return collator.compare(o1, o2);
        }
        return ((Comparable)o1).compareTo(o2);
    }
    
    public static boolean equals(final Object o1, final Object o2) {
        return equals(o1, o2, null);
    }
    
    public static boolean equals(final Object o1, final Object o2, final Collator collator) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (collator != null) {
            return collator.compare(o1, o2) == 0;
        }
        return o1.equals(o2);
    }
    
    public static int compare(final String s1, final String s2, final boolean caseSensitive) {
        if (s1 == s2) {
            return 0;
        }
        if (s1 == null) {
            return -1;
        }
        if (s2 == null) {
            return 1;
        }
        return caseSensitive ? s1.compareTo(s2) : s1.compareToIgnoreCase(s2);
    }
    
    public static boolean matches(final String s1, final String s2, final boolean caseSensitive, final boolean allowEmpty) {
        return (allowEmpty || (!isEmpty(s1) && !isEmpty(s2))) && compare(s1, s2, caseSensitive) == 0;
    }
    
    public static boolean matches(final String s1, final String s2, final boolean caseSensitive) {
        return compare(s1, s2, caseSensitive) == 0;
    }
    
    public static boolean matches(final String s1, final String s2) {
        return matches(s1, s2, true);
    }
    
    public static String arrayToString(final Object[] array, final String separator) {
        if (array == null || array.length == 0) {
            return "";
        }
        final StringBuilder s = new StringBuilder(32 * array.length);
        s.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            s.append(separator).append(array[i]);
        }
        return s.toString();
    }
    
    public static String arrayToString(final List<?> array, final String separator) {
        if (array == null || array.size() == 0) {
            return "";
        }
        final StringBuilder s = new StringBuilder(32 * array.size());
        s.append(array.get(0));
        for (int i = 1; i < array.size(); ++i) {
            s.append(separator).append(array.get(i));
        }
        return s.toString();
    }
    
    public static String arrayToString(final int[] array, final String separator) {
        if (array == null || array.length == 0) {
            return "";
        }
        final StringBuilder s = new StringBuilder(32 * array.length);
        s.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            s.append(separator).append(array[i]);
        }
        return s.toString();
    }
    
    public static String arrayToString(final double[] array, final String separator) {
        if (array == null || array.length == 0) {
            return "";
        }
        final StringBuilder s = new StringBuilder(32 * array.length);
        s.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            s.append(separator).append(array[i]);
        }
        return s.toString();
    }
    
    public static String collectionToString(final Collection<?> collection, final String separator) {
        if (collection == null || collection.size() == 0) {
            return "";
        }
        final StringBuilder s = new StringBuilder(32 * collection.size());
        final Iterator<?> i = collection.iterator();
        s.append(i.next());
        while (i.hasNext()) {
            s.append(separator).append(i.next());
        }
        return s.toString();
    }
    
    public static String collectionToString(final Collection<?> collection, final String elementFormat, final String separator) {
        if (collection == null || collection.size() == 0) {
            return "";
        }
        final StringBuilder s = new StringBuilder((32 + elementFormat.length() + separator.length()) * collection.size());
        final Iterator<?> i = collection.iterator();
        s.append(String.format(elementFormat, i.next()));
        while (i.hasNext()) {
            s.append(separator).append(String.format(elementFormat, i.next()));
        }
        return s.toString();
    }
    
    public static int getCharacterCount(final String string, final char searchChar) {
        int index = 0;
        int count = 0;
        while ((index = string.indexOf(searchChar, index)) >= 0) {
            ++count;
            ++index;
        }
        return count;
    }
    
    public static <K, V> K getKeyForValue(final V value, final Map<K, V> map) {
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static String padLeft(String string, int length, final char padChar) {
        if (string == null) {
            string = "";
        }
        if (length < 0) {
            length = 0;
        }
        if (string.length() >= length) {
            return string;
        }
        final int count = length - string.length();
        final StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < count; ++i) {
            buffer.append(padChar);
        }
        buffer.append(string);
        return buffer.toString();
    }
    
    public static String padRight(String string, int length, final char padChar) {
        if (string == null) {
            string = "";
        }
        if (length < 0) {
            length = 0;
        }
        if (string.length() >= length) {
            return string;
        }
        final int count = length - string.length();
        final StringBuilder buffer = new StringBuilder(length);
        buffer.append(string);
        for (int i = 0; i < count; ++i) {
            buffer.append(padChar);
        }
        return buffer.toString();
    }
    
    public static String trimLeft(final String string) {
        if (isEmpty(string)) {
            return string;
        }
        int length;
        int i;
        for (length = string.length(), i = 0; i < length && string.charAt(i) <= ' '; ++i) {}
        return (i > 0) ? string.substring(i) : string;
    }
    
    public static String trimRight(final String string) {
        if (isEmpty(string)) {
            return string;
        }
        int length;
        for (length = string.length(); length > 0 && string.charAt(length - 1) <= ' '; --length) {}
        return (length < string.length()) ? string.substring(0, length) : string;
    }
    
    public static String trimLeft(final String string, final char padChar) {
        if (isEmpty(string)) {
            return string;
        }
        int length;
        int i;
        for (length = string.length(), i = 0; i < length && string.charAt(i) == padChar; ++i) {}
        return (i > 0) ? string.substring(i) : string;
    }
    
    public static String trimRight(final String string, final char padChar) {
        if (isEmpty(string)) {
            return string;
        }
        int length;
        for (length = string.length(); length > 0 && string.charAt(length - 1) == padChar; --length) {}
        return (length < string.length()) ? string.substring(0, length) : string;
    }
    
    public static String repeat(final String string, int count) {
        if (count < 0) {
            count = 0;
        }
        final StringBuilder s = new StringBuilder(string.length() * count);
        for (int i = 0; i < count; ++i) {
            s.append(string);
        }
        return s.toString();
    }
    
    public static Object[] resizeArray(final Object[] array, final int difference) {
        if (difference == 0) {
            return array;
        }
        final int newSize = array.length + difference;
        final Object[] newArray = (Object[])Array.newInstance(array.getClass().getComponentType(), newSize);
        if (difference > 0) {
            System.arraycopy(array, 0, newArray, 0, array.length);
        }
        else {
            System.arraycopy(array, 0, newArray, 0, newSize);
        }
        return newArray;
    }
    
    public static int[] resizeArray(final int[] array, final int difference) {
        if (difference == 0) {
            return array;
        }
        final int newSize = array.length + difference;
        final int[] newArray = (int[])Array.newInstance(array.getClass().getComponentType(), newSize);
        if (difference > 0) {
            System.arraycopy(array, 0, newArray, 0, array.length);
        }
        else {
            System.arraycopy(array, 0, newArray, 0, newSize);
        }
        return newArray;
    }
    
    public static void debugReflection(final Object o, final Set<String> excludedPrefixes, final boolean excludeConstants, final boolean showErrors) {
        final ObjectReflector objectReflector = new ObjectReflector();
        objectReflector.setExcludeConstants(excludeConstants);
        objectReflector.setExcludedPrefixes(excludedPrefixes);
        final StringReflection s = new StringReflection();
        try {
            Util.log.debug(objectReflector.inspect(o, s));
        }
        catch (Throwable e) {
            if (showErrors) {
                Util.log.debug(e);
                Util.log.debug(s.getBuffer());
            }
        }
    }
    
    public static void debugReflection(final Object o, final boolean excludeConstants, final boolean showErrors) {
        debugReflection(o, null, excludeConstants, showErrors);
    }
    
    public static void debugReflection(final Object o) {
        debugReflection(o, true, false);
    }
    
    public static String left(final String string, final int length) {
        if (string == null || string.length() <= length) {
            return string;
        }
        return string.substring(0, length);
    }
    
    public static String right(final String string, final int length) {
        if (string == null || string.length() <= length) {
            return string;
        }
        return string.substring(string.length() - length, string.length());
    }
    
    static {
        log = DataEndpoint.log;
        yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Util.LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    }
}
