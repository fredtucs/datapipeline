package com.northconcepts.datapipeline.core;

import java.util.ArrayList;
import java.util.Stack;

import com.northconcepts.datapipeline.internal.lang.Util;

public class Messages
{
    private static final ThreadLocal<Messages> threadLocal;
    private static final ThreadLocal<Stack<Messages>> stack;
    private final ArrayList<Message> messages;
    
    public Messages() {
        super();
        this.messages = new ArrayList<Message>();
    }
    
    public static final Messages getCurrent() {
        return Messages.threadLocal.get();
    }
    
    public static final void setCurrent(final Messages messages) {
        Messages.threadLocal.set(messages);
    }
    
    static final Stack<Messages> getStack() {
        return Messages.stack.get();
    }
    
    public static final void push() {
        getStack().push(getCurrent());
        setCurrent(new Messages());
    }
    
    public static final void pop() {
        final Messages m = getStack().pop();
        setCurrent(m);
    }
    
    public void addMessage(final Message message) {
        this.messages.add(message);
    }
    
    public int getMessageCount() {
        return this.messages.size();
    }
    
    public Message getMessage(final int index) {
        return this.messages.get(index);
    }
    
    public void addWarning(final String message, final boolean recordStackTrace) {
        this.addMessage(new Message(Message.Level.WARNING, message, recordStackTrace));
    }
    
    public void addDebug(final String message, final boolean recordStackTrace) {
        this.addMessage(new Message(Message.Level.DEBUG, message, recordStackTrace));
    }
    
    public void addWarning(final String message, final Throwable exception) {
        this.addMessage(new Message(Message.Level.WARNING, message, exception));
    }
    
    public void addDebug(final String message, final Throwable exception) {
        this.addMessage(new Message(Message.Level.DEBUG, message, exception));
    }
    
    @Override
	public String toString() {
        final StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.getMessageCount(); ++i) {
            s.append('[').append(this.getMessage(i)).append(']').append(Util.LINE_SEPARATOR);
        }
        return s.toString();
    }
    
    static {
        threadLocal = new ThreadLocal<Messages>() {
            @Override
			protected Messages initialValue() {
                return new Messages();
            }
        };
        stack = new ThreadLocal<Stack<Messages>>() {
            @Override
			protected Stack<Messages> initialValue() {
                return new Stack<Messages>();
            }
        };
    }
}
