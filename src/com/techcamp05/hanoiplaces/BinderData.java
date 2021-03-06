package com.techcamp05.hanoiplaces;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//
/*
 * Binding XML data
 * @author Huy Phung
 */
public class BinderData extends BaseAdapter {

	// XML node keys
	static final String KEY_TAG = "result"; // parent node
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
//	
	LayoutInflater inflater;
	ImageView thumb_image;
	List<HashMap<String, String>> placeDataCollection;
	ViewHolder holder;
	ImageLoader mImageLoader;
	
	double currentLon;
	double currentLat;
	
	public BinderData() {
		// TODO Auto-generated constructor stub
	}
	
	public void refreshList(List<HashMap<String, String>> list){
		this.placeDataCollection = list;
		notifyDataSetChanged();
	}

	public BinderData(Activity act, List<HashMap<String,String>> map) {

		this.placeDataCollection = map;

		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		LocationManager locationManager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		currentLon = location.getLongitude();
		currentLat = location.getLatitude();
		mImageLoader = new ImageLoader(act);
	}


	public int getCount() {
		// TODO Auto-generated method stub
		//		return idlist.size();
		return placeDataCollection.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi=convertView;
		if(convertView==null){

			vi = inflater.inflate(R.layout.list_row, null);
			holder = new ViewHolder();

			holder.tvLabel = (TextView)vi.findViewById(R.id.tvLabel); 
			holder.tvStreet = (TextView)vi.findViewById(R.id.tvStreet);
			holder.tvNum =  (TextView)vi.findViewById(R.id.tvNum); 
			holder.tvImage =(ImageView)vi.findViewById(R.id.list_image); // thumb image
			holder.tvPhone = (TextView)vi.findViewById(R.id.tvphone);
			holder.tvCat = (TextView)vi.findViewById(R.id.tvcategory);
			holder.tvDistance = (TextView)vi.findViewById(R.id.tvDistance);
			vi.setTag(holder);
		}
		else{
			holder = (ViewHolder)vi.getTag();
		}

		// Setting all values in listview
		holder.tvLabel.setText(placeDataCollection.get(position).get(KEY_LABEL));
		holder.tvStreet.setText(placeDataCollection.get(position).get(KEY_STREET));
		holder.tvNum.setText(placeDataCollection.get(position).get(KEY_NUM));
		holder.tvPhone.setText(placeDataCollection.get(position).get(KEY_PHONE));
		holder.tvCat.setText(placeDataCollection.get(position).get(KEY_CAT));
		holder.tvDistance.setText(String.format("%1$,.2f", GeoCalc.calcDistanceByLatLon(Double.parseDouble(placeDataCollection.get(position).get(KEY_LON)), Double.parseDouble(placeDataCollection.get(position).get(KEY_LAT)), currentLon, currentLat)) + "km distance.");

		//Setting an image
		String url = placeDataCollection.get(position).get(KEY_IMG);
		mImageLoader.DisplayImage(url, holder.tvImage);
		return vi;
	}

	public Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}catch(Exception e){
			Log.e("Error","" + e.getMessage());
			return null;
		}
	}
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			
		}
	};
	static class ViewHolder{

		TextView tvLabel;
		TextView tvStreet;
		TextView tvNum;
		ImageView tvImage;
		TextView tvPhone;
		TextView tvCat;
		TextView tvDistance;
	}

}
