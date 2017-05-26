package com.northconcepts.datapipeline.filter.rule;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.filter.FieldFilterRule;
import com.northconcepts.datapipeline.internal.lang.Util;

public class PatternMatch extends FieldFilterRule
{
    private final ArrayList<Pattern> patterns;
    
    public PatternMatch() {
        super();
        this.patterns = new ArrayList<Pattern>();
    }
    
    public PatternMatch(final String... regex) {
        super();
        this.patterns = new ArrayList<Pattern>();
        this.add(regex);
    }
    
    public PatternMatch(final String regex, final int flags) {
        super();
        this.patterns = new ArrayList<Pattern>();
        this.add(regex, flags);
    }
    
    public PatternMatch add(final String... regex) {
        for (int i = 0; i < regex.length; ++i) {
            this.patterns.add(Pattern.compile(regex[i]));
        }
        return this;
    }
    
    public PatternMatch add(final String regex, final int flags) {
        this.patterns.add(Pattern.compile(regex, flags));
        return this;
    }
    
    public int getCount() {
        return this.patterns.size();
    }
    
    public Pattern get(final int index) {
        return this.patterns.get(index);
    }
    
    @Override
	public boolean allow(final Field field) {
        if (field.isNull()) {
            return false;
        }
        final String string = field.getValueAsString();
        for (int i = 0; i < this.getCount(); ++i) {
            final Pattern pattern = this.get(i);
            final Matcher matcher = pattern.matcher(string);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
	public String toString() {
        final String[] s = new String[this.patterns.size()];
        for (int i = 0; i < s.length; ++i) {
            s[i] = this.get(i).pattern();
        }
        return "value's pattern matches " + Util.arrayToString(s, " or ");
    }
}
