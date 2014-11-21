package org.hyperion.rs2.cache;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class Cache {

	private static CacheFileManager[] cacheFileManagers;
	private static CacheFile containersInformCacheFile;
	
	public static final void init() throws IOException {
		System.out.println("Loading cache...");
		byte[] cacheFileBuffer = new byte[520];
		RandomAccessFile containersInformFile = new RandomAccessFile("./data/cache/main_file_cache.idx255", "r");
		RandomAccessFile dataFile =	new RandomAccessFile("./data/cache/main_file_cache.dat2", "r");
		containersInformCacheFile = new CacheFile(255, containersInformFile, dataFile, 500000, cacheFileBuffer);
		int length = (int) (containersInformFile.length() / 6);
		cacheFileManagers = new CacheFileManager[length];
		for(int i = 0; i < length; i++) {
			File f = new File("./data/cache/main_file_cache.idx" + i);
			if(f.exists() && f.length() > 0) {
				cacheFileManagers[i] = new CacheFileManager(new CacheFile(i, new RandomAccessFile(f, "r"), dataFile, 1000000, cacheFileBuffer), true);
			}
		}
		UpdateServer.loadCache();
	}
	
	public static final CacheFileManager[] getCacheFileManagers() {
		return cacheFileManagers;
	}
	
	public static final CacheFile getConstainersInformCacheFile() {
		return containersInformCacheFile;
	}
}