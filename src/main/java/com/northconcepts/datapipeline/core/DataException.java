package com.northconcepts.datapipeline.core;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.northconcepts.datapipeline.internal.lang.Util;

public class DataException extends RuntimeException {
	private static final int MAX_PROPERTY_LENGTH = 256;
	private static final long serialVersionUID = 1L;
	private static final String PROPERTY_RECORD = "record";
	private static final String PROPERTY_FIELD_NAME = "fieldName";
	private String messagePrefix;

	public static DataException wrap(String message, Throwable exception) {
		if ((exception instanceof DataException)) {
			return ((DataException) exception).prefixMessageWith(message);
		}
		return new DataException(exception.getMessage(), exception).prefixMessageWith(message);
	}

	public static DataException wrap(Throwable exception) {
		if ((exception instanceof DataException)) {
			return (DataException) exception;
		}
		return new DataException(exception.getMessage(), exception);
	}

	private final TreeMap<String, Object> properties = new TreeMap<String, Object>();;

	public DataException() {
	}

	public DataException(String message) {
		super(message);
	}

	public DataException(Throwable cause) {
		super(cause.getMessage(), cause);
		copyProperties(cause);
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
		copyProperties(cause);
	}

	private void copyProperties(Throwable cause) {
		if ((cause instanceof DataException)) {
			this.properties.putAll(((DataException) cause).getProperties());
		}
	}

	public Object get(String name) {
		return this.properties.get(name);
	}

	public String getAsString(String name) {
		Object o = this.properties.get(name);
		return o == null ? null : o.toString();
	}

	public int getAsInteger(String name) {
		return ((Number) this.properties.get(name)).intValue();
	}

	public long getAsLong(String name) {
		return ((Number) this.properties.get(name)).longValue();
	}

	public double getAsDouble(String name) {
		return ((Number) this.properties.get(name)).doubleValue();
	}

	public boolean getAsBoolean(String name) {
		return ((Boolean) this.properties.get(name)).booleanValue();
	}

	public Date getAsDatetime(String name) {
		return (Date) this.properties.get(name);
	}

	public DataException set(String name, Map<?, ?> value) {
		if (value == null) {
			this.properties.put(name, null);
		} else {
			this.properties.put(name + ".length", Integer.valueOf(value.size()));
			if (value.size() == 0) {
				this.properties.put(name, "{}");
			} else {
				for (Object key : value.keySet()) {
					String keyString = key == null ? "null" : key.toString();
					set(name + "." + keyString, value.get(key));
				}
			}
		}
		return this;
	}

	public DataException set(String name, Collection<?> value) {
		if (value == null) {
			this.properties.put(name, null);
		} else {
			this.properties.put(name + ".length", Integer.valueOf(value.size()));
			if (value.size() == 0) {
				this.properties.put(name, "{}");
			} else {
				Iterator<?> iterator = value.iterator();
				for (int i = 0; iterator.hasNext(); i++) {
					set(name + "[" + i + "]", iterator.next());
				}
			}
		}
		return this;
	}

	public DataException set(String name, Object value) {
		if (value == null) {
			this.properties.put(name, null);
		} else if (value.getClass().isArray()) {
			int length = Array.getLength(value);
			this.properties.put(name + ".length", Integer.valueOf(length));
			if (length == 0) {
				this.properties.put(name, "{}");
			} else {
				for (int i = 0; i < length; i++) {
					set(name + "[" + i + "]", Array.get(value, i));
				}
			}
		} else {
			String string = value.toString();
			if (string.length() > 256) {
				string = string.substring(0, 256) + "...";
			}
			this.properties.put(name, string);
		}
		return this;
	}

	public DataException set(String name, char value) {
		return set(name, Util.toPrintableString(value));
	}

	public DataException set(String name, int value) {
		return set(name, new Integer(value));
	}

	public DataException set(String name, long value) {
		return set(name, new Long(value));
	}

	public DataException set(String name, double value) {
		return set(name, new Double(value));
	}

	public DataException set(String name, boolean value) {
		return set(name, new Boolean(value));
	}

	public String getFieldName() {
		return getAsString("fieldName");
	}

	public DataException setFieldName(String fieldName) {
		return set("fieldName", fieldName);
	}

	public Record getRecord() {
		return (Record) get("record");
	}

	public DataException setRecord(Record record) {
		if (record == null) {
			return this;
		}
		return set("record", record);
	}

	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public String getPropertiesAsString() {
		String propertyPart = null;

		Iterator<String> iterator = this.properties.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (propertyPart == null) {
				propertyPart = "";
			} else {
				propertyPart = propertyPart + "\n";
			}
			propertyPart = propertyPart + key + "=[" + this.properties.get(key) + "]";
		}
		return propertyPart;
	}

	public DataException prefixMessageWith(String message) {
		if (this.messagePrefix != null) {
			this.messagePrefix = (message + "; " + this.messagePrefix);
		} else {
			this.messagePrefix = message;
		}
		return this;
	}

	private String getOriginalMessageWithPrefix() {
		if (this.messagePrefix != null) {
			return this.messagePrefix + "; " + super.getMessage();
		}
		return super.getMessage();
	}

	@Override
	public String getMessage() {
		return getOriginalMessageWithPrefix();
	}

	public String getMessageWithProperties() {
		if (this.properties.size() > 0) {
			return getOriginalMessageWithPrefix() + "\n-------------------------------\n" + getPropertiesAsString();
		}
		return getOriginalMessageWithPrefix();
	}

	@Override
	public void printStackTrace(PrintStream s) {
		synchronized (s) {
			printStackTrace(new PrintWriter(s));
		}
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		synchronized (s) {
			s.println(getClass().getName() + ":  " + getMessageWithProperties());
			s.println("-------------------------------");
			StackTraceElement[] trace = getStackTrace();
			for (int i = 0; i < trace.length; i++) {
				s.println("\tat " + trace[i]);
			}
			Throwable ourCause = getCause();
			if (ourCause != null) {
				ourCause.printStackTrace(s);
			}
			s.flush();
		}
	}
}
