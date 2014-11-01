package com.example.cornelloutdoors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.support.v4.app.Fragment;

public class MapViewActivity extends ActionBarActivity{
	
	private GoogleMap mMap;
	public List<Marker> latLongs = new ArrayList<Marker>();
	public int cycleIndex;
	public JSONArray locationsArray;
	public HashMap<String, JSONObject> markers;
	public LinkedList<String> userActivities;
	GlobalState gs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        gs = (GlobalState) getApplication();
    }
	
	private void initializeActivityList() {
		ActivityLoader loader = new ActivityLoader();
		loader.execute();
	}

  public class ActivityLoader extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... arg0) {
		String response = "";
		try {
			System.out.println( "Attempting to run background task \n");
			String serverString = gs.getServer();
			String queryString = serverString + '?';
			String line;
			userActivities = gs.getUserActivities();
			Iterator<String> iter = userActivities.iterator();
			while (iter.hasNext())
			{
				queryString = queryString + iter.next() + '&';
			}
			URL url = new URL(queryString);
			System.out.println( "URL: " + queryString);
			URLConnection connection = url.openConnection();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			System.out.println("Getting Input");
			while (null != ((line = input.readLine())))
			{
				response = response + line; 
			}
		} catch (Exception e) 
		{
			System.out.println( "Error with background task \n");
			e.printStackTrace();
		}
		return response;
	}
	
	@Override
	protected void onPostExecute(String obj) {
		try {
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			locationsArray = new JSONArray(obj);
			JSONObject m;
			markers = new HashMap<String, JSONObject>();
			System.out.println("Placing Markers\n");
			for (int i = 0; i < locationsArray.length(); i++)
			{
				m = locationsArray.getJSONObject(i);
				Double lat = Double.parseDouble(m.getString("latitude"));
				Double lon = Double.parseDouble(m.getString("longitude"));
				LatLng loc = new LatLng(lat, lon);
				
				Marker newmarker = mMap.addMarker(new MarkerOptions()
						.position(loc)
						.title(m.getString("name")));
				
				latLongs.add( newmarker );

				markers.put(m.getString("name"), m);
				
			}
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				public void onInfoWindowClick(Marker marker) {
					JSONObject markerData = markers.get(marker.getTitle());
					System.out.println(markerData);
					Intent informationScreen = new Intent(MapViewActivity.this, MarkerInformation.class);
					
					informationScreen.putExtra("info", markerData.toString());
					startActivity( informationScreen );
					return;
				}
				
			});
			
			mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng point){
					Intent suggestActivity = new Intent(MapViewActivity.this, SuggestActivity.class);
					
					suggestActivity.putExtra("lat", point.latitude);
					suggestActivity.putExtra("lon", point.longitude);
					suggestActivity.putExtra("activities", userActivities);
					startActivity( suggestActivity );
					return;
				}
			});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
  }

	public void cycleLocations(View view)
	{
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		LatLng curr;
		
		//Haven't initialized anything yet
		if ( latLongs.size() == 0 )
		{
			initializeActivityList();
			LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			Location loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
	
	        curr = new LatLng(loc.getLatitude(),loc.getLongitude());
			
		}
		else
		{
	        //Grab Location and Add Marker
	        Marker marker = latLongs.get( cycleIndex );
	      	curr = marker.getPosition();
	      	
	      	cycleIndex += 1;
	      	if(cycleIndex >= latLongs.size())
	      	{
	      		cycleIndex = 0;
	      	}
		}
	    //Zoom In On Marker
	    resetCamera(curr,.002);
	}
	
	public void resetCamera(LatLng curr, double offset)
	{
	        LatLng lowerBound = new LatLng(curr.latitude-offset, curr.longitude-offset);
	        LatLng upperBound = new LatLng(curr.latitude+offset, curr.longitude+offset);
	        LatLngBounds currBounds = new LatLngBounds(lowerBound, upperBound);
	        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(currBounds, 0));
	}
}
