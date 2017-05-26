package com.northconcepts.datapipeline.core;

import java.util.HashMap;

public final class Functions
{
    private static final HashMap<String, String> functions;
    
    public static void add(final String alias, final String signature) {
        Functions.functions.put(alias, signature);
    }
    
    public static void add(final String alias, final Class<?> clazz, final String methodName) {
        Functions.functions.put(alias, clazz.getName() + "." + methodName);
    }
    
    public static void add(final Class<?> clazz, final String methodName) {
        Functions.functions.put(methodName, clazz.getName() + "." + methodName);
    }
    
    public static String get(final String alias) {
        return Functions.functions.get(alias);
    }
    
    public static int getCount() {
        return Functions.functions.size();
    }
    
    public static String[] getAliases() {
        return Functions.functions.keySet().toArray(new String[Functions.functions.size()]);
    }
    
    public static void reset(final boolean clear) {
        if (clear) {
            Functions.functions.clear();
        }
        add("parseInt", "java.lang.Integer.parseInt");
        add("parseLong", "java.lang.Long.parseLong");
        add("parseDouble", "java.lang.Double.parseDouble");
        add("parseBoolean", "java.lang.Boolean.parseBoolean");
    }
    
    static {
        functions = new HashMap<String, String>();
        reset(false);
    }
}
