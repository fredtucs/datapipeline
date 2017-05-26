package com.northconcepts.datapipeline.internal.xpath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.northconcepts.datapipeline.core.DataException;

public class LocationPath
{
    private String sourceString;
    private LocationPathType type;
    private final List<Step> steps;
    
    public LocationPath() {
        super();
        this.type = LocationPathType.RELATIVE;
        this.steps = new StepArrayList();
    }
    
    @Override
	public String toString() {
        String s = "LocationPath[source=" + this.sourceString + ";  AST[" + this.type + ";  steps=" + this.steps.size() + "\n";
        for (int i = 0; i < this.steps.size(); ++i) {
            s = s + "    step[" + i + "]=[" + this.steps.get(i) + "]\n";
        }
        s += "]]";
        return s;
    }
    
    private void onStepAdded(final Step step) {
        step.locationPath = this;
    }
    
    public String getSourceString() {
        return this.sourceString;
    }
    
    public void setSourceString(final String sourceString) {
        this.sourceString = sourceString;
    }
    
    public void setType(LocationPathType type) {
        if (type == null) {
            type = LocationPathType.RELATIVE;
        }
        this.type = type;
    }
    
    public LocationPathType getType() {
        return this.type;
    }
    
    public boolean isRelative() {
        return this.type.isRelative();
    }
    
    public List<Step> getSteps() {
        return this.steps;
    }
    
    public void addStep(final Step step) {
        this.steps.add(step);
    }
    
    public PrincipalNodeType getPrincipalNodeType() {
        if (this.steps.size() > 0) {
            return this.steps.get(this.steps.size() - 1).getAxis().getPrincipalNodeType();
        }
        return null;
    }
    
    public boolean matchesElement(final XmlNode node) {
        final int size = this.steps.size();
        if (size == 0) {
            throw new DataException("no steps to match");
        }
        return this.steps.get(size - 1).matches(node);
    }
    
    public Object getValue(final XmlNode node) {
        final int size = this.steps.size();
        if (size > 0) {
            return this.steps.get(size - 1).getValue(node);
        }
        throw new DataException("no steps in this location path");
    }
    
    private final class StepArrayList extends ArrayList<Step>
    {
        @Override
		public void add(final int index, final Step element) {
            LocationPath.this.onStepAdded(element);
            super.add(index, element);
        }
        
        @Override
		public boolean add(final Step e) {
            LocationPath.this.onStepAdded(e);
            return super.add(e);
        }
        
        @Override
		public boolean addAll(final Collection<? extends Step> c) {
            for (final Step step : c) {
                LocationPath.this.onStepAdded(step);
            }
            return super.addAll(c);
        }
        
        @Override
		public boolean addAll(final int index, final Collection<? extends Step> c) {
            for (final Step step : c) {
                LocationPath.this.onStepAdded(step);
            }
            return super.addAll(index, c);
        }
        
        @Override
		public Step set(final int index, final Step element) {
            LocationPath.this.onStepAdded(element);
            return super.set(index, element);
        }
    }
}
