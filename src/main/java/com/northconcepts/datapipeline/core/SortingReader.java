package com.northconcepts.datapipeline.core;

import java.io.File;
import java.io.IOException;

import com.northconcepts.datapipeline.file.FileReader;
import com.northconcepts.datapipeline.file.FileWriter;
import com.northconcepts.datapipeline.internal.io.TemporaryFileManager;
import com.northconcepts.datapipeline.internal.io.TemporaryFolder;
import com.northconcepts.datapipeline.job.JobTemplate;
import com.northconcepts.datapipeline.memory.MemoryReader;
import com.northconcepts.datapipeline.memory.MemoryWriter;

public class SortingReader extends ProxyReader {
	private final RecordComparator order;
	private final int bufferSize;
	private boolean cacheFieldIndexes;

	public SortingReader(final DataReader reader, final int bufferSize) {
		super(reader);
		this.order = new RecordComparator();
		this.bufferSize = bufferSize;
	}

	public SortingReader(final DataReader reader) {
		this(reader, 0);
	}

	public boolean isCacheFieldIndexes() {
		return this.cacheFieldIndexes;
	}

	public SortingReader setCacheFieldIndexes(final boolean cacheFieldIndexes) {
		this.cacheFieldIndexes = cacheFieldIndexes;
		return this;
	}

	public RecordComparator getOrder() {
		return this.order;
	}

	@Override
	public void open() {
		super.open();
		try {
			this.sort();
		} catch (DataException e) {
			throw e;
		} catch (Throwable e2) {
			throw this.exception(e2);
		}
	}

	private void sort() throws Throwable, IOException {
		final RecordList buffer = new RecordList();
		final DataReader reader = this.getNestedReader();
		if (this.bufferSize < 1) {
			final MemoryWriter sink = new MemoryWriter(buffer);
			JobTemplate.DEFAULT.transfer(reader, sink);
			this.sortInMemory(buffer);
		} else {
			final TemporaryFileManager fileManager = new TemporaryFileManager();
			TemporaryFolder folder = null;
			final long bufferSize = this.bufferSize << 20;
			long bytes = 0L;
			int fileCount = 0;
			Record record;
			while ((record = reader.read()) != null) {
				final long recordSize = record.getSizeInBytes();
				if (bytes + recordSize > bufferSize) {
					folder = this.flushBuffer(buffer, fileManager, folder, fileCount);
					++fileCount;
					bytes = 0L;
				}
				buffer.add(record);
				bytes += recordSize;
			}
			if (fileCount == 0) {
				this.sortInMemory(buffer);
			} else {
				folder = this.flushBuffer(buffer, fileManager, folder, fileCount);
				++fileCount;
				do {
					final TemporaryFolder targetFolder = fileManager.newFolder("sort_overflow", true);
					this.mergeFiles(folder, targetFolder, fileCount);
					folder.release();
					folder = targetFolder;
					fileCount = (fileCount + 1) / 2;
				} while (fileCount > 1);
				final TemporaryFolder folder2 = folder;
				this.setNestedDataReader(new FileReader(folder.getFile("0.bin")) {
					@Override
					public void close() {
						try {
							super.close();
						} finally {
							try {
								folder2.release();
							} catch (IOException e) {
								throw this.exception(e);
							}
						}
					}
				}, true);
			}
		}
	}

	private void sortInMemory(final RecordList buffer) throws Throwable {
		buffer.sort(this.order, this.cacheFieldIndexes);
		this.setNestedDataReader(new MemoryReader(buffer), true);
	}

	private TemporaryFolder flushBuffer(final RecordList buffer, final TemporaryFileManager fileManager, TemporaryFolder folder, final int fileCount)
			throws Throwable {
		buffer.sort(this.order, this.cacheFieldIndexes);
		if (folder == null) {
			folder = fileManager.newFolder("sort_overflow", true);
		}
		final FileWriter sink = new FileWriter(folder.getFile(fileCount + ".bin"));
		JobTemplate.DEFAULT.transfer(new MemoryReader(buffer), sink);
		buffer.clear();
		return folder;
	}

	private void mergeFiles(final TemporaryFolder sourceFolder, final TemporaryFolder targetFolder, final int fileCount) throws Throwable {
		final int merges = fileCount / 2;
		for (int i = 0; i < merges; ++i) {
			final File sourceFile1 = sourceFolder.getFile(i * 2 + ".bin");
			final File sourceFile2 = sourceFolder.getFile(i * 2 + 1 + ".bin");
			final File targetFile = targetFolder.getFile(i + ".bin");
			final FileReader source1 = new FileReader(sourceFile1);
			final FileReader source2 = new FileReader(sourceFile2);
			final FileWriter sink = new FileWriter(targetFile);
			final DataEndpointGroup endpoints = new DataEndpointGroup().add(source1).add(source2).add(sink);
			try {
				endpoints.open();
				this.mergeFile(source1, source2, sink);
			} finally {
				endpoints.close();
			}
			sourceFile1.delete();
			sourceFile2.delete();
		}
		if (merges * 2 < fileCount) {
			JobTemplate.DEFAULT.transfer(new FileReader(sourceFolder.getFile(fileCount - 1 + ".bin")),
					new FileWriter(targetFolder.getFile(merges + ".bin")));
		}
	}

	private void mergeFile(final DataReader leftSource, final DataReader rightSource, final DataWriter sink) throws Throwable {
		Record leftRecord;
		Record rightRecord;
		while ((leftRecord = leftSource.peek(0)) != null && (rightRecord = rightSource.peek(0)) != null) {
			if (this.order.compare(leftRecord, rightRecord) <= 0) {
				sink.write(leftSource.read());
			} else {
				sink.write(rightSource.read());
			}
		}
		copy(leftSource, sink);
		copy(rightSource, sink);
	}

	public static void copy(final DataReader source, final DataWriter sink) throws Throwable {
		Record record;
		while ((record = source.read()) != null) {
			sink.write(record);
		}
	}
}
