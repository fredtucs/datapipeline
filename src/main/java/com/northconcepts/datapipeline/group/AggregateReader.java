package com.northconcepts.datapipeline.group;

import java.util.HashMap;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.ProxyReader;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.internal.lang.Util;

public class AggregateReader extends ProxyReader
{
    private HashMap<String, AggregateOperation> operations;
    private AggregateOperation[] cachedOperations;
    private AggregateOperation currentOperation;
    
    public AggregateReader(final DataReader targetDataSource) {
        super(targetDataSource);
        this.operations = new HashMap<String, AggregateOperation>();
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.currentOperation != null) {
            exception.set("AggregateReader.operation", this.currentOperation);
        }
        return super.addExceptionProperties(exception);
    }
    
    private void cacheOperations() {
        if (this.cachedOperations == null) {
            this.cachedOperations = this.operations.values().toArray(new AggregateOperation[this.operations.size()]);
        }
    }
    
    @Override
	protected Record interceptRecord(final Record record) throws Throwable {
        this.cacheOperations();
        for (int i = 0; i < this.cachedOperations.length; ++i) {
            (this.currentOperation = this.cachedOperations[i]).accept(record);
        }
        this.currentOperation = null;
        return super.interceptRecord(record);
    }
    
    private AggregateOperation get(final String name, final String fieldName) {
        final String key = AggregateOperation.toString(name, fieldName);
        final AggregateOperation operation = this.operations.get(key);
        if (operation == null) {
            throw new DataException("unknown aggregate operation (call minimum, count, etc. first)").set("operation", key).set("operator", name).set("field", fieldName);
        }
        return operation;
    }
    
    private AggregateReader add(final AggregateOperation... operations) {
        this.cachedOperations = null;
        for (int i = 0; i < operations.length; ++i) {
            final AggregateOperation operation = operations[i];
            this.operations.put(operation.toString(), operation);
        }
        return this;
    }
    
    public AggregateReader clear() {
        this.operations.clear();
        return this;
    }
    
    public void reset() {
        this.cacheOperations();
        final AggregateOperation temp = this.currentOperation;
        for (int i = 0; i < this.cachedOperations.length; ++i) {
            (this.currentOperation = this.cachedOperations[i]).reset();
        }
        this.currentOperation = temp;
    }
    
    @Override
	public void open() throws DataException {
        this.reset();
        super.open();
    }
    
    public AggregateReader minimum(final String fieldName) {
        return this.add(new AggregateOperation("minimum", fieldName) {
            private boolean first = true;
            private Object minimum;
            
            @Override
			public void accept(final Record record) {
                if (this.first) {
                    this.minimum = record.getField(this.fieldName).getValue();
                    this.first = false;
                }
                else {
                    final Object value = record.getField(this.fieldName).getValue();
                    if (Util.compare(value, this.minimum) < 0) {
                        this.minimum = value;
                    }
                }
            }
            
            @Override
			public Object evaluate() {
                return this.minimum;
            }
            
            @Override
			public void reset() {
                this.minimum = null;
                this.first = true;
            }
        });
    }
    
    public AggregateReader maximum(final String fieldName) {
        return this.add(new AggregateOperation("maximum", fieldName) {
            private boolean first = true;
            private Object maximum;
            
            @Override
			public void accept(final Record record) {
                if (this.first) {
                    this.maximum = record.getField(this.fieldName).getValue();
                    this.first = false;
                }
                else {
                    final Object value = record.getField(this.fieldName).getValue();
                    if (Util.compare(value, this.maximum) > 0) {
                        this.maximum = value;
                    }
                }
            }
            
            @Override
			public Object evaluate() {
                return this.maximum;
            }
            
            @Override
			public void reset() {
                this.maximum = null;
                this.first = true;
            }
        });
    }
    
    public AggregateReader sum(final String fieldName) {
        return this.add(new AggregateOperation("sum", fieldName) {
            private double sum;
            
            @Override
			public void accept(final Record record) {
                final Field field = record.getField(this.fieldName);
                if (field.isNotNull()) {
                    this.sum += field.getValueAsDouble();
                }
            }
            
            @Override
			public Object evaluate() {
                return new Double(this.sum);
            }
            
            @Override
			public void reset() {
                this.sum = 0.0;
            }
        });
    }
    
    public AggregateReader average(final String fieldName) {
        return this.add(new AggregateOperation("average", fieldName) {
            private long count;
            private double sum;
            
            @Override
			public void accept(final Record record) {
                final Field field = record.getField(this.fieldName);
                if (field.isNotNull()) {
                    this.sum += field.getValueAsDouble();
                    ++this.count;
                }
            }
            
            @Override
			public Object evaluate() {
                return new Double(this.sum / this.count);
            }
            
            @Override
			public void reset() {
                this.sum = 0.0;
                this.count = 0L;
            }
        });
    }
    
    public AggregateReader count(final String fieldName) {
        return this.add(new AggregateOperation("count", fieldName) {
            private long count;
            
            @Override
			public void accept(final Record record) {
                final Field field = record.getField(this.fieldName);
                if (field.isNotNull()) {
                    ++this.count;
                }
            }
            
            @Override
			public Object evaluate() {
                return new Long(this.count);
            }
            
            @Override
			public void reset() {
                this.count = 0L;
            }
        });
    }
    
    public Object getMinimum(final String fieldName) {
        return this.get("minimum", fieldName).evaluate();
    }
    
    public Object getMaximum(final String fieldName) {
        return this.get("maximum", fieldName).evaluate();
    }
    
    public Object getSum(final String fieldName) {
        return this.get("sum", fieldName).evaluate();
    }
    
    public Object getAverage(final String fieldName) {
        return this.get("average", fieldName).evaluate();
    }
    
    public Object getCount(final String fieldName) {
        return this.get("count", fieldName).evaluate();
    }
    
    @Override
	public String toString() {
        String s = super.toString();
        this.cacheOperations();
        final AggregateOperation temp = this.currentOperation;
        for (int i = 0; i < this.cachedOperations.length; ++i) {
            this.currentOperation = this.cachedOperations[i];
            s = s + "\n" + this.currentOperation.toString() + " = " + this.currentOperation.evaluate();
        }
        this.currentOperation = temp;
        return s;
    }
    
    private abstract static class AggregateOperation
    {
        private final String name;
        protected final String fieldName;
        
        public AggregateOperation(final String name, final String fieldName) {
            super();
            this.name = name;
            this.fieldName = fieldName;
        }
        
        @Override
		public String toString() {
            return toString(this.name, this.fieldName);
        }
        
        public static String toString(final String name, final String fieldName) {
            return name + " of " + fieldName.toUpperCase();
        }
        
        public abstract void accept(final Record p0);
        
        public abstract Object evaluate();
        
        public abstract void reset();
    }
}
