package com.northconcepts.datapipeline.core;

public final class Product
{
    private static boolean logged;
    
    public static Record onRead(final DataReader reader, final Record record) {
        if (limitExceeded(reader)) {
            return null;
        }
        return record;
    }
    
    public static boolean allowWrite(final DataWriter writer, final Record record) {
        return !limitExceeded(writer);
    }
    
    private static boolean limitExceeded(final DataEndpoint endpoint) {
        final boolean exceeded = endpoint.getRecordCount() + 31072L - 1L >> 17 > 0L;
        if (exceeded && !Product.logged) {
            DataEndpoint.log.error("The FREE edition of Data Pipeline is limited to 100000 records.");
            Product.logged = true;
        }
        return exceeded;
    }
}
