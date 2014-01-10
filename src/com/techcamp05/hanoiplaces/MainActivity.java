package com.techcamp05.hanoiplaces;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, LocationListener {

	final Context context = this;

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
	static final String KEY_PHONE = "phone";

	// List items
	ListView list;
	BinderData adapter = null;
	// List<HashMap<String, String>> placeDataCollection;
	String xmlString = "";
	List<HashMap<String, String>> placeDataCollection;

	// GoogleAPI querying
	public SearchView searchView;
	public String search_query;

	private LocationClient mLocationClient;
	private TextView mMessageView;
	private TextView resultText;

	public double lonLocation;
	public double latLocation;
	private BinderData bindingData;

	private Button showNearBy;
	private Button showCategory;

	// Request for a location
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(10000) // 5 seconds
			.setFastestInterval(5000) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	/*
	 * Testing function
	 * 
	 * @author Huy Phung
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

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ServerQuery.setServerURI("http://192.168.50.140/HanoiTour/query.php");
		resultText = (TextView) findViewById(R.id.text_view_search);
		/*
		 * author: Tran Hoang Tung View a dialog to search by category
		 * ==========
		 * ============================================================
		 */
		showNearBy = (Button) findViewById(R.id.show_near_by);
		// set onclick listenner to button
		showNearBy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog_near_by = new Dialog(context);
				dialog_near_by.setContentView(R.layout.near_by);
				dialog_near_by.setTitle("Set distance");
				searchNearbyPlaces();
			}
		});

		showCategory = (Button) findViewById(R.id.show_category);

		// Set onclick listenner to button, view category dialog
		showCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog dialog_category = new Dialog(context);
				dialog_category.setContentView(R.layout.category_dialog);
				dialog_category.setTitle("Category search");
				
				// Set button category
				ImageButton UniImgBut = (ImageButton) dialog_category
						.findViewById(R.id.university_category);
				UniImgBut.setOnClickListener(new OnClickListener() {

					// Set even on click button
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						searchCategory("University");
						dialog_category.dismiss();
					}
				});

				ImageButton hosptImgBut = (ImageButton) dialog_category
						.findViewById(R.id.hospital_category);
				hosptImgBut.setOnClickListener(new OnClickListener() {

					// Set even on click button
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						searchCategory("Hospital");
						dialog_category.dismiss();
					}
				});
				
				ImageButton restatImgBut = (ImageButton) dialog_category
						.findViewById(R.id.restaurant_category);
				restatImgBut.setOnClickListener(new OnClickListener() {

					// Set even on click button
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						searchCategory("Restaurant");
						dialog_category.dismiss();
					}
				});
				ImageButton famousImgBut = (ImageButton) dialog_category
						.findViewById(R.id.famous_category);
				famousImgBut.setOnClickListener(new OnClickListener() {

					// Set even on click button
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						searchCategory("Famous");
						dialog_category.dismiss();
					}
				});

				ImageButton hotelImgBut = (ImageButton) dialog_category
						.findViewById(R.id.hotel_category);
				hotelImgBut.setOnClickListener(new OnClickListener() {

					// Set even on click button
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						searchCategory("Hotel");
						dialog_category.dismiss();
					}
				});

				ImageButton aTMImgBut = (ImageButton) dialog_category
						.findViewById(R.id.atm_category);
				aTMImgBut.setOnClickListener(new OnClickListener() {

					// Set even on click button
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						searchCategory("ATM");
						dialog_category.dismiss();
					}
				});

				ImageButton barImgBut = (ImageButton) dialog_category
						.findViewById(R.id.bar_category);
				barImgBut.setOnClickListener(new OnClickListener() {

					// Set even on click button
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						searchCategory("Bar");
						dialog_category.dismiss();
					}
				});

				ImageButton cafeImgBut = (ImageButton) dialog_category
						.findViewById(R.id.cafe_category);
				cafeImgBut.setOnClickListener(new OnClickListener() {

					// Set even on click button
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						searchCategory("Coffee");
						dialog_category.dismiss();
					}
				});

				ImageButton phmcyImgBut = (ImageButton) dialog_category
						.findViewById(R.id.pharmacy_category);
				phmcyImgBut.setOnClickListener(new OnClickListener() {

					// Set even on click button
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						searchCategory("Pharmacies");
						dialog_category.dismiss();
					}
				});
				dialog_category.show();

			}
		});

		/*
		 * ======================================================================
		 * =
		 */

		//mMessageView = (TextView) findViewById(R.id.text_view_location);
		try {
			xmlString = ServerQuery.getFamousPlaces();
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlString));
			Document doc = docBuilder.parse(is);

			placeDataCollection = new ArrayList<HashMap<String, String>>();

			// normalize text representation
			doc.getDocumentElement().normalize();

			NodeList placeList = doc.getElementsByTagName("result");

			HashMap<String, String> map = null;
			resultText.setText("We suggest some well-known places in Ha noi.");
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
					Log.e("null", ((Node) textUriList.item(0)).getNodeValue());
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
					// category formatting
					String rawCat = ((Node) textCatList.item(0)).getNodeValue()
							.trim();
					String rawCatParts[] = rawCat.split("#");
					String actualCat = rawCatParts[1].replaceAll("-", " ");

					map.put(KEY_CAT, actualCat);

					// 10.-------
					NodeList phoneList = firstPlaceElement
							.getElementsByTagName(KEY_PHONE);
					Element firstPhoneElement = (Element) phoneList.item(0);
					NodeList textPhoneList = firstPhoneElement.getChildNodes();
					// --phone
					map.put(KEY_PHONE, ((Node) textPhoneList.item(0))
							.getNodeValue().trim());

					// Add to the Arraylist
					placeDataCollection.add(map);
				}
			}

			bindingData = new BinderData(this, placeDataCollection);

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
					i.putExtra("phone",
							placeDataCollection.get(position).get(KEY_PHONE));
					startActivity(i);
				}
			});
			//resultText = (TextView) findViewById(R.id.text_view_search);
			setupSearchView();
		} catch (IOException ex) {
			ex.printStackTrace();
			Log.e("Error in MainActivity", "IOException " + ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e("Error in MainActivity",
					"Loading exception " + ex.getMessage());
		}

	}

	/*
	 * @author Huy Phung (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * search view init
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @author Tung Tran
	 */
	private void setupSearchView() {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		final SearchView searchView = (SearchView) findViewById(R.id.search_place);
		SearchableInfo searchableInfo = searchManager
				.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(searchableInfo);
	}
	

	protected void searchNearbyPlaces() {
		try {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			Log.e("GPS debugging ", location.getLongitude() + " " + location.getLatitude());
			String xmlString = ServerQuery.getNearbyPlaces(location.getLongitude(), location.getLatitude());
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory
					.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlString));
			Document doc = docBuilder.parse(is);

			placeDataCollection = new ArrayList<HashMap<String, String>>();
			bindingData = new BinderData(this, placeDataCollection);
			list.setAdapter(bindingData);
			
			placeDataCollection = new ArrayList<HashMap<String, String>>();

			// normalize text representation
			doc.getDocumentElement().normalize();

			NodeList placeList = doc.getElementsByTagName("result");

			HashMap<String, String> map = null;

			resultText.setText("We suggest some places that are nearby you.");
			for (int i = 0; i < placeList.getLength(); i++) {

				map = new HashMap<String, String>();

				Node firstPlaceNode = placeList.item(i);

				if (firstPlaceNode.getNodeType() == Node.ELEMENT_NODE) {

					Element firstPlaceElement = (Element) firstPlaceNode;

					// 1-------
					NodeList labelList = firstPlaceElement
							.getElementsByTagName(KEY_LABEL);
					Element firstLabelElement = (Element) labelList.item(0);
					NodeList textLabelList = firstLabelElement
							.getChildNodes();
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
					Log.e("null",
							((Node) textUriList.item(0)).getNodeValue());
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
					Element firstStreetElement = (Element) streetList
							.item(0);
					NodeList textStreetList = firstStreetElement
							.getChildNodes();
					// --street
					map.put(KEY_STREET, ((Node) textStreetList.item(0))
							.getNodeValue().trim());

					// 7.-------
					NodeList descList = firstPlaceElement
							.getElementsByTagName(KEY_DESC);
					Element firstDescElement = (Element) descList.item(0);
					NodeList textDescList = firstDescElement
							.getChildNodes();
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
					String rawCat = ((Node) textCatList.item(0)).getNodeValue()
							.trim();
					String rawCatParts[] = rawCat.split("#");
					String actualCat = rawCatParts[1].replaceAll("-", " ");

					map.put(KEY_CAT, actualCat);

					// 10.-------
					NodeList phoneList = firstPlaceElement
							.getElementsByTagName(KEY_PHONE);
					Element firstPhoneElement = (Element) phoneList.item(0);
					NodeList textPhoneList = firstPhoneElement
							.getChildNodes();
					// --phone
					map.put(KEY_PHONE, ((Node) textPhoneList.item(0))
							.getNodeValue().trim());

					// Add to the Arraylist
					placeDataCollection.add(map);
				}
			}

			bindingData = new BinderData(this, placeDataCollection);

			list = (ListView) findViewById(R.id.list);

			Log.i("BEFORE",
					"<<------------- Before SetAdapter-------------->>");

			list.setAdapter(bindingData);

			Log.i("AFTER",
					"<<------------- After SetAdapter-------------->>");

			// Click event for single list row
			list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Intent i = new Intent();
					i.setClass(MainActivity.this, DetailActivity.class);

					// parameters
					i.putExtra("position", String.valueOf(position + 1));

					i.putExtra("label", placeDataCollection.get(position)
							.get(KEY_LABEL));
					i.putExtra("lon", placeDataCollection.get(position)
							.get(KEY_LON));
					i.putExtra("lat", placeDataCollection.get(position)
							.get(KEY_LAT));
					i.putExtra("uri", placeDataCollection.get(position)
							.get(KEY_URI));
					i.putExtra("num", placeDataCollection.get(position)
							.get(KEY_NUM));
					i.putExtra("street", placeDataCollection.get(position)
							.get(KEY_STREET));
					i.putExtra("desc", placeDataCollection.get(position)
							.get(KEY_DESC));
					i.putExtra("img", placeDataCollection.get(position)
							.get(KEY_IMG));
					i.putExtra("cat", placeDataCollection.get(position)
							.get(KEY_CAT));
					i.putExtra("phone", placeDataCollection.get(position)
							.get(KEY_PHONE));
					startActivity(i);
				}
			});

		} catch (IOException ex) {
			ex.printStackTrace();
			Log.e("Error in MainActivity", "IOException " + ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e("Error in MainActivity",
					"Loading exception " + ex.getMessage());
		}
	
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Confirt quits");
		builder.setMessage("Are you sure you want to quit?");
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
		
		builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	protected void searchCategory(String query) {
		try {
			
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			Log.e("GPS debugging ", location.getLongitude() + " " + location.getLatitude());
			
			String xmlString = ServerQuery.getPlacesByCategory(query, location.getLongitude(), location.getLatitude());
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory
					.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlString));
			Document doc = docBuilder.parse(is);

			placeDataCollection = new ArrayList<HashMap<String, String>>();
			bindingData = new BinderData(this, placeDataCollection);
			list.setAdapter(bindingData);
			
			placeDataCollection = new ArrayList<HashMap<String, String>>();

			// normalize text representation
			doc.getDocumentElement().normalize();

			NodeList placeList = doc.getElementsByTagName("result");

			HashMap<String, String> map = null;

			if (placeList.getLength() == 0) {
				resultText
						.setText("There is no result.");
				return;
			} else {
				resultText.setText("There are "
						+ placeList.getLength() + " results.");
			}
			for (int i = 0; i < placeList.getLength(); i++) {

				map = new HashMap<String, String>();

				Node firstPlaceNode = placeList.item(i);

				if (firstPlaceNode.getNodeType() == Node.ELEMENT_NODE) {

					Element firstPlaceElement = (Element) firstPlaceNode;

					// 1-------
					NodeList labelList = firstPlaceElement
							.getElementsByTagName(KEY_LABEL);
					Element firstLabelElement = (Element) labelList.item(0);
					NodeList textLabelList = firstLabelElement
							.getChildNodes();
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
					Log.e("null",
							((Node) textUriList.item(0)).getNodeValue());
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
					Element firstStreetElement = (Element) streetList
							.item(0);
					NodeList textStreetList = firstStreetElement
							.getChildNodes();
					// --street
					map.put(KEY_STREET, ((Node) textStreetList.item(0))
							.getNodeValue().trim());

					// 7.-------
					NodeList descList = firstPlaceElement
							.getElementsByTagName(KEY_DESC);
					Element firstDescElement = (Element) descList.item(0);
					NodeList textDescList = firstDescElement
							.getChildNodes();
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
					String rawCat = ((Node) textCatList.item(0)).getNodeValue()
							.trim();
					String rawCatParts[] = rawCat.split("#");
					String actualCat = rawCatParts[1].replaceAll("-", " ");

					map.put(KEY_CAT, actualCat);

					// 10.-------
					NodeList phoneList = firstPlaceElement
							.getElementsByTagName(KEY_PHONE);
					Element firstPhoneElement = (Element) phoneList.item(0);
					NodeList textPhoneList = firstPhoneElement
							.getChildNodes();
					// --phone
					map.put(KEY_PHONE, ((Node) textPhoneList.item(0))
							.getNodeValue().trim());

					// Add to the Arraylist
					placeDataCollection.add(map);
				}
			}

			bindingData = new BinderData(this, placeDataCollection);

			list = (ListView) findViewById(R.id.list);

			Log.i("BEFORE",
					"<<------------- Before SetAdapter-------------->>");

			list.setAdapter(bindingData);

			Log.i("AFTER",
					"<<------------- After SetAdapter-------------->>");

			// Click event for single list row
			list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					Intent i = new Intent();
					i.setClass(MainActivity.this, DetailActivity.class);

					// parameters
					i.putExtra("position", String.valueOf(position + 1));

					i.putExtra("label", placeDataCollection.get(position)
							.get(KEY_LABEL));
					i.putExtra("lon", placeDataCollection.get(position)
							.get(KEY_LON));
					i.putExtra("lat", placeDataCollection.get(position)
							.get(KEY_LAT));
					i.putExtra("uri", placeDataCollection.get(position)
							.get(KEY_URI));
					i.putExtra("num", placeDataCollection.get(position)
							.get(KEY_NUM));
					i.putExtra("street", placeDataCollection.get(position)
							.get(KEY_STREET));
					i.putExtra("desc", placeDataCollection.get(position)
							.get(KEY_DESC));
					i.putExtra("img", placeDataCollection.get(position)
							.get(KEY_IMG));
					i.putExtra("cat", placeDataCollection.get(position)
							.get(KEY_CAT));
					i.putExtra("phone", placeDataCollection.get(position)
							.get(KEY_PHONE));
					startActivity(i);
				}
			});

		} catch (IOException ex) {
			ex.printStackTrace();
			Log.e("Error in MainActivity", "IOException " + ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e("Error in MainActivity",
					"Loading exception " + ex.getMessage());
		}
	
	}

	protected void onNewIntent(Intent intent) {
//		if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent
//				.getAction())) {
//			// handles suggestion clicked query
//
//		} else 
			if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			String query = intent.getStringExtra(SearchManager.QUERY);

			try {
				String xmlString = ServerQuery.getPlacesByKeyword(query);
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory
						.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(xmlString));
				Document doc = docBuilder.parse(is);

				placeDataCollection = new ArrayList<HashMap<String, String>>();
				bindingData = new BinderData(this, placeDataCollection);
				list.setAdapter(bindingData);

				// normalize text representation
				doc.getDocumentElement().normalize();

				NodeList placeList = doc.getElementsByTagName("result");

				HashMap<String, String> map = null;

				if (placeList.getLength() == 0) {
					resultText
							.setText("OOPS!!! There is no result for query: '"
									+ query);
					return;
				} else {
					resultText.setText("Searching is done! There are "
							+ placeList.getLength() + " results for query: '"
							+ query + "'");
				}
				for (int i = 0; i < placeList.getLength(); i++) {

					map = new HashMap<String, String>();

					Node firstPlaceNode = placeList.item(i);

					if (firstPlaceNode.getNodeType() == Node.ELEMENT_NODE) {

						Element firstPlaceElement = (Element) firstPlaceNode;

						// 1-------
						NodeList labelList = firstPlaceElement
								.getElementsByTagName(KEY_LABEL);
						Element firstLabelElement = (Element) labelList.item(0);
						NodeList textLabelList = firstLabelElement
								.getChildNodes();
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
						Log.e("null",
								((Node) textUriList.item(0)).getNodeValue());
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
						Element firstStreetElement = (Element) streetList
								.item(0);
						NodeList textStreetList = firstStreetElement
								.getChildNodes();
						// --street
						map.put(KEY_STREET, ((Node) textStreetList.item(0))
								.getNodeValue().trim());

						// 7.-------
						NodeList descList = firstPlaceElement
								.getElementsByTagName(KEY_DESC);
						Element firstDescElement = (Element) descList.item(0);
						NodeList textDescList = firstDescElement
								.getChildNodes();
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
						String rawCat = ((Node) textCatList.item(0)).getNodeValue()
								.trim();
						String rawCatParts[] = rawCat.split("#");
						String actualCat = rawCatParts[1].replaceAll("-", " ");

						map.put(KEY_CAT, actualCat);

						// 10.-------
						NodeList phoneList = firstPlaceElement
								.getElementsByTagName(KEY_PHONE);
						Element firstPhoneElement = (Element) phoneList.item(0);
						NodeList textPhoneList = firstPhoneElement
								.getChildNodes();
						// --phone
						map.put(KEY_PHONE, ((Node) textPhoneList.item(0))
								.getNodeValue().trim());

						// Add to the Arraylist
						placeDataCollection.add(map);
					}
				}

				bindingData = new BinderData(this, placeDataCollection);

				list = (ListView) findViewById(R.id.list);

				Log.i("BEFORE",
						"<<------------- Before SetAdapter-------------->>");

				list.setAdapter(bindingData);

				Log.i("AFTER",
						"<<------------- After SetAdapter-------------->>");

				// Click event for single list row
				list.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Intent i = new Intent();
						i.setClass(MainActivity.this, DetailActivity.class);

						// parameters
						i.putExtra("position", String.valueOf(position + 1));

						i.putExtra("label", placeDataCollection.get(position)
								.get(KEY_LABEL));
						i.putExtra("lon", placeDataCollection.get(position)
								.get(KEY_LON));
						i.putExtra("lat", placeDataCollection.get(position)
								.get(KEY_LAT));
						i.putExtra("uri", placeDataCollection.get(position)
								.get(KEY_URI));
						i.putExtra("num", placeDataCollection.get(position)
								.get(KEY_NUM));
						i.putExtra("street", placeDataCollection.get(position)
								.get(KEY_STREET));
						i.putExtra("desc", placeDataCollection.get(position)
								.get(KEY_DESC));
						i.putExtra("img", placeDataCollection.get(position)
								.get(KEY_IMG));
						i.putExtra("cat", placeDataCollection.get(position)
								.get(KEY_CAT));
						i.putExtra("phone", placeDataCollection.get(position)
								.get(KEY_PHONE));
						startActivity(i);
					}
				});

			} catch (IOException ex) {
				ex.printStackTrace();
				Log.e("Error in MainActivity", "IOException " + ex.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("Error in MainActivity",
						"Loading exception " + ex.getMessage());
			}
		}
	}

	protected void displayItemList(String xmlString) {

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
			mLocationClient.disconnect();
		}
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	public void showMyLocation(View view) {
		if (mLocationClient != null && mLocationClient.isConnected()) {
			String msg = "Location = " + mLocationClient.getLastLocation();
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// mMessageView.setText("Location = " + location);
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	/**
	 * Callback called when disconnected from GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 * 
	 * @author Tung Tran
	 */
	@Override
	public void onDisconnected() {
		// Do nothing
	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 * 
	 * @author Tung Tran
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}
}
