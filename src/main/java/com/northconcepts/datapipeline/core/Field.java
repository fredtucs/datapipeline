package com.northconcepts.datapipeline.core;

import java.sql.Time;
import java.text.Collator;

import com.northconcepts.datapipeline.internal.lang.Moment;
import com.northconcepts.datapipeline.internal.lang.TypeUtil;
import com.northconcepts.datapipeline.internal.lang.Util;

public final class Field
  implements Comparable<Field>, Session
{
  public static final int MAX_DISPLAY_STRING = 128;
  public static final int JAVA_OBJECT_SIZE = 0;
  public static final int JAVA_ARRAY_SIZE = 0;
  private String name;
  private FieldType type = FieldType.STRING;
  private Object value;
  private final Record record;
  private Session.SessionImpl session;
  
  protected Field(Record record)
  {
    this.record = record;
  }
  
  public Field copyFrom(Field field)
  {
    this.name = field.name;
    this.type = field.type;
    this.value = field.value;
    return this;
  }
  
  public Field copyFrom(Field field, boolean copyName)
  {
    if (copyName) {
      this.name = field.name;
    }
    this.type = field.type;
    this.value = field.value;
    return this;
  }
  
  @Override
public String toString()
  {
    String valueString = this.value == null ? "null" : this.value.toString();
    if (valueString.length() > 128) {
      valueString = valueString.substring(0, 128) + "..." + valueString.length();
    }
    String s = "[" + this.name + "]:" + getType() + "=[" + valueString + "]";
    if (this.value != null) {
      s = s + ':' + Util.getTypeName(this.value);
    }
    return s;
  }
  
  public int getIndex()
  {
    return this.record.indexOfField(this);
  }
  
  public String getName()
  {
    return getName(true);
  }
  
  public String getName(boolean nullToDefaultName)
  {
    if ((this.name != null) || (!nullToDefaultName)) {
      return this.name;
    }
    int index = getIndex();
    String columnName = Util.intToColumnName(index);
    String defaultName = columnName;
    for (int j = 2; isDuplicateFieldName(defaultName); j++) {
      defaultName = columnName + j;
    }
    return defaultName;
  }
  
  private boolean isDuplicateFieldName(String fieldName)
  {
    int size = this.record.getFieldCount();
    for (int i = 0; i < size; i++)
    {
      String name = this.record.getField(i).getName(false);
      if (Util.namesMatch(fieldName, name)) {
        return true;
      }
    }
    return false;
  }
  
  public Field setName(String name)
  {
    this.name = name;
    return this;
  }
  
  public FieldType getType()
  {
    return this.type;
  }
  
  public boolean isNull()
  {
    return this.value == null;
  }
  
  public boolean isNotNull()
  {
    return this.value != null;
  }
  
  private Field setValue(FieldType type, Object value)
  {
    this.record.setModified();
    this.type = type;
    this.value = value;
    return this;
  }
  
  public Field setValue(Object value)
  {
    FieldType type = FieldType.UNDEFINED;
    if (value != null)
    {
      if ((value instanceof Moment))
      {
        Moment m = (Moment)value;
        value = m.getDateOrTime();
      }
      Class<?> clazz = value.getClass();
      FieldType t = TypeUtil.getFieldTypeForClass(clazz);
      if (t != null) {
        type = t;
      }
    }
    return setValue(type, value);
  }
  
  public Field setNull(FieldType type)
  {
    return setValue(type, null);
  }
  
  public int compareTo(Field o)
  {
    return compareTo(o, null);
  }
  
  public int compareTo(Field o, Collator collator)
  {
    if (o == null) {
      return 1;
    }
    if (this == o) {
      return 0;
    }
    Field field = o;
    return Util.compare(this.value, field.value, collator);
  }
  
  @Override
public boolean equals(Object o)
  {
    return equals(o, null);
  }
  
  public boolean equals(Object o, Collator collator)
  {
    return compareTo((Field)o, collator) == 0;
  }
  
  @Override
public int hashCode()
  {
    return this.value == null ? 0 : this.value.hashCode();
  }
  
  public long getSizeOfNameInBytes()
  {
    int size = 8;
    if (getName() != null) {
      size += getName().length() * 2;
    }
    return size;
  }
  
  public long getSizeInBytes()
  {
    long size = 4L;
    if (this.value == null) {
      return size;
    }
    size += this.type.getSizeInBytes(this.value);
    
    return size;
  }
  
  public Record getRecord()
  {
    return this.record;
  }
  
  public Object getValue()
  {
    return this.value;
  }
  
  public String getValueAsString()
  {
    Object value = getValue();
    return value == null ? null : value.toString();
  }
  
  public java.util.Date getValueAsDatetime()
  {
    return (java.util.Date)this.value;
  }
  
  public java.sql.Date getValueAsDate()
  {
    if (this.value == null) {
      return null;
    }
    if (getType() == FieldType.DATETIME)
    {
      java.util.Date d = (java.util.Date)this.value;
      return new java.sql.Date(d.getTime());
    }
    return (java.sql.Date)this.value;
  }
  
  public Time getValueAsTime()
  {
    if (this.value == null) {
      return null;
    }
    if (getType() == FieldType.DATETIME)
    {
      java.util.Date d = (java.util.Date)this.value;
      return new Time(d.getTime());
    }
    return (Time)this.value;
  }
  
  public int getValueAsInteger()
  {
    return ((Number)this.value).intValue();
  }
  
  public long getValueAsLong()
  {
    return ((Number)this.value).longValue();
  }
  
  public short getValueAsShort()
  {
    return ((Number)this.value).shortValue();
  }
  
  public byte getValueAsByte()
  {
    return ((Number)this.value).byteValue();
  }
  
  public boolean getValueAsBoolean()
  {
    return ((Boolean)this.value).booleanValue();
  }
  
  public char getValueAsChar()
  {
    return ((Character)this.value).charValue();
  }
  
  public double getValueAsDouble()
  {
    return ((Number)this.value).doubleValue();
  }
  
  public float getValueAsFloat()
  {
    return ((Number)this.value).floatValue();
  }
  
  public byte[] getValueAsBytes()
  {
    return (byte[])this.value;
  }
  
  public Field setValue(String value)
  {
    return setValue(FieldType.STRING, value);
  }
  
  public Field setValue(java.util.Date value)
  {
    return setValue(FieldType.DATETIME, value);
  }
  
  public Field setValue(java.sql.Date value)
  {
    return setValue(FieldType.DATE, value);
  }
  
  public Field setValue(Time value)
  {
    return setValue(FieldType.TIME, value);
  }
  
  public Field setValue(int value)
  {
    return setValue(FieldType.INT, new Integer(value));
  }
  
  public Field setValue(long value)
  {
    return setValue(FieldType.LONG, new Long(value));
  }
  
  public Field setValue(short value)
  {
    return setValue(FieldType.SHORT, new Short(value));
  }
  
  public Field setValue(byte value)
  {
    return setValue(FieldType.BYTE, new Byte(value));
  }
  
  public Field setValue(boolean value)
  {
    return setValue(FieldType.BOOLEAN, new Boolean(value));
  }
  
  public Field setValue(char value)
  {
    return setValue(FieldType.CHAR, new Character(value));
  }
  
  public Field setValue(double value)
  {
    return setValue(FieldType.DOUBLE, new Double(value));
  }
  
  public Field setValue(float value)
  {
    return setValue(FieldType.FLOAT, new Float(value));
  }
  
  public Field setValue(byte[] value)
  {
    return setValue(FieldType.BLOB, value);
  }
  
  public boolean containsSessionProperty(String name)
  {
    if (this.session == null) {
      return false;
    }
    return this.session.containsSessionProperty(name);
  }
  
  public void copySessionPropertiesFrom(Session sourceSession)
  {
    if (sourceSession == null) {
      return;
    }
    Field sourceField = (Field)sourceSession;
    if (this.session == null) {
      this.session = new Session.SessionImpl();
    }
    this.session.copySessionPropertiesFrom(sourceField.session);
  }
  
  public Object getSessionProperty(String name)
  {
    if (this.session == null) {
      return null;
    }
    return this.session.getSessionProperty(name);
  }
  
  public void setSessionProperty(String name, Object value)
  {
    if (this.session == null) {
      this.session = new Session.SessionImpl();
    }
    this.session.setSessionProperty(name, value);
  }
}
