package com.techcamp05.hanoiplaces;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*
 * @author Tung Tran
 */
public class DetailActivity extends FragmentActivity {

	// Google map related properties
	private Marker testPoint;
	private GoogleMap mMap;

	private static int IO_BUFFER_SIZE = 70;
	private String label;
	private String lon;
	private String lat;
	private String uri;
	private String num;
	private String street;
	private String desc;
	private String img;
	private String cat;
	private String phone;
	
	ImageView imgPlaceIcon;

	TextView tvLabel;
	TextView tvStreet;
	TextView tvNum;
	TextView tvCat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailpage);
		try {

			// handle for the UI elements
			imgPlaceIcon = (ImageView) findViewById(R.id.imageView);
			// Text fields
			tvLabel = (TextView) findViewById(R.id.textViewLabel);
			tvStreet = (TextView) findViewById(R.id.textViewStreet);
			// tvNum = (TextView) findViewById(R.id.textViewWindNum);
			tvCat = (TextView) findViewById(R.id.textView1);

			// Get position to display
			Intent i = getIntent();

			this.label = i.getStringExtra("label");
			this.lon = i.getStringExtra("lon");
			this.lat = i.getStringExtra("lat");
			this.uri = i.getStringExtra("uri");
			this.num = i.getStringExtra("num");
			this.street = i.getStringExtra("street");
			this.desc = i.getStringExtra("desc");
			this.img = i.getStringExtra("img");
			this.cat = i.getStringExtra("cat");
			this.phone = i.getStringExtra("phone");
			
			// text elements
			tvLabel.setText(label);
			tvStreet.setText(street);
			tvCat.setText(desc);
			
			String url = this.img;
			new LoadImage((ImageView) findViewById(R.id.imageView))
					.execute(url);
		}

		catch (Exception ex) {
			Log.e("Error on DetailActivity", " " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	class LoadImage extends AsyncTask<String, Void, Boolean> {

		ImageView imgv;
		private Bitmap bitmap = null;

		public LoadImage(ImageView imgv) { // TODO Auto-generated constructor stub
			this.imgv = imgv;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String Url = params[0];
			bitmap = getBitmapFromURL(Url);
			if (bitmap != null) {
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				bitmap = bitmap.createScaledBitmap(bitmap, 200, 200, false);
				imgPlaceIcon.setImageBitmap(bitmap);
			}
			super.onPostExecute(result);
		}
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			Log.e("Error", "" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	// Set marker attribute and add marker to map
	private void setMarkersToMap() {
		testPoint = mMap.addMarker(new MarkerOptions().position(
				new LatLng(Double.parseDouble(this.lat), Double
						.parseDouble(this.lon))).title("Hello"));
	}

	private void setUpMap() {
		setMarkersToMap();
		// Move camera to marker
		mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double
				.parseDouble(this.lat), Double.parseDouble(this.lon))));
		// Zoom camera in
		mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	}
	

}
