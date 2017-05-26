package com.northconcepts.datapipeline.fixedwidth;

class FixedWidthField
{
    private String name;
    private int width;
    private Character fillChar;
    private boolean skip;
    private FixedWidthAlign align;
    
    public FixedWidthField() {
        super();
        this.align = FixedWidthAlign.LEFT;
    }
    
    public String getName() {
        return this.name;
    }
    
    public FixedWidthField setName(final String name) {
        this.name = name;
        return this;
    }
    
    public boolean isSkip() {
        return this.skip;
    }
    
    public FixedWidthField setSkip(final boolean skip) {
        this.skip = skip;
        return this;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public FixedWidthField setWidth(final int width) {
        this.width = width;
        return this;
    }
    
    public FixedWidthField setFillChar(final Character fillChar) {
        this.fillChar = fillChar;
        return this;
    }
    
    public Character getFillChar() {
        return this.fillChar;
    }
    
    public FixedWidthAlign getAlign() {
        return this.align;
    }
    
    public FixedWidthField setAlign(final FixedWidthAlign align) {
        this.align = align;
        return this;
    }
    
    @Override
	public String toString() {
        return "name=" + this.name + ", width=" + this.width + ", skip=" + this.skip;
    }
}
