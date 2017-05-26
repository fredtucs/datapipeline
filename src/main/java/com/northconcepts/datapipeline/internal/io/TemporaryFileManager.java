package com.northconcepts.datapipeline.internal.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashSet;

import com.northconcepts.datapipeline.internal.lang.Util;

public class TemporaryFileManager
{
    public static final String FOLDER_PREFIX = "datastream_tmp_";
    public static final int RANDOM_FOLDER_SUFFIX_LENGTH = 10;
    public static final int RANDOM_FILE_NAME_LENGTH = 10;
    public static final String LOCK_FILE_NAME = ".lock";
    private final File rootTempFolder;
    private final HashSet<TemporaryFolder> folders;
    
    public TemporaryFileManager() throws IOException {
        this(new File(System.getProperty("java.io.tmpdir")));
    }
    
    public TemporaryFileManager(final File rootTempFolder) throws IOException {
        super();
        this.folders = new HashSet<TemporaryFolder>();
        this.rootTempFolder = rootTempFolder;
        this.cleanup();
    }
    
    public File getRootTempFolder() {
        return this.rootTempFolder;
    }
    
    private void cleanup() throws IOException {
        final File[] folders = this.rootTempFolder.listFiles(new FileFilter() {
            public boolean accept(final File pathname) {
                return pathname.getName().startsWith("datastream_tmp_") && pathname.isDirectory();
            }
        });
        for (int i = 0; i < folders.length; ++i) {
            this.removeFolder(folders[i]);
        }
    }
    
    public TemporaryFolder newFolder(final String name) throws IOException {
        return this.newFolder(name, false);
    }
    
    public TemporaryFolder newFolder(final String namePrefix, final boolean randomSuffix) throws IOException {
        String name = (namePrefix != null) ? namePrefix : "";
        if (randomSuffix) {
            name = name + "_" + Util.getRandomString(10);
        }
        File file;
        do {
            file = new File(this.rootTempFolder, "datastream_tmp_" + name);
        } while (randomSuffix && file.exists());
        final TemporaryFolder folder = new TemporaryFolder(this, file);
        return folder;
    }
    
    void register(final TemporaryFolder folder) {
        this.folders.add(folder);
    }
    
    void unregister(final TemporaryFolder folder) {
        this.folders.remove(folder);
    }
    
    public void release() throws IOException {
        final TemporaryFolder[] folders = this.folders.toArray(new TemporaryFolder[this.folders.size()]);
        for (int i = 0; i < folders.length; ++i) {
            folders[i].release();
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
        return this.getRootTempFolder().toString();
    }
    
    public static boolean isFileLocked(final File file) throws IOException {
        if (!file.exists()) {
            return false;
        }
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        try {
            final FileChannel channel = randomAccessFile.getChannel();
            final FileLock lock = channel.tryLock();
            if (lock != null) {
                lock.release();
            }
            return lock == null;
        }
        finally {
            randomAccessFile.close();
        }
    }
    
    public static boolean isFolderLocked(final File folder) throws IOException {
        return isFileLocked(new File(folder, ".lock"));
    }
    
    public boolean removeFolder(final File folder) throws IOException {
        if (folder.isDirectory()) {
            if (isFolderLocked(folder)) {
                throw new IOException("folder is locked [" + folder.getAbsolutePath() + "]");
            }
            final String[] children = folder.list();
            for (int i = 0; i < children.length; ++i) {
                final boolean success = this.removeFolder(new File(folder, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return folder.delete();
    }
}
