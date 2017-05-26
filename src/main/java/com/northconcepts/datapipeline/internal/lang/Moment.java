package com.northconcepts.datapipeline.internal.lang;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Moment implements Comparable<Moment>
{
    private final long datetimeInMilliseconds;
    private Calendar calendar;
    private String dateString;
    private boolean hasDatePart;
    private boolean hasTimePart;
    private static final int[] CALENDAR_FIELDS;
    private static final int[] CALENDAR_FIELDS_MAX;
    private static final int[] DAYS_IN_MONTH;
    private static final SimpleDateFormat dateTimeFormat;
    private static final SimpleDateFormat dateTimeFormatWithSecond;
    private static final SimpleDateFormat dateTimeFormatWithMillisecond;
    private static final SimpleDateFormat dateFormat;
    private static final SimpleDateFormat timeFormat;
    private static final SimpleDateFormat timeFormatWithSecond;
    private static final SimpleDateFormat timeFormatWithMillisecond;
    
    private Moment(final Calendar calendar) {
        super();
        this.hasDatePart = true;
        this.hasTimePart = true;
        this.calendar = calendar;
        this.datetimeInMilliseconds = calendar.getTimeInMillis();
    }
    
    public Moment(final Date date) {
        super();
        this.hasDatePart = true;
        this.hasTimePart = true;
        this.datetimeInMilliseconds = date.getTime();
        if (date instanceof java.sql.Date) {
            this.hasTimePart = false;
        }
        else if (date instanceof Time) {
            this.hasDatePart = false;
        }
    }
    
    public Moment(final long datetimeInMilliseconds) {
        super();
        this.hasDatePart = true;
        this.hasTimePart = true;
        this.datetimeInMilliseconds = datetimeInMilliseconds;
    }
    
    public Moment(final int year, final int month, final int day, final int hour, final int minute, final int second) {
        this(year, month - 1, day, hour, minute, second, 0);
    }
    
    public Moment(int year, int month, int day, final int hour, final int minute, final int second, final int milliseconds) {
        super();
        this.hasDatePart = true;
        this.hasTimePart = true;
        final Calendar calendar = Calendar.getInstance();
        if (year == 0 && month == -1 && day == 0) {
            year = 1970;
            month = 0;
            day = 1;
        }
        calendar.set(year, month, day, hour, minute, second);
        calendar.set(14, milliseconds);
        this.calendar = calendar;
        this.datetimeInMilliseconds = calendar.getTimeInMillis();
    }
    
    public boolean hasDatePart() {
        return this.hasDatePart;
    }
    
    public boolean hasTimePart() {
        return this.hasTimePart;
    }
    
    public Moment add(final Interval interval) {
        Moment moment;
        if (interval.getMonthPart() == 0) {
            moment = new Moment(this.datetimeInMilliseconds + interval.getMillisecondPart());
        }
        else {
            final Calendar calendar = this.getCalendar();
            calendar.setTimeInMillis(calendar.getTimeInMillis() + interval.getMillisecondPart());
            calendar.add(2, interval.getMonthPart());
            moment = new Moment(calendar);
        }
        moment.hasDatePart = this.hasDatePart;
        moment.hasTimePart = this.hasTimePart;
        return moment;
    }
    
    public Moment subtract(final Interval interval) {
        final Moment moment = this.add(interval.getNegative());
        moment.hasDatePart = this.hasDatePart;
        moment.hasTimePart = this.hasTimePart;
        return moment;
    }
    
    public Interval subtract(final Moment moment) {
        return getDifference(this.getCalendar0(), moment.getCalendar0());
    }
    
    public boolean isGreaterThan(final Moment moment) {
        return this.datetimeInMilliseconds > moment.datetimeInMilliseconds;
    }
    
    public boolean isGreaterThanOrEqualTo(final Moment moment) {
        return this.datetimeInMilliseconds >= moment.datetimeInMilliseconds;
    }
    
    public boolean isLessThan(final Moment moment) {
        return this.datetimeInMilliseconds < moment.datetimeInMilliseconds;
    }
    
    public boolean isLessThanOrEqualTo(final Moment moment) {
        return this.datetimeInMilliseconds <= moment.datetimeInMilliseconds;
    }
    
    public int compareTo(final Moment o) {
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
        if (o instanceof Moment) {
            final Moment moment = (Moment)o;
            return this.datetimeInMilliseconds == moment.datetimeInMilliseconds;
        }
        return false;
    }
    
    @Override
	public int hashCode() {
        return (int)(this.datetimeInMilliseconds ^ this.datetimeInMilliseconds >>> 32);
    }
    
    Calendar getCalendar0() {
        if (this.calendar == null) {
            (this.calendar = Calendar.getInstance()).setTimeInMillis(this.datetimeInMilliseconds);
        }
        return this.calendar;
    }
    
    public Calendar getCalendar() {
        return (Calendar)this.getCalendar0().clone();
    }
    
    public Date getDate() {
        return this.getCalendar0().getTime();
    }
    
    public Date getDateOrTime() {
        Date date;
        if (!this.hasDatePart) {
            date = new Time(this.datetimeInMilliseconds);
        }
        else if (!this.hasTimePart) {
            date = new java.sql.Date(this.datetimeInMilliseconds);
        }
        else {
            date = this.getCalendar0().getTime();
        }
        return date;
    }
    
    public long getDatetimeInMilliseconds() {
        return this.datetimeInMilliseconds;
    }
    
    public String toDateString() {
        return Moment.dateFormat.format(this.getDate());
    }
    
    public String toTimeString() {
        if (this.datetimeInMilliseconds % 60000L == 0L) {
            return Moment.timeFormat.format(this.getDate());
        }
        if (this.datetimeInMilliseconds % 1000L == 0L) {
            return Moment.timeFormatWithSecond.format(this.getDate());
        }
        return Moment.timeFormatWithMillisecond.format(this.getDate());
    }
    
    public String toDateTimeString() {
        if (this.datetimeInMilliseconds % 60000L == 0L) {
            return Moment.dateTimeFormat.format(this.getDate());
        }
        if (this.datetimeInMilliseconds % 1000L == 0L) {
            return Moment.dateTimeFormatWithSecond.format(this.getDate());
        }
        return Moment.dateTimeFormatWithMillisecond.format(this.getDate());
    }
    
    public String toFormattedString() {
        if (!this.hasDatePart()) {
            return this.toTimeString();
        }
        if (!this.hasTimePart()) {
            return this.toDateString();
        }
        return this.toDateTimeString();
    }
    
    @Override
	public String toString() {
        if (this.dateString == null) {
            this.dateString = this.getDate().toString();
        }
        return this.dateString;
    }
    
    private static int getActualMaximum(final int fieldIndex, final Calendar calendar) {
        int max = Moment.CALENDAR_FIELDS_MAX[fieldIndex];
        if (max == -1) {
            max = Moment.DAYS_IN_MONTH[calendar.get(2)];
            if (max == -1) {
                return calendar.getActualMaximum(5);
            }
        }
        return max;
    }
    
    private static Interval getDifference(Calendar calendar1, Calendar calendar2) {
        if (calendar1.before(calendar2)) {
            final Calendar temp = calendar1;
            calendar1 = calendar2;
            calendar2 = temp;
        }
        final int[] result = new int[Moment.CALENDAR_FIELDS.length];
        for (int i = 0; i < result.length; ++i) {
            final int field = Moment.CALENDAR_FIELDS[i];
            final int value1 = calendar1.get(field);
            final int value2 = calendar2.get(field);
            if (value2 > value1) {
                result[i + 1] = -1;
                final int[] array = result;
                final int n = i;
                array[n] += getActualMaximum(i, calendar1);
            }
            final int[] array2 = result;
            final int n2 = i;
            array2[n2] += value1 - value2;
        }
        final long milliseconds = result[0] + result[1] * 1000L + result[2] * 60000L + result[3] * 3600000L + result[4] * 86400000L;
        final int months = result[5] + result[6] * 12;
        return new Interval(milliseconds, months);
    }
    
    public static Moment parseMoment(String momentStatement) {
        if (momentStatement == null) {
            throw new NullPointerException("momentStatement is null");
        }
        try {
            momentStatement = momentStatement.trim();
            final int countOfSemicolons = Util.getCharacterCount(momentStatement, ':');
            final int countOfPeriods = Util.getCharacterCount(momentStatement, '.');
            Date date;
            if (momentStatement.indexOf(32) >= 0) {
                if (countOfPeriods > 0) {
                    date = Moment.dateTimeFormatWithMillisecond.parse(momentStatement);
                }
                else if (countOfSemicolons > 1) {
                    date = Moment.dateTimeFormatWithSecond.parse(momentStatement);
                }
                else {
                    date = Moment.dateTimeFormat.parse(momentStatement);
                }
            }
            else if (countOfPeriods > 0) {
                date = Moment.timeFormatWithMillisecond.parse(momentStatement);
            }
            else if (countOfSemicolons > 1) {
                date = Moment.timeFormatWithSecond.parse(momentStatement);
            }
            else if (countOfSemicolons == 1) {
                date = Moment.timeFormat.parse(momentStatement);
            }
            else {
                date = Moment.dateFormat.parse(momentStatement);
            }
            return new Moment(date.getTime());
        }
        catch (ParseException e) {
            throw new RuntimeException("expected yyyy-MM-dd HH:mm, yyyy-MM-dd, or HH:mm, but found \"" + momentStatement + "\"", e);
        }
    }
    
    static {
        CALENDAR_FIELDS = new int[] { 14, 13, 12, 11, 5, 2, 1 };
        CALENDAR_FIELDS_MAX = new int[] { 1000, 60, 60, 24, -1, 12, Integer.MAX_VALUE };
        DAYS_IN_MONTH = new int[] { 31, -1, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTimeFormatWithSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTimeFormatWithMillisecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        timeFormat = new SimpleDateFormat("HH:mm");
        timeFormatWithSecond = new SimpleDateFormat("HH:mm:ss");
        timeFormatWithMillisecond = new SimpleDateFormat("HH:mm:ss.SSS");
    }
}
