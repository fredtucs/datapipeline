package com.northconcepts.datapipeline.filter.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.filter.FieldFilterRule;
import com.northconcepts.datapipeline.internal.lang.Util;

public class ValueMatch<T> extends FieldFilterRule {
	private final ArrayList<T> values;

	public ValueMatch() {
		super();
		this.values = new ArrayList<T>();
	}

	public ValueMatch(final T... array) {
		super();
		this.values = new ArrayList<T>();
		this.add(array);
	}

	@SuppressWarnings("unchecked")
	public ValueMatch(final Collection<?> collection) {
		super();
		this.values = new ArrayList<T>();
		final Iterator<?> i = collection.iterator();
		while (i.hasNext()) {
			this.add((T)i.next());
		}
	}

	public int getCount() {
		return this.values.size();
	}

	public Object get(final int index) {
		return this.values.get(index);
	}

	@Override
	public boolean allow(final Field field) {
		final Object value = field.getValue();
		for (int i = 0; i < this.getCount(); ++i) {
			if (Util.equals(value, this.get(i))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "value matches [" + Util.collectionToString(this.values, "] or [") + "]";
	}

	public ValueMatch<T> add(final T... values) {
		for (int i = 0; i < values.length; ++i) {
			this.values.add(values[i]);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public ValueMatch<T> add(final DataReader source, final String fieldName) {
		source.open();
		try {
			Record record;
			while ((record = source.read()) != null) {
				this.add((T)record.getField(fieldName).getValue());
			}
		} finally {
			source.close();
		}
		return this;
	}
}
