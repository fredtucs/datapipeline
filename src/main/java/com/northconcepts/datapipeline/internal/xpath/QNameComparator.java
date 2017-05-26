package com.northconcepts.datapipeline.internal.xpath;

import java.util.Comparator;

import javax.xml.namespace.QName;

public class QNameComparator implements Comparator<QName>
{
    public static final QNameComparator INSTANCE;
    
    public int compare(final QName o1, final QName o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        int diff = this.comparePart(o1.getNamespaceURI(), o2.getNamespaceURI());
        if (diff == 0) {
            diff = this.comparePart(o1.getLocalPart(), o2.getLocalPart());
        }
        return diff;
    }
    
    private int comparePart(final String o1, final String o2) {
        if (o1 == o2) {
            return 0;
        }
        if ("*".equals(o1) || "*".equals(o2)) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        return o1.toString().compareToIgnoreCase(o2.toString());
    }
    
    public static boolean matches(final QName o1, final QName o2) {
        return QNameComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final QName o1, final QName o2) {
        return QNameComparator.INSTANCE.compare(o1, o2) < 0;
    }
    
    public static boolean after(final QName o1, final QName o2) {
        return QNameComparator.INSTANCE.compare(o1, o2) > 0;
    }
    
    static {
        INSTANCE = new QNameComparator();
    }
}
