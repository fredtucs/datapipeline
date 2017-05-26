package com.northconcepts.datapipeline.internal.lang.comparator;

public final class CharacterComparator extends BaseComparator<Character>
{
    public static final CharacterComparator INSTANCE;
    
    public int compare(final Character o1, final Character o2) {
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
    
    public static boolean matches(final Character o1, final Character o2) {
        return CharacterComparator.INSTANCE.compare(o1, o2) == 0;
    }
    
    public static boolean before(final Character o1, final Character o2) {
        return CharacterComparator.INSTANCE.compare(o1, o2) == -1;
    }
    
    public static boolean after(final Character o1, final Character o2) {
        return CharacterComparator.INSTANCE.compare(o1, o2) == 1;
    }
    
    static {
        INSTANCE = new CharacterComparator();
    }
}
