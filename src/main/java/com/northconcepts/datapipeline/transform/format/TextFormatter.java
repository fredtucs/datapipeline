package com.northconcepts.datapipeline.transform.format;

import java.util.ArrayList;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.Field;
import com.northconcepts.datapipeline.core.FieldType;
import com.northconcepts.datapipeline.transform.FieldTransformer;

class TextFormatter extends FieldTransformer
{
    private ArrayList<TextOperation> operations;
    private TextOperation currentOperation;
    
    public TextFormatter(final String name) {
        super(name);
        this.operations = new ArrayList<TextOperation>();
    }
    
    private TextOperation get(final int index) {
        return this.operations.get(index);
    }
    
    private TextFormatter add(final TextOperation... operation) {
        for (int i = 0; i < operation.length; ++i) {
            this.operations.add(operation[i]);
        }
        return this;
    }
    
    public TextFormatter clear() {
        this.operations.clear();
        return this;
    }
    
    @Override
	protected void transformField(final Field field) throws Throwable {
        String string = field.getValueAsString();
        if (string != null) {
            for (int i = 0; i < this.operations.size(); ++i) {
                this.currentOperation = this.get(i);
                string = this.currentOperation.apply(string);
            }
            this.currentOperation = null;
            field.setValue(string);
        }
        else {
            field.setNull(FieldType.STRING);
        }
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        if (this.currentOperation != null) {
            exception.set("TextFormatter.operation", this.currentOperation);
        }
        return exception;
    }
    
    public TextFormatter lowerCase() {
        return this.add(new TextOperation("lowerCase") {
            @Override
			public String apply(final String value) {
                return value.toLowerCase();
            }
        });
    }
    
    public TextFormatter upperCase() {
        return this.add(new TextOperation("upperCase") {
            @Override
			public String apply(final String value) {
                return value.toUpperCase();
            }
        });
    }
    
    public TextFormatter lowerCaseFirstChar() {
        return this.add(new TextOperation("lowerCaseFirstChar") {
            @Override
			public String apply(final String value) {
                if (value.length() == 0) {
                    return value;
                }
                return Character.toLowerCase(value.charAt(0)) + value.substring(1);
            }
        });
    }
    
    public TextFormatter upperCaseFirstChar() {
        return this.add(new TextOperation("upperCaseFirstChar") {
            @Override
			public String apply(final String value) {
                if (value.length() == 0) {
                    return value;
                }
                return Character.toUpperCase(value.charAt(0)) + value.substring(1);
            }
        });
    }
    
    public TextFormatter trim() {
        return this.add(new TextOperation("trim") {
            @Override
			public String apply(final String value) {
                return value.trim();
            }
        });
    }
    
    public TextFormatter trimLeft() {
        return this.add(new TextOperation("trimLeft") {
            @Override
			public String apply(final String value) {
                int length;
                int i;
                for (length = value.length(), i = 0; i < length && value.charAt(i) <= ' '; ++i) {}
                return (i > 0) ? value.substring(i) : value;
            }
        });
    }
    
    public TextFormatter trimRight() {
        return this.add(new TextOperation("trimRight") {
            @Override
			public String apply(final String value) {
                int length;
                for (length = value.length(); length > 0 && value.charAt(length - 1) <= ' '; --length) {}
                return (length < value.length()) ? value.substring(0, length) : value;
            }
        });
    }
    
    public TextFormatter padLeft(final int length, final char filler) {
        return this.add(new TextOperation("padLeft") {
            @Override
			public String apply(final String value) {
                if (value.length() >= length) {
                    return value;
                }
                final int count = length - value.length();
                final StringBuilder buffer = new StringBuilder(length);
                for (int i = 0; i < count; ++i) {
                    buffer.append(filler);
                }
                buffer.append(value);
                return buffer.toString();
            }
        });
    }
    
    public TextFormatter padRight(final int length, final char filler) {
        return this.add(new TextOperation("padRight") {
            @Override
			public String apply(final String value) {
                if (value.length() >= length) {
                    return value;
                }
                final int count = length - value.length();
                final StringBuilder buffer = new StringBuilder(length);
                buffer.append(value);
                for (int i = 0; i < count; ++i) {
                    buffer.append(filler);
                }
                return buffer.toString();
            }
        });
    }
    
    public TextFormatter left(final int length) {
        return this.add(new TextOperation("left") {
            @Override
			public String apply(final String value) {
                if (value.length() <= length) {
                    return value;
                }
                return value.substring(0, length);
            }
        });
    }
    
    public TextFormatter right(final int length) {
        return this.add(new TextOperation("right") {
            @Override
			public String apply(final String value) {
                if (value.length() <= length) {
                    return value;
                }
                return value.substring(value.length() - length, value.length());
            }
        });
    }
    
    public TextFormatter substring(final int begin, final int end) {
        return this.add(new TextOperation("substring") {
            @Override
			public String apply(final String value) {
                if (begin > value.length() || end > value.length()) {
                    return "";
                }
                return value.substring(begin, end);
            }
        });
    }
    
    public TextFormatter append(final String suffix) {
        return this.add(new TextOperation("append") {
            @Override
			public String apply(final String value) {
                return value + suffix;
            }
        });
    }
    
    public TextFormatter prepend(final String prefix) {
        return this.add(new TextOperation("prepend") {
            @Override
			public String apply(final String value) {
                return prefix + value;
            }
        });
    }
    
    public TextFormatter insert(final int index, final String text) {
        return this.add(new TextOperation("prepend") {
            @Override
			public String apply(final String value) {
                if (index >= value.length()) {
                    return value + text;
                }
                return new StringBuilder(value).insert(index, text).toString();
            }
        });
    }
    
    private abstract static class TextOperation
    {
        private final String name;
        
        public TextOperation(final String name) {
            super();
            this.name = name;
        }
        
        @Override
		public String toString() {
            return this.name;
        }
        
        public abstract String apply(final String p0);
    }
}
