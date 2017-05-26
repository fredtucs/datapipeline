package com.northconcepts.datapipeline.core;

import java.util.HashMap;
import java.util.Map;

public interface Session
{
    boolean containsSessionProperty(String p0);
    
    Object getSessionProperty(String p0);
    
    void setSessionProperty(String p0, Object p1);
    
    void copySessionPropertiesFrom(Session p0);
    
    public static class SessionImpl implements Session
    {
        private Map<String, Object> map;
        
        private synchronized void init() {
            if (this.map == null) {
                this.map = new HashMap<String, Object>();
            }
        }
        
        public synchronized boolean containsSessionProperty(String name) {
            this.init();
            return this.map.containsKey(name);
        }
        
        public synchronized Object getSessionProperty(String name) {
            this.init();
            return this.map.get(name);
        }
        
        public synchronized void setSessionProperty(String name, Object value) {
            this.init();
            this.map.put(name, value);
        }
        
        public synchronized void copySessionPropertiesFrom(Session session) {
            SessionImpl s;
            s = (SessionImpl)session;
            if (s.map != null) {
                this.init();
                this.map.putAll(s.map);
            }
        }
    }
}
