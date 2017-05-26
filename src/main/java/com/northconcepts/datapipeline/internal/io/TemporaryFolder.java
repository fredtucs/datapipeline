package com.northconcepts.datapipeline.internal.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class TemporaryFolder
{
    private final TemporaryFileManager fileManager;
    private final File path;
    private final File lockFile;
    private RandomAccessFile randomAccessLockFile;
    private FileLock lock;
    
    TemporaryFolder(final TemporaryFileManager fileManager, final File path) throws IOException {
        super();
        if (path.exists()) {
            throw new IOException("folder already exists [" + path.getAbsolutePath() + "]");
        }
        if (!path.mkdirs()) {
            throw new IOException("unable to create folder [" + path.getAbsolutePath() + "]");
        }
        this.fileManager = fileManager;
        this.path = path;
        this.lockFile = new File(path, ".lock");
        this.lock();
        this.lockFile.deleteOnExit();
        path.deleteOnExit();
        fileManager.register(this);
    }
    
    public File getFile(final String name) {
        final File file = new File(this.path, name);
        file.deleteOnExit();
        return file;
    }
    
    public File getPath() {
        return this.path;
    }
    
    private void lock() throws IOException {
        this.randomAccessLockFile = new RandomAccessFile(this.lockFile, "rw");
        final FileChannel channel = this.randomAccessLockFile.getChannel();
        this.lock = channel.tryLock();
        if (this.lock == null) {
            throw new IOException("couldn't acquire file lock [" + this.lockFile.getAbsolutePath() + "]");
        }
    }
    
    public void release() throws IOException {
        this.fileManager.unregister(this);
        if (this.lock != null) {
            this.lock.release();
            this.lock = null;
        }
        if (this.randomAccessLockFile != null) {
            this.randomAccessLockFile.close();
            this.randomAccessLockFile = null;
            this.fileManager.removeFolder(this.getPath());
        }
    }
    
    @Override
	protected void finalize() throws Throwable {
        try {
            this.release();
        }
        finally {
            super.finalize();
        }
    }
    
    @Override
	public String toString() {
        return this.getPath().toString();
    }
}
