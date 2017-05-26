package com.northconcepts.datapipeline.internal.lang;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Interval implements Comparable<Interval>
{
    public static final long MILLISECONDS_IN_MILLISECOND = 1L;
    public static final long MILLISECONDS_IN_SECOND = 1000L;
    public static final long MILLISECONDS_IN_MINUTE = 60000L;
    public static final long MILLISECONDS_IN_HOUR = 3600000L;
    public static final long MILLISECONDS_IN_DAY = 86400000L;
    public static final long MILLISECONDS_IN_WEEK = 604800000L;
    public static final int YEARS = 0;
    public static final int MONTHS = 1;
    public static final int DAYS = 2;
    public static final int HOURS = 3;
    public static final int MINUTES = 4;
    public static final int SECONDS = 5;
    public static final int MILLISECONDS = 6;
    private static final String[] FIELD_NAMES;
    private final long millisecondPart;
    private final int monthPart;
    private final int[] fields;
    private Calendar calendar;
    private static final Map<String, Unit> units;
    
    public Interval(final long datetimeInMilliseconds) {
        super();
        this.fields = new int[7];
        final Moment m1 = new Moment(0L);
        final Moment m2 = new Moment(datetimeInMilliseconds);
        final Interval i = m2.subtract(m1);
        this.millisecondPart = i.millisecondPart;
        this.monthPart = i.monthPart;
        System.arraycopy(i.fields, 0, this.fields, 0, this.fields.length);
    }
    
    public Interval(final long millisecondPart, final int monthPart) {
        super();
        this.fields = new int[7];
        this.millisecondPart = millisecondPart;
        this.monthPart = monthPart;
        this.setupFields();
    }
    
    private void setupFields() {
        long millisecondPart = this.millisecondPart;
        final int monthPart = this.monthPart;
        this.fields[0] = monthPart / 12;
        this.fields[1] = monthPart % 12;
        this.fields[2] = (int)(millisecondPart / 86400000L);
        millisecondPart %= 86400000L;
        this.fields[3] = (int)(millisecondPart / 3600000L);
        millisecondPart %= 3600000L;
        this.fields[4] = (int)(millisecondPart / 60000L);
        millisecondPart %= 60000L;
        this.fields[5] = (int)(millisecondPart / 1000L);
        millisecondPart %= 1000L;
        this.fields[6] = (int)millisecondPart;
    }
    
    public long getMillisecondPart() {
        return this.millisecondPart;
    }
    
    public int getMonthPart() {
        return this.monthPart;
    }
    
    public int getField(final int field) {
        return this.fields[field];
    }
    
    public int getFieldCount() {
        return this.fields.length;
    }
    
    public Interval getNegative() {
        return new Interval(this.millisecondPart * -1L, this.monthPart * -1);
    }
    
    public Interval add(final Interval interval) {
        return new Interval(this.millisecondPart + interval.getMillisecondPart(), this.monthPart + interval.getMonthPart());
    }
    
    public Interval subtract(final Interval interval) {
        return this.add(interval.getNegative());
    }
    
    public boolean isGreaterThan(final Interval interval) {
        return this.monthPart > interval.getMonthPart() || (this.monthPart == interval.getMonthPart() && this.millisecondPart > interval.getMillisecondPart());
    }
    
    public boolean isGreaterThanOrEqualTo(final Interval interval) {
        return this.monthPart > interval.getMonthPart() || (this.monthPart == interval.getMonthPart() && this.millisecondPart >= interval.getMillisecondPart());
    }
    
    public boolean isLessThan(final Interval interval) {
        return this.monthPart < interval.getMonthPart() || (this.monthPart == interval.getMonthPart() && this.millisecondPart < interval.getMillisecondPart());
    }
    
    public boolean isLessThanOrEqualTo(final Interval interval) {
        return this.monthPart < interval.getMonthPart() || (this.monthPart == interval.getMonthPart() && this.millisecondPart <= interval.getMillisecondPart());
    }
    
    public int compareTo(final Interval o) {
        if (this.isGreaterThan(o)) {
            return 1;
        }
        if (this.isLessThan(o)) {
            return -1;
        }
        return 0;
    }
    
    @Override
	public boolean equals(final Object o) {
        if (o instanceof Interval) {
            final Interval interval = (Interval)o;
            return this.millisecondPart == interval.getMillisecondPart() && this.monthPart == interval.getMonthPart();
        }
        return false;
    }
    
    @Override
	public int hashCode() {
        return (int)(this.millisecondPart ^ this.millisecondPart >>> 32) ^ this.monthPart;
    }
    
    private Calendar getCalendar0() {
        if (this.calendar == null) {
            Moment m = new Moment(0L);
            m = m.add(this);
            this.calendar = m.getCalendar0();
        }
        return this.calendar;
    }
    
    public Calendar getCalendar() {
        return (Calendar)this.getCalendar0().clone();
    }
    
    public Date getDate() {
        return this.getCalendar0().getTime();
    }
    
    @Override
	public String toString() {
        return this.toFormattedString();
    }
    
    public String toDetailedString() {
        return "[milliseconds=" + this.millisecondPart + ", months=" + this.monthPart + "]\n  " + "[" + "YEARS=" + this.getField(0) + "; MONTHS=" + this.getField(1) + "; DAYS=" + this.getField(2) + "; HOURS=" + this.getField(3) + "; MINUTES=" + this.getField(4) + "; SECONDS=" + this.getField(5) + "; MILLISECONDS=" + this.getField(6) + "]";
    }
    
    private String getFieldAsString(final int field) {
        final int value = this.getField(field);
        if (value == 1) {
            return value + " " + Interval.FIELD_NAMES[field];
        }
        if (value != 0) {
            return value + " " + Interval.FIELD_NAMES[field] + "s";
        }
        return "";
    }
    
    private void getFieldAsString(final int field, final List<String> list) {
        final String s = this.getFieldAsString(field);
        if (s.length() > 0) {
            list.add(s);
        }
    }
    
    public String toFormattedString() {
        final List<String> list = new ArrayList<String>();
        this.getFieldAsString(0, list);
        this.getFieldAsString(1, list);
        this.getFieldAsString(2, list);
        this.getFieldAsString(3, list);
        this.getFieldAsString(4, list);
        this.getFieldAsString(5, list);
        this.getFieldAsString(6, list);
        final StringBuilder s = new StringBuilder(15 * list.size());
        if (list.size() > 0) {
            s.append(list.get(0));
            for (int i = 1; i < list.size() - 1; ++i) {
                s.append(", ");
                s.append(list.get(i));
            }
            if (list.size() > 1) {
                s.append(" and ");
                s.append(list.get(list.size() - 1));
            }
        }
        return s.toString();
    }
    
    private static Unit getUnit(String unitName) {
        if (unitName == null) {
            throw new NullPointerException("unitName is null");
        }
        unitName = unitName.trim().toUpperCase();
        final Unit unit = Interval.units.get(unitName);
        if (unit == null) {
            throw new IllegalArgumentException("unrecognized unitName \"" + unitName + "\"");
        }
        return unit;
    }
    
    public static Interval parseInterval(final long count, final String unitName) {
        final Unit unit = getUnit(unitName);
        final long value = count * unit.multiplier;
        if (unit.month) {
            return new Interval(0L, (int)value);
        }
        return new Interval(value, 0);
    }
    
    public static Interval parseInterval(final String intervalStatement) {
        if (intervalStatement == null) {
            throw new NullPointerException("intervalStatement is null");
        }
        final int space = intervalStatement.indexOf(32);
        if (space < 1 || space + 1 == intervalStatement.length()) {
            throw new IllegalArgumentException("unrecognized intervalStatement \"" + intervalStatement + "\"");
        }
        final long count = Long.parseLong(intervalStatement.substring(0, space).trim());
        final String unitName = intervalStatement.substring(space + 1).trim();
        return parseInterval(count, unitName);
    }
    
    static {
        FIELD_NAMES = new String[] { "Year", "Month", "Day", "Hour", "Minute", "Second", "Millisecond" };
        (units = new HashMap<String, Unit>()).put("MILLISECOND", new Unit(false, 1L));
        Interval.units.put("SECOND", new Unit(false, 1000L));
        Interval.units.put("MINUTE", new Unit(false, 60000L));
        Interval.units.put("HOUR", new Unit(false, 3600000L));
        Interval.units.put("DAY", new Unit(false, 86400000L));
        Interval.units.put("WEEK", new Unit(false, 604800000L));
        Interval.units.put("MONTH", new Unit(true, 1L));
        Interval.units.put("YEAR", new Unit(true, 12L));
        Interval.units.put("MILLISECONDS", new Unit(false, 1L));
        Interval.units.put("SECONDS", new Unit(false, 1000L));
        Interval.units.put("MINUTES", new Unit(false, 60000L));
        Interval.units.put("HOURS", new Unit(false, 3600000L));
        Interval.units.put("DAYS", new Unit(false, 86400000L));
        Interval.units.put("WEEKS", new Unit(false, 604800000L));
        Interval.units.put("MONTHS", new Unit(true, 1L));
        Interval.units.put("YEARS", new Unit(true, 12L));
    }
    
    private static class Unit
    {
        public final boolean month;
        public final long multiplier;
        
        public Unit(final boolean month, final long multiplier) {
            super();
            this.month = month;
            this.multiplier = multiplier;
        }
    }
}
