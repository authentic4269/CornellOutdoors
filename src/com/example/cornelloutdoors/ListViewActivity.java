package com.example.cornelloutdoors;

import java.io.FileOutputStream;
import java.util.*;

import com.example.cornelloutdoors.MainActivity.PlaceholderFragment;
import com.example.cornelloutdoors.MainActivity.Setting;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Iterator;

public class ListViewActivity extends Activity {
	
	ActivityArrayAdapter adapter;
	LocationManager locationManager;
	CornellActivityCompare comparator;
	
	protected class CornellActivityCompare implements Comparator<CornellActivity> {

		/*
		 * Sorts CornellActivities by distance from the user
		 * (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(CornellActivity arg0, CornellActivity arg1) {
			Location loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
			if (loc == null)
			{
				return 0;
			}
			double mylong = loc.getLongitude();
			double mylat = loc.getLatitude();
			double comparison = distance(arg1.longitude, arg1.latitude, mylong, mylat) - 
					distance(arg0.longitude, arg0.latitude, mylong, mylat);
			if (comparison < 0)
				return -1;
			else if (comparison == 0)
				return 0;
			return 1;
		}
		
	}
	
	/**
	 * Returns the distance in miles between two points, in miles.
	 * 3961 miles is the radius of the earth
	 * @param long1
	 * @param lat1
	 * @param long2
	 * @param lat2
	 * @return
	 */
	public static double distance(double long1, double lat1, double long2, double lat2)
	{
		double dlon = long2 - long1;
		double dlat = lat2 - lat1;
		double a = (Math.pow(Math.sin(dlat/2), 2) + Math.cos(lat1) * Math.cos(lat2)*(Math.pow(Math.sin(dlon/2),2)));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return 3961.0 * c;
	}
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		comparator = new CornellActivityCompare();
		setContentView(R.layout.activity_listview);
		final ListView activitiesListView = (ListView) findViewById(R.id.list);
		ArrayList<CornellActivity> activities = new ArrayList<CornellActivity>();
		GlobalState gs = (GlobalState) getApplication();
		synchronized (gs.activities) {
			Iterator<CornellActivity> iter = gs.activities.values().iterator();
			locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			
	
			while(iter.hasNext())
				activities.add(iter.next());
			Collections.sort(activities, comparator);
			adapter = new ActivityArrayAdapter(this, 
					activities, comparator, locationManager);
			activitiesListView.setAdapter(adapter);
		}
		
		final EditText search = (EditText) findViewById(R.id.search);
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				String t = search.getText().toString().toLowerCase();
				adapter.filter(t);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		/*final Button historyButton = (Button) findViewById(R.id.history);
		final Intent historyIntent = new Intent(this, HistoryActivity.class);
		historyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(historyIntent);
			}
			
		});*/
	}
}
