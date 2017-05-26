package com.northconcepts.datapipeline.transform.format;

public class Rounder
{
    private int decimalPlaces;
    private int decimalShiftMagnitude;
    private RoundingPolicy roundPolicy;
    
    public Rounder(final RoundingPolicy roundPolicy, final int decimalPlaces) {
        super();
        this.decimalPlaces = -1;
        this.decimalShiftMagnitude = 1;
        this.roundPolicy = RoundingPolicy.UNNECESSARY;
        this.setRoundPolicy(roundPolicy);
        this.setDecimalPlaces(decimalPlaces);
    }
    
    public int getDecimalPlaces() {
        return this.decimalPlaces;
    }
    
    public Rounder setDecimalPlaces(final int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
        this.decimalShiftMagnitude = 1;
        for (int i = 0; i < decimalPlaces; ++i) {
            this.decimalShiftMagnitude *= 10;
        }
        return this;
    }
    
    public RoundingPolicy getRoundPolicy() {
        return this.roundPolicy;
    }
    
    public Rounder setRoundPolicy(final RoundingPolicy roundPolicy) {
        this.roundPolicy = roundPolicy;
        return this;
    }
    
    public double apply(final double value) {
        if (this.roundPolicy == RoundingPolicy.UNNECESSARY || this.decimalPlaces < 0) {
            return value;
        }
        double newValue = value * this.decimalShiftMagnitude;
        switch (this.roundPolicy) {
            case UP: {
                newValue = up(newValue);
                break;
            }
            case DOWN: {
                newValue = down(newValue);
                break;
            }
            case CEILING: {
                newValue = Math.ceil(newValue);
                break;
            }
            case FLOOR: {
                newValue = Math.floor(newValue);
                break;
            }
            case HALF_UP: {
                newValue = Math.round(newValue);
                break;
            }
            case HALF_DOWN: {
                newValue = halfDown(newValue);
                break;
            }
            case HALF_EVEN: {
                newValue = even(newValue);
                break;
            }
            case HALF_ODD: {
                newValue = odd(newValue);
                break;
            }
        }
        newValue /= this.decimalShiftMagnitude;
        return newValue;
    }
    
    private static double halfDown(final double a) {
        return Math.ceil(a - 0.5);
    }
    
    private static double up(final double a) {
        return Math.ceil(Math.abs(a)) * sign(a);
    }
    
    private static double down(final double a) {
        return Math.floor(Math.abs(a)) * sign(a);
    }
    
    @Override
	public String toString() {
        return "round " + this.getRoundPolicy() + " to " + this.getDecimalPlaces() + " decimal places";
    }
    
    private static double sign(final double a) {
        return (a <= 0.0) ? -1.0 : 1.0;
    }
    
    private static double odd(final double a) {
        final boolean odd = ((long)a & 0x1L) == 0x1L;
        if (odd) {
            return halfDown(Math.abs(a)) * sign(a);
        }
        return Math.round(Math.abs(a)) * sign(a);
    }
    
    private static double even(final double a) {
        final boolean even = ((long)a & 0x1L) == 0x0L;
        if (even) {
            return halfDown(Math.abs(a)) * sign(a);
        }
        return Math.round(Math.abs(a)) * sign(a);
    }
    
    public enum RoundingPolicy
    {
        UP(0), 
        DOWN(1), 
        CEILING(2), 
        FLOOR(3), 
        HALF_UP(4), 
        HALF_DOWN(5), 
        HALF_EVEN(6), 
        HALF_ODD(8), 
        UNNECESSARY(7);
        
        private final int policy;
        
        private RoundingPolicy(final int policy) {
            this.policy = policy;
        }
        
        public int getPolicy() {
            return this.policy;
        }
    }
}
