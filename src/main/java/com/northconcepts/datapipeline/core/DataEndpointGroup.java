package com.northconcepts.datapipeline.core;

import java.util.ArrayList;

public class DataEndpointGroup extends DataEndpoint
{
    private final ArrayList<DataEndpoint> endpoints;
    
    public DataEndpointGroup() {
        super();
        this.endpoints = new ArrayList<DataEndpoint>();
    }
    
    public int getCount() {
        return this.endpoints.size();
    }
    
    public DataEndpointGroup add(final DataEndpoint... endpoints) {
        for (int i = 0; i < endpoints.length; ++i) {
            this.endpoints.add(endpoints[i]);
        }
        return this;
    }
    
    public DataEndpoint get(final int index) {
        return this.endpoints.get(index);
    }
    
    @Override
	public void open() throws DataException {
        for (int i = 0; i < this.getCount(); ++i) {
            this.get(i).open();
        }
        super.open();
    }
    
    @Override
	public void close() throws DataException {
        DataException exception = null;
        for (int i = this.getCount() - 1; i >= 0; --i) {
            final DataEndpoint endpoint = this.get(i);
            try {
                if (endpoint.isOpen()) {
                    endpoint.close();
                }
            }
            catch (Throwable e) {
                if (exception == null) {
                    exception = endpoint.exception(e);
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
        super.close();
    }
}
