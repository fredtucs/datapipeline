package com.northconcepts.datapipeline.core;

import java.io.IOException;

public interface IParser
{
    public static final int EOF = -1;
    
    DataException exception(Throwable p0);
    
    boolean ready() throws IOException;
    
    int getColumn();
    
    int peek(int p0);
    
    int pop();
    
    void match(char p0, String p1);
    
    void match(char p0);
    
    void consume();
    
    void consume(int p0);
    
    void consumeWhitespace();
    
    void consumeWhitespace(int p0);
    
    void close();
}
