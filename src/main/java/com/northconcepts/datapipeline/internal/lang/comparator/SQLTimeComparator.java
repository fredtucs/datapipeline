package com.northconcepts.datapipeline.internal.lang.comparator;

import java.sql.Time;
import java.util.Date;

public final class SQLTimeComparator extends BaseComparator<Time> {
	public static final SQLTimeComparator INSTANCE;

	public int compare(final Time o1, final Time o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		return o1.compareTo(o2);
	}

	public static boolean matches(final Time o1, final Time o2) {
		return SQLTimeComparator.INSTANCE.compare(o1, o2) == 0;
	}

	public static boolean before(final Time o1, final Time o2) {
		return SQLTimeComparator.INSTANCE.compare(o1, o2) == -1;
	}

	public static boolean after(final Time o1, final Time o2) {
		return SQLTimeComparator.INSTANCE.compare(o1, o2) == 1;
	}

	static {
		INSTANCE = new SQLTimeComparator();
	}
}
