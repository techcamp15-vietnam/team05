package com.techcamp05.hanoiplaces;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivity extends Activity {

	// XML node keys
	static final String KEY_TAG = "result";
	static final String KEY_LABEL = "label";
	static final String KEY_LON = "lon";
	static final String KEY_LAT = "lat";
	static final String KEY_URI = "uri";
	static final String KEY_STREET = "street";
	static final String KEY_NUM = "num";
	static final String KEY_DESC = "desc";
	static final String KEY_IMG = "img";
	static final String KEY_CAT = "cat";

	// List items
	ListView list;
	BinderData adapter = null;
	List<HashMap<String, String>> placeDataCollection;
	String xmlString = "";

	/*
	 * Testing function
	 */
	public String getXmlData() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle) onCreate, parse XML
	 * sample data into Hashmap collection
	 * 
	 * @param savedInstanceState
	 * 
	 * @author Huy Phung
	 */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		StrictMode.ThreadPolicy policy = new
				StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				
		try {
			// Doc building from sample data
			// sample data will be replaced by service consuming later
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			//new GetData().execute();
			xmlString = ServerQuery.getFamousPlaces();
			
			InputSource is = new InputSource(new StringReader(xmlString));
			Document doc = docBuilder.parse(is);
//
//			TransformerFactory transformerFactory = TransformerFactory
//					.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			Writer out = new StringWriter();
//			transformer.transform(new DOMSource(doc), new StreamResult(out));
//			System.out.println(out.toString());
//			doc = docBuilder.parse(new InputSource(new StringReader(out.toString())));
			
			placeDataCollection = new ArrayList<HashMap<String, String>>();

			// normalize text representation
			doc.getDocumentElement().normalize();

			NodeList placeList = doc.getElementsByTagName("result");

			HashMap<String, String> map = null;

			for (int i = 0; i < placeList.getLength(); i++) {

				map = new HashMap<String, String>();

				Node firstPlaceNode = placeList.item(i);

				if (firstPlaceNode.getNodeType() == Node.ELEMENT_NODE) {

					Element firstPlaceElement = (Element) firstPlaceNode;

					// 1-------
					NodeList labelList = firstPlaceElement
							.getElementsByTagName(KEY_LABEL);
					Element firstLabelElement = (Element) labelList.item(0);
					NodeList textLabelList = firstLabelElement.getChildNodes();
					// --label
					map.put(KEY_LABEL, ((Node) textLabelList.item(0))
							.getNodeValue().trim());

					// 2.-------
					NodeList lonList = firstPlaceElement
							.getElementsByTagName(KEY_LON);
					Element firstLonElement = (Element) lonList.item(0);
					NodeList textLonList = firstLonElement.getChildNodes();
					// --lon
					map.put(KEY_LON, ((Node) textLonList.item(0))
							.getNodeValue().trim());

					// 3.-------
					NodeList latList = firstPlaceElement
							.getElementsByTagName(KEY_LAT);
					Element firstLatElement = (Element) latList.item(0);
					NodeList textLatList = firstLatElement.getChildNodes();
					// --lat
					map.put(KEY_LAT, ((Node) textLatList.item(0))
							.getNodeValue().trim());

					// 4.-------
					NodeList uriList = firstPlaceElement
							.getElementsByTagName(KEY_URI);
					Element firstUriElement = (Element) uriList.item(0);
					NodeList textUriList = firstUriElement.getChildNodes();
					// --uri
					Log.e("null", ((Node) textUriList.item(0))
							.getNodeValue());
					map.put(KEY_URI, ((Node) textUriList.item(0))
							.getNodeValue().trim());

					// 5.-------
					NodeList numList = firstPlaceElement
							.getElementsByTagName(KEY_NUM);
					Element firstNumElement = (Element) numList.item(0);
					NodeList textNumList = firstNumElement.getChildNodes();
					// --num
					map.put(KEY_NUM, ((Node) textNumList.item(0))
							.getNodeValue().trim());

					// 6.-------
					NodeList streetList = firstPlaceElement
							.getElementsByTagName(KEY_STREET);
					Element firstStreetElement = (Element) streetList.item(0);
					NodeList textStreetList = firstStreetElement
							.getChildNodes();
					// --street
					map.put(KEY_STREET, ((Node) textStreetList.item(0))
							.getNodeValue().trim());

					// 7.-------
					NodeList descList = firstPlaceElement
							.getElementsByTagName(KEY_DESC);
					Element firstDescElement = (Element) descList.item(0);
					NodeList textDescList = firstDescElement.getChildNodes();
					// --desc
					map.put(KEY_DESC, ((Node) textDescList.item(0))
							.getNodeValue().trim());

					// 8.-------
					NodeList imgList = firstPlaceElement
							.getElementsByTagName(KEY_IMG);
					Element firstImgElement = (Element) imgList.item(0);
					NodeList textImgList = firstImgElement.getChildNodes();
					// --img
					map.put(KEY_IMG, ((Node) textImgList.item(0))
							.getNodeValue().trim());

					// 9.-------
					NodeList catList = firstPlaceElement
							.getElementsByTagName(KEY_CAT);
					Element firstCatElement = (Element) catList.item(0);
					NodeList textCatList = firstCatElement.getChildNodes();
					// --category
					map.put(KEY_CAT, ((Node) textCatList.item(0))
							.getNodeValue().trim());

					// Add to the Arraylist
					placeDataCollection.add(map);
				}
			}

			BinderData bindingData = new BinderData(this, placeDataCollection);

			list = (ListView) findViewById(R.id.list);

			Log.i("BEFORE", "<<------------- Before SetAdapter-------------->>");

			list.setAdapter(bindingData);

			Log.i("AFTER", "<<------------- After SetAdapter-------------->>");

			// Click event for single list row
			list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Intent i = new Intent();
					i.setClass(MainActivity.this, DetailActivity.class);

					// parameters
					i.putExtra("position", String.valueOf(position + 1));

					i.putExtra("label",
							placeDataCollection.get(position).get(KEY_LABEL));
					i.putExtra("lon",
							placeDataCollection.get(position).get(KEY_LON));
					i.putExtra("lat",
							placeDataCollection.get(position).get(KEY_LAT));
					i.putExtra("uri",
							placeDataCollection.get(position).get(KEY_URI));
					i.putExtra("num",
							placeDataCollection.get(position).get(KEY_NUM));
					i.putExtra("street",
							placeDataCollection.get(position).get(KEY_STREET));
					i.putExtra("desc",
							placeDataCollection.get(position).get(KEY_DESC));
					i.putExtra("img",
							placeDataCollection.get(position).get(KEY_IMG));
					i.putExtra("cat",
							placeDataCollection.get(position).get(KEY_CAT));

					startActivity(i);
				}
			});

		}

		catch (IOException ex) {
			ex.printStackTrace();
			Log.e("Error in MainActivity", "IOException " + ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e("Error in MainActivity",
					"Loading exception " + ex.getMessage());
		}
	}

	/*
	 * @author Huy Phung
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	private class GetData extends AsyncTask<Void, Void, String> {
//		@Override
//		protected String doInBackground(Void... params) {
//
//			try {
//				String query = "getFamousPlaces";
//				HttpClient httpclient = new DefaultHttpClient();
//				HttpPost httppost = new HttpPost(
//						"http://192.168.50.140/HanoiTour/query.php");
//				Calendar c = Calendar.getInstance();
//				Date date = c.getTime();
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				nameValuePairs.add(new BasicNameValuePair("request", query));
//				nameValuePairs.add(new BasicNameValuePair("date", date
//						.toString()));
//				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//				HttpResponse response = httpclient.execute(httppost);
//				xmlString += EntityUtils.toString(response.getEntity(),"UTF-8");
//				xmlString = xmlString.replaceAll("\\r\\n", " ");
//				xmlString.replaceFirst(".$","");
//				Log.e("HTTP", xmlString);
//
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
	

}
