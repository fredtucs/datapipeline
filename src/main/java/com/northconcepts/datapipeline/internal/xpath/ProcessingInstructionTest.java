package com.northconcepts.datapipeline.internal.xpath;

import com.northconcepts.datapipeline.internal.lang.Util;

public class ProcessingInstructionTest extends NodeTest
{
    private final String name;
    
    public ProcessingInstructionTest(final String name) {
        super();
        this.name = name;
    }
    
    @Override
	public String toString() {
        return "ProcessingInstructionTest[" + this.name + "]";
    }
    
    @Override
	public boolean matches(final XmlNode node) {
        return node.getType() == NodeType.PROCESSING_INSTRUCTION && (Util.isEmpty(this.name) || Util.matches(this.name, node.getName().toString(), false, false));
    }
    
    @Override
	public Object getValue(final XmlNode node) {
        return node.getText();
    }
}
