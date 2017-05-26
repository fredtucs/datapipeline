package com.northconcepts.datapipeline.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.Stack;

import com.northconcepts.datapipeline.core.DataException;
import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.Record;

public class FileSystemInfoReader extends DataReader
{
    private static final String[] FIELD_NAMES;
    private final File root;
    private FileFilter filter;
    private boolean recurseDirectories;
    private boolean includeDirectories;
    private boolean absolutePaths;
    private final Stack<Directory> directoryStack;
    private Directory currentDirectory;
    
    public FileSystemInfoReader(final File root) {
        super();
        this.includeDirectories = false;
        this.absolutePaths = false;
        this.directoryStack = new Stack<Directory>();
        this.root = root;
    }
    
    public FileFilter getFilter() {
        return this.filter;
    }
    
    public FileSystemInfoReader setFilter(final FileFilter filter) {
        this.filter = filter;
        return this;
    }
    
    public boolean getRecurseDirectories() {
        return this.recurseDirectories;
    }
    
    public FileSystemInfoReader setRecurseDirectories(final boolean recurseDirectories) {
        this.recurseDirectories = recurseDirectories;
        return this;
    }
    
    public boolean getIncludeDirectories() {
        return this.includeDirectories;
    }
    
    public FileSystemInfoReader setIncludeDirectories(final boolean includeDirectories) {
        this.includeDirectories = includeDirectories;
        return this;
    }
    
    public boolean getAbsolutePaths() {
        return this.absolutePaths;
    }
    
    public FileSystemInfoReader setAbsolutePaths(final boolean absolutePaths) {
        this.absolutePaths = absolutePaths;
        return this;
    }
    
    @Override
	public DataException addExceptionProperties(final DataException exception) {
        exception.set("FileSystemReader.root", this.root);
        exception.set("FileSystemReader.currentDirectory", this.currentDirectory);
        exception.set("FileSystemReader.filter", this.filter);
        exception.set("FileSystemReader.includeDirectories", this.includeDirectories);
        exception.set("FileSystemReader.recurseDirectories", this.recurseDirectories);
        exception.set("FileSystemReader.absolutePaths", this.absolutePaths);
        exception.set("FileSystemReader.depth", this.directoryStack.size());
        return super.addExceptionProperties(exception);
    }
    
    @Override
	public void open() throws DataException {
        this.currentDirectory = new Directory(this.root, this.filter);
        super.open();
    }
    
    @Override
	public void close() {
        try {
            this.directoryStack.clear();
            this.currentDirectory = null;
        }
        finally {
            super.close();
        }
    }
    
    @Override
	protected Record readImpl() throws Throwable {
        final File file = this.currentDirectory.nextFile();
        if (file != null) {
            if (file.isDirectory()) {
                if (this.recurseDirectories) {
                    this.directoryStack.push(this.currentDirectory);
                    this.currentDirectory = new Directory(file, this.filter);
                }
                if (!this.includeDirectories) {
                    return this.readImpl();
                }
            }
            return this.createRecord(file);
        }
        if (!this.directoryStack.isEmpty()) {
            this.currentDirectory = this.directoryStack.pop();
            return this.readImpl();
        }
        return null;
    }
    
    private Record createRecord(final File file) {
        final Record record = new Record(FileSystemInfoReader.FIELD_NAMES);
        record.getField(0).setValue(file.getName());
        record.getField(1).setValue(this.getPath(file.getPath()));
        record.getField(2).setValue(this.getPath(file.getParent()));
        record.getField(3).setValue(file.isDirectory());
        record.getField(4).setValue(file.length());
        record.getField(5).setValue(new Date(file.lastModified()));
        record.getField(6).setValue(file.canRead());
        record.getField(7).setValue(file.canWrite());
        record.getField(8).setValue(file.isHidden());
        record.setAlive();
        return record;
    }
    
    private String getPath(final String path) {
        if (this.absolutePaths) {
            return path;
        }
        final int startIndex = this.root.getPath().length() + 1;
        return (startIndex < path.length()) ? path.substring(startIndex) : "";
    }
    
    static {
        FIELD_NAMES = new String[] { "file", "path", "parent", "directory", "size", "modified", "read", "write", "hidden" };
    }
}
