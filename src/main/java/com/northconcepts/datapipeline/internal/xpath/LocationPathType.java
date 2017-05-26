package com.northconcepts.datapipeline.internal.xpath;

public enum LocationPathType
{
    RELATIVE(Axis.SELF, true), 
    ABSOLUTE(Axis.SELF, false), 
    ABSOLUTE_DESCENDANTS(Axis.DESCENDANT_OR_SELF, false);
    
    private final Axis axis;
    private final boolean relative;
    
    private LocationPathType(final Axis axis, final boolean relative) {
        this.axis = axis;
        this.relative = relative;
    }
    
    public Axis getAxis() {
        return this.axis;
    }
    
    public boolean isRelative() {
        return this.relative;
    }
}
