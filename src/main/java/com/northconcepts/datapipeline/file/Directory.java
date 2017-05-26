package com.northconcepts.datapipeline.file;

import java.io.File;
import java.io.FileFilter;

final class Directory
{
    private final File[] files;
    private int nextIndex;
    
    public Directory(final File parent, final FileFilter fileFilter) {
        super();
        this.files = parent.listFiles(fileFilter);
    }
    
    public File nextFile() {
        return (this.files == null || this.nextIndex >= this.files.length) ? null : this.files[this.nextIndex++];
    }
}
