package com.northconcepts.datapipeline.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.northconcepts.datapipeline.core.DataException;

public class ExcelDocument
{
    private final ProviderType providerType;
    private final Provider provider;
    
    public ExcelDocument() {
        this(ProviderType.POI_XSSF);
    }
    
    public ExcelDocument(final ProviderType providerType) {
        super();
        if (providerType == null) {
            throw new DataException("providerType is null");
        }
        this.providerType = providerType;
        (this.provider = providerType.newProvider()).newWorkbook();
    }
    
    public ProviderType getProviderType() {
        return this.providerType;
    }
    
    Provider getProvider() {
        return this.provider;
    }
    
    public ExcelDocument open(final InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is null");
        }
        this.provider.openWorkbook(inputStream);
        return this;
    }
    
    public ExcelDocument open(final File file) {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        try {
            return this.open(new FileInputStream(file));
        }
        catch (IOException e) {
            throw DataException.wrap(e).set("ExcelDocument.file", file);
        }
    }
    
    public ExcelDocument save(final OutputStream outputStream) {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is null");
        }
        this.provider.saveWorkbook(outputStream);
        return this;
    }
    
    public ExcelDocument save(final File file) {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        try {
            final FileOutputStream out = new FileOutputStream(file);
            this.save(out);
            out.close();
            return this;
        }
        catch (Throwable e) {
            throw DataException.wrap(e);
        }
    }
    
    public enum ProviderType
    {
        POI {
            @Override
			public Provider newProvider() {
                return new PoiProvider();
            }
        }, 
        POI_XSSF {
            @Override
			public Provider newProvider() {
                return new PoiXssfProvider();
            }
        }, 
        JXL {
            @Override
			public Provider newProvider() {
                return new JxlProvider();
            }
        };
        
        public abstract Provider newProvider();
    }
}
