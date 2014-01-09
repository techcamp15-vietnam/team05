package com.techcamp05.hanoiplaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/*
 * @author Huy Phung
 */
public class ServerQuery {
	
	private static String uri = "";
	
	public static void setServerURI (String uri) {
		ServerQuery.uri = uri;
	}
	
	public static String getServerUri (String uri) {
		return ServerQuery.uri;
	}
	
	/*
	 * @param longtitude
	 * @param latitude
	 */
	public static String getNearbyPlaces(Double lon, Double lat) 
			throws ClientProtocolException, IOException, IllegalStateException {
		String request = "getNearbyPlaces";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);
			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("request", request));
			nameValuePairs.add(new BasicNameValuePair("lon", lon.toString()));
			nameValuePairs.add(new BasicNameValuePair("lat", lat.toString()));
			nameValuePairs.add(new BasicNameValuePair("date", date.toString()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			InputStream in = response.getEntity().getContent();
			StringBuilder stringbuilder = new StringBuilder();
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(in),1024);
			String line;
			while((line = bfrd.readLine()) != null)
				stringbuilder.append(line);
			return stringbuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/*
	 * get famous places
	 * @param
	 * @return xml results
	 */
	public static String getFamousPlaces() 
			throws ClientProtocolException, IOException, IllegalStateException {
		
		String query = "getFamousPlaces";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);
			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("request", query));
			nameValuePairs.add(new BasicNameValuePair("date", date.toString()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			InputStream in = response.getEntity().getContent();
			StringBuilder stringbuilder = new StringBuilder();
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(in),1024);
			String line;
			while((line = bfrd.readLine()) != null)
				stringbuilder.append(line);
			return stringbuilder.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * search places by keyword
	 * @param keyword
	 * @return xml
	 */
	public static String searchForPlace(String keyword)
			throws ClientProtocolException, IOException, IllegalStateException {
		String query = "getPlacesByKeyword";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(uri);
			Calendar c = Calendar.getInstance();
			Date date = c.getTime();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("request", query));
			nameValuePairs.add(new BasicNameValuePair("keyword", keyword));
			nameValuePairs.add(new BasicNameValuePair("date", date.toString()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			InputStream in = response.getEntity().getContent();
			StringBuilder stringbuilder = new StringBuilder();
			BufferedReader bfrd = new BufferedReader(new InputStreamReader(in),1024);
			String line;
			while((line = bfrd.readLine()) != null)
				stringbuilder.append(line);
			return stringbuilder.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
