package com.northconcepts.datapipeline.internal.xpath;

import java.util.ArrayList;
import java.util.List;

public class Step
{
    LocationPath locationPath;
    private Axis axis;
    private NodeTest nodeTest;
    private final List<Predicate> predicates;
    
    public Step() {
        super();
        this.predicates = new ArrayList<Predicate>();
    }
    
    @Override
	public String toString() {
        final String s = ((this.axis == null) ? "no axis" : this.axis.toString()) + ";  " + this.nodeTest + ";  predicates=" + this.predicates.size();
        return s;
    }
    
    public LocationPath getLocationPath() {
        return this.locationPath;
    }
    
    public Step getPreviousStep() {
        final List<Step> steps = this.locationPath.getSteps();
        final int index = steps.indexOf(this);
        return (index > 0) ? steps.get(index - 1) : null;
    }
    
    public boolean hasPreviousStep() {
        return this.getPreviousStep() != null;
    }
    
    public Axis getAxis() {
        if (this.axis == null && this.isfirst()) {
            return this.locationPath.getType().getAxis();
        }
        return this.axis;
    }
    
    public void setAxis(final Axis axis) {
        this.axis = axis;
    }
    
    public void setDefaultAxis(final Axis axis) {
        if (this.axis == null) {
            this.axis = axis;
        }
    }
    
    public NodeTest getNodeTest() {
        return this.nodeTest;
    }
    
    public void setNodeTest(final NodeTest nodeTest) {
        if (nodeTest != null) {
            nodeTest.step = this;
        }
        this.nodeTest = nodeTest;
    }
    
    public List<Predicate> getPredicates() {
        return this.predicates;
    }
    
    public boolean isfirst() {
        return this.locationPath.getSteps().get(0) == this;
    }
    
    protected boolean matches(final XmlNode node) {
        return this.getAxis().test(this, node);
    }
    
    protected boolean matchesElement(final XmlNode node) {
        if (this.nodeTest.matches(node)) {
            for (int i = 0; i < this.predicates.size(); ++i) {
                if (!this.predicates.get(i).matches(node)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public Object getValue(final XmlNode node) {
        return this.nodeTest.getValue(node);
    }
}
