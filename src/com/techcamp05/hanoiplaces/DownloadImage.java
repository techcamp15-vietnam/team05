/**
 * 
 */
package com.techcamp05.hanoiplaces;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Tung Do
 * 
 */
public class DownloadImage {
	/**
	 * This method is used for get InputStream from url normal.
	 * 
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	public static InputStream openHttpConnection(String urlString)
			throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting");
		}
		return in;
	}

	/**
	 * This method is used for download image from URL.
	 * 
	 * @param URL
	 * @return
	 */
	public static Bitmap download(String URL) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = openHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (Exception e) {
			bitmap = null;
		}
		return bitmap;
	}
}
