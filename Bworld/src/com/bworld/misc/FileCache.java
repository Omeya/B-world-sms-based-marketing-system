package com.bworld.misc;

import java.io.File;
import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class FileCache.
 */
public class FileCache {

	/** The cache dir. */
	private File cacheDir;

	/**
	 * Instantiates a new file cache.
	 *
	 * @param context the context
	 * @param dir the dir
	 */
	public FileCache(Context context, String dir) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(), dir);
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	/**
	 * Gets the file.
	 *
	 * @param url the url
	 * @return the file
	 */
	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	/**
	 * Clear.
	 */
	public void clear() {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

}