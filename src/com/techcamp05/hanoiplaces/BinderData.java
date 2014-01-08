package com.techcamp05.hanoiplaces;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.techcamp05.hanoiplaces.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

	LayoutInflater inflater;
	ImageView thumb_image;
	List<HashMap<String,String>> placeDataCollection;
	ViewHolder holder;
	
	public BinderData() {
		// TODO Auto-generated constructor stub
	}

	public BinderData(Activity act, List<HashMap<String,String>> map) {

		this.placeDataCollection = map;

		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

			vi.setTag(holder);
		}
		else{
			holder = (ViewHolder)vi.getTag();
		}

		// Setting all values in listview

		holder.tvLabel.setText(placeDataCollection.get(position).get(KEY_LABEL));
		holder.tvStreet.setText(placeDataCollection.get(position).get(KEY_STREET));
		holder.tvNum.setText(placeDataCollection.get(position).get(KEY_NUM));

		//Setting an image
		//   String uri = placeDataCollection.get(position).get(KEY_IMG);
		// int imageResource = vi.getContext().getApplicationContext().getResources().getIdentifier(uri, null, vi.getContext().getApplicationContext().getPackageName());
		//Drawable image = vi.getContext().getResources().getDrawable(imageResource);
		//holder.tvImage.setImageDrawable(image)
		return vi;
	}

	static class ViewHolder{

		TextView tvLabel;
		TextView tvStreet;
		TextView tvNum;
		ImageView tvImage;
	}

}

