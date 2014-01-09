package com.techcamp05.hanoiplaces;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

/*
 * @author Tung Do
 * 
 */

public class ImageLoader {
	private static MemoryCache memoryCache = null;
	
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	ExecutorService executorService;
	
	private Context mContext;

	/*
	 * Constructor
	 * @param
	 * @return
	 */
	public ImageLoader(Context context) {
		if (memoryCache == null) {
			memoryCache = new MemoryCache();
		}
		executorService = Executors.newFixedThreadPool(5);
		this.mContext = context;
	}

	/*
	 * Display an image
	 * @param url, imageView
	 * @return
	 */
	public void DisplayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(url, imageView);
			imageView.setImageBitmap(null);
		}
	}

	/*
	 * Queueing photos
	 * @param url, ImageView
	 * @return queue
	 */
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	/*
	 * get bitmap photos
	 * @param url
	 * @return Bitmap
	 * 
	 */
	public Bitmap getBitmap(String url) {
		// from web
		try {

			// Download bitmap.
			Bitmap bitmapDownload = null;
			try {
				bitmapDownload = DownloadImage.download(url);
			} catch (Exception e) {
				bitmapDownload = null;
			}
			return bitmapDownload;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError) {
				memoryCache.clear();
				System.gc();
			}
			return null;
		}
	}

	/*
	 * Task for the queue
	 */
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	/*
	 * class to load photos to the display
	 */
	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		/*
		 * Load photo do display
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}
	
	/*
	 * check if a photo is reused
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageBitmap(null);
		}
	}

	public void clearCache() {
		memoryCache.clear();
	}
}
