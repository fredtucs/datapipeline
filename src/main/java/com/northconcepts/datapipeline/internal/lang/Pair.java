package com.northconcepts.datapipeline.internal.lang;

public class Pair<A, B>
{
    private A a;
    private B b;
    
    public static <A, B> Pair<A, B> create(final A a, final B b) {
        return new Pair<A, B>(a, b);
    }
    
    public Pair() {
        super();
    }
    
    public Pair(final A a, final B b) {
        super();
        this.a = a;
        this.b = b;
    }
    
    public A getA() {
        return this.a;
    }
    
    public Pair<A, B> setA(final A a) {
        this.a = a;
        return this;
    }
    
    public B getB() {
        return this.b;
    }
    
    public Pair<A, B> setB(final B b) {
        this.b = b;
        return this;
    }
    
    @Override
	public String toString() {
        return "Pair[a=" + this.getA() + ", b=" + this.getB() + "]";
    }
}
