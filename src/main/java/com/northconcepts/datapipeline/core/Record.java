package com.northconcepts.datapipeline.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.northconcepts.datapipeline.internal.lang.Util;

public final class Record implements Cloneable, Comparable<Record>, Session {
	private final ArrayList<Field> fields;
	private State state;
	private SessionImpl session;

	public Record(final Record record) {
		super();
		this.state = State.ALIVE;
		this.fields = new ArrayList<Field>(record.getFieldCount());
		for (int i = 0; i < record.getFieldCount(); ++i) {
			this.addField().copyFrom(record.getField0(i));
		}
		this.state = State.NEW;
	}

	public Record() {
		super();
		this.state = State.ALIVE;
		this.fields = new ArrayList<Field>();
	}

	public Record(final String... fieldNames) {
		super();
		this.state = State.ALIVE;
		this.fields = new ArrayList<Field>(fieldNames.length);
		for (int i = 0; i < fieldNames.length; ++i) {
			this.addField().setName(fieldNames[i]);
		}
	}

	public Record(final FieldList fieldNames) {
		super();
		this.state = State.ALIVE;
		this.fields = new ArrayList<Field>(fieldNames.size());
		for (int i = 0; i < fieldNames.size(); ++i) {
			this.addField().setName(fieldNames.get(i));
		}
	}

	public void copyFrom(final Record record, final boolean overwriteFields) {
		for (int i = 0; i < record.getFieldCount(); ++i) {
			final Field sourceField = record.getField0(i);
			String name = sourceField.getName();
			if (overwriteFields) {
				this.getField(name, true).copyFrom(sourceField);
			} else {
				for (int j = 2; this.containsField(name); name = sourceField.getName() + j, ++j) {
				}
				this.addField().copyFrom(sourceField).setName(name);
			}
		}
	}

	public void copyFrom(final Record record) {
		this.copyFrom(record, false);
	}

	@Override
	public Object clone() {
		return new Record(this);
	}

	public State getState() {
		return this.state;
	}

	public boolean isDeleted() {
		return this.state == State.DELETED;
	}

	void setModified() {
		if (this.state == State.MODIFIED || this.state == State.NEW) {
			return;
		}
		if (this.state == State.ALIVE) {
			this.state = State.MODIFIED;
		} else if (this.state == State.DELETED) {
			throw new DataException("cannot modify record, it has already been marked as deleted").setRecord(this);
		}
	}

	public void setAlive() {
		this.state = State.ALIVE;
	}

	public void delete() {
		this.state = State.DELETED;
	}

	private Field getField0(final int index) {
		return this.fields.get(index);
	}

	private Field getField0(final String fieldName, final boolean throwExceptionOnFailure) {
		return this.getField0(this.indexOfField(fieldName, throwExceptionOnFailure));
	}

	public Field getField(final int index) {
		return this.getField0(index);
	}

	public Field getField(final int index, final boolean createField) {
		if (createField) {
			while (index >= this.fields.size()) {
				this.addField();
			}
		}
		return this.getField0(index);
	}

	public Field getField(final String fieldName) {
		return this.getField0(fieldName, true);
	}

	public Field getField(final String fieldName, final boolean createField) {
		if (!createField) {
			return this.getField0(this.indexOfField(fieldName, true));
		}
		final int index = this.indexOfField(fieldName, false);
		if (index < 0) {
			return this.addField().setName(fieldName);
		}
		return this.getField0(index);
	}

	public Record setField(final String fieldName, final Object value) {
		this.getField(fieldName, true).setValue(value);
		return this;
	}

	public Record setFieldNull(final String fieldName, final FieldType type) {
		this.getField(fieldName, true).setNull(type);
		return this;
	}

	public int getFieldCount() {
		return this.fields.size();
	}

	public Field addField() {
		final Field f = new Field(this);
		this.fields.add(f);
		return f;
	}

