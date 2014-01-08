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
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class ServerQuery {
	/*
	 * Querying famous places from server
	 * 
	 * @param
	 * 
	 * @return xml string data
	 * 
	 * @author Huy Phung
	 */
	public static String getFamousPlaces() throws ClientProtocolException,
			IOException, IllegalStateException {
		// String query =
		// "select ?label (sample(?lon)as ?lon)(sample(?lat)as ?lat)(sample(?num)as ?num)(sample(?street)as ?street) (sample(?desc)as ?desc) (sample(?img)as ?img) {?uri rdfs:label ?label. filter(lang(?label)='en').?uri vtio:hasLongtitude ?lon.?uri vtio:hasLatitude ?lat. ?uri vtio:hasAbstract ?desc.?uri vtio:isWellKnown \"true\"^^xsd:boolean.?uri vtio:hasMedia ?media.?media vtio:hasURL ?img . filter (regex(?img, \"png\", \"i\")||regex(?img,\"jpg\",\"i\")). optional{?uri vtio:hasLocation ?add.?add vtio:hasValue ?num.?add vtio:isPartOf ?str.?str rdfs:label ?street.filter(lang(?street)='en').}. optional{?uri vtio:hasLocation ?str.?str rdfs:label ?street. filter(lang(?street)='en').}}group by ?label";
		String query = "getFamousPlaces";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.50.140/HanoiTour/query.php");
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
	        Log.e("http response", stringbuilder.toString());
	        return stringbuilder.toString();
	        
			//return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String searchForPlace(String keyword)
			throws ClientProtocolException, IOException, IllegalStateException {
		/*
		 * TODO
		 * search by keyword
		 */
		return null;
	}

}