	ArrayList<Field> getFields(final FieldList fieldList) {
		if (fieldList == null) {
			return this.fields;
		}
		final int length = fieldList.size();
		final ArrayList<Field> list = new ArrayList<Field>(length);
		for (int i = 0; i < length; ++i) {
			list.add(this.getField0(fieldList.get(i), true));
		}
		return list;
	}

	ArrayList<Field> getFields(final int... indexes) {
		if (indexes == null) {
			return this.fields;
		}
		final int length = indexes.length;
		final ArrayList<Field> fields = new ArrayList<Field>(length);
		for (int i = 0; i < length; ++i) {
			fields.add(this.getField0(indexes[i]));
		}
		return fields;
	}

	int[] indexesOfFields(final FieldList fieldList) {
		if (fieldList == null) {
			final int[] indexes = new int[this.fields.size()];
			for (int i = 0; i < indexes.length; ++i) {
				indexes[i] = i;
			}
			return indexes;
		}
		final int[] indexes = new int[fieldList.size()];
		for (int i = 0; i < indexes.length; ++i) {
			indexes[i] = this.indexOfField(fieldList.get(i), true);
		}
		return indexes;
	}

	public List<String> getFieldNames() {
		final int length = this.getFieldCount();
		final ArrayList<String> array = new ArrayList<String>(length);
		for (int i = 0; i < length; ++i) {
			array.add(this.getField(i).getName());
		}
		return array;
	}

	public FieldList getFieldNameList() {
		return new FieldList().add(this.getFieldNames());
	}

	public List<FieldType> getFieldTypes(final FieldList fieldList) {
		final ArrayList<Field> fields = this.getFields(fieldList);
		final int length = fields.size();
		final ArrayList<FieldType> array = new ArrayList<FieldType>(length);
		for (int i = 0; i < length; ++i) {
			array.add(fields.get(i).getType());
		}
		return array;
	}

	public List<FieldType> getFieldTypes() {
		return this.getFieldTypes(null);
	}

	public List<?> getValues(final FieldList fieldList) {
		final ArrayList<Field> fields = this.getFields(fieldList);
		final int length = fields.size();
		final ArrayList<Object> array = new ArrayList<Object>(length);
		for (int i = 0; i < length; ++i) {
			array.add(fields.get(i).getValue());
		}
		return array;
	}

	public List<?> getValues() {
		return this.getValues(null);
	}

	public int compareTo(final Record o) {
		return this.compareTo(o, null);
	}

	public int compareTo(final Record o, RecordComparator comparator) {
		if (comparator == null) {
			comparator = RecordComparator.DEFAULT;
		}
		return comparator.compare(this, o);
	}

	@Override
	public boolean equals(final Object o) {
		return this.equals((Record) o, null);
	}

	public boolean equals(final Record o, final RecordComparator comparator) {
		return this.compareTo(o, comparator) == 0;
	}

	@Override
	public int hashCode() {
		return this.hashCode(null);
	}

	public int hashCode(final FieldList fieldList) {
		final ArrayList<Field> fields = this.getFields(fieldList);
		int hashCode = 1;
		for (int i = 0; i < fields.size(); ++i) {
			hashCode = 31 * hashCode + fields.get(i).hashCode();
		}
		return hashCode;
	}

	protected int indexOfField(final Field field) {
		for (int length = this.fields.size(), i = 0; i < length; ++i) {
			if (this.fields.get(i) == field) {
				return i;
			}
		}
		return -1;
	}

	public int indexOfField(final String fieldName, final boolean throwExceptionOnFailure) {
		for (int length = this.fields.size(), i = 0; i < length; ++i) {
			if (Util.namesMatch(this.getField(i).getName(), fieldName)) {
				return i;
			}
		}
		if (throwExceptionOnFailure) {
			throw new DataException("unknown field [" + fieldName + "]").setFieldName(fieldName).setRecord(this);
		}
		return -1;
	}

	public boolean containsField(final String fieldName) {
		return this.indexOfField(fieldName, false) >= 0;
	}

	public Field removeField(final int index) {
		final Field removed = this.fields.remove(index);
		this.setModified();
		return removed;
	}

	public Field removeField(final String columnName) {
		return this.removeField(this.indexOfField(columnName, true));
	}

	public void moveField(final int oldIndex, int newIndex) {
		final Field field = this.fields.get(oldIndex);
		this.fields.remove(oldIndex);
		if (newIndex > oldIndex) {
			--newIndex;
		}
		this.fields.add(newIndex, field);
		this.setModified();
	}

	public void moveField(final String columnName, final int newIndex) {
		this.moveField(this.indexOfField(columnName, true), newIndex);
	}

	public Record selectFields(final FieldList fields) {
		return this.selectFields(fields, false);
	}

	public Record selectFields(final FieldList fields, final boolean lenient) {
		for (int i = 0; i < fields.size(); ++i) {
			final String fieldName = fields.get(i);
			final int index = this.indexOfField(fieldName, !lenient);
			if (index >= 0) {
				this.moveField(index, i);
			}
		}
		for (int i = this.getFieldCount() - 1; i >= fields.size(); --i) {
			this.removeField(i);
		}
		return this;
	}

	public Record excludeFields(final FieldList fields) {
		for (int i = this.getFieldCount() - 1; i >= 0; --i) {
			if (fields.contains(this.getField(i).getName())) {
				this.removeField(i);
			}
		}
		return this;
	}

	public Record excludeFields(final Set<String> fields) {
		for (int i = this.getFieldCount() - 1; i >= 0; --i) {
			if (fields.contains(this.getField(i).getName())) {
				this.removeField(i);
			}
		}
		return this;
	}

	public long getSizeInBytesOfFieldNames() {
		long size = 4L;
		for (int fieldCount = this.getFieldCount(), i = 0; i < fieldCount; ++i) {
			size += this.getField0(i).getSizeOfNameInBytes();
		}
		return size;
	}

	public long getSizeInBytes() {
		long size = 4L;
		for (int fieldCount = this.getFieldCount(), i = 0; i < fieldCount; ++i) {
			size += this.getField0(i).getSizeInBytes();
		}
		return size;
	}

	@Override
	public String toString() {
		final StringBuilder s = new StringBuilder(2048);
		s.append("Record");
		if (this.state != State.ALIVE) {
			s.append(" (").append(this.getState()).append(")");
		}
		s.append(" {").append(Util.LINE_SEPARATOR);
		for (int i = 0; i < this.getFieldCount(); ++i) {
			final Field field = this.getField0(i);
			String valueString = field.isNull() ? "null" : field.getValueAsString();
			if (valueString.length() > 128) {
				valueString = valueString.substring(0, 128) + "..." + valueString.length();
			}
			s.append("    ").append(i).append(":[").append(field.getName()).append("]:").append(field.getType()).append("=[").append(valueString)
					.append("]");
			if (field.isNotNull()) {
				s.append(':').append(Util.getTypeName(field.getValue()));
			}
			s.append(Util.LINE_SEPARATOR);
		}
		s.append("}").append(Util.LINE_SEPARATOR);
		return s.toString();
	}

	public boolean containsSessionProperty(final String name) {
		return this.session != null && this.session.containsSessionProperty(name);
	}

	public void copySessionPropertiesFrom(final Session sourceSession) {
		if (sourceSession == null) {
			return;
		}
		final Record sourceRecord = (Record) sourceSession;
		if (this.session == null) {
			this.session = new SessionImpl();
		}
		this.session.copySessionPropertiesFrom(sourceRecord.session);
	}

	public Object getSessionProperty(final String name) {
		if (this.session == null) {
			return null;
		}
		return this.session.getSessionProperty(name);
	}

	public void setSessionProperty(final String name, final Object value) {
		if (this.session == null) {
			this.session = new SessionImpl();
		}
		this.session.setSessionProperty(name, value);
	}

	public enum State {
		ALIVE, NEW, MODIFIED, DELETED;
	}
}
