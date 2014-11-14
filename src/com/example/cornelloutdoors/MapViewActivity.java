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
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class MapViewActivity extends ActionBarActivity{
	
	private GoogleMap mMap;
	public List<Marker> latLongs = new ArrayList<Marker>();
	public int cycleIndex;
	public JSONArray locationsArray;
	public HashMap<String, CornellActivity> markers;
	public LinkedList<String> userActivities;
	GlobalState gs;
	Builder bounds;
	String prevActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        gs = (GlobalState) getApplication();
        bounds = new LatLngBounds.Builder();
        Intent intent = getIntent();
        prevActivity = intent.getStringExtra("Activity");
    }
	
	protected void onResume()
	{
		super.onResume();
		try {
			TextView suggestText = (TextView) findViewById(R.id.suggesttext);
			if( prevActivity.equals("ListView") )
			{
				suggestText.setVisibility(View.VISIBLE);
			}
			else
			{
				suggestText.setVisibility(View.GONE);
			}
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			synchronized( gs.activities )
			{
				markers = gs.getActivities();
			}
			Iterator<CornellActivity> iter = markers.values().iterator();
			while( iter.hasNext() )
			{
				CornellActivity curActivity = iter.next();
				Double lat = curActivity.latitude;
				Double lon = curActivity.longitude;
				LatLng loc = new LatLng(lat, lon);
				
				Marker newmarker = mMap.addMarker(new MarkerOptions()
						.position(loc)
						.title(curActivity.name));
				
				bounds.include( new LatLng(lat, lon) );
				
				latLongs.add( newmarker );
			}
			mMap.setOnCameraChangeListener( new OnCameraChangeListener()
			{
				@Override
				public void onCameraChange(CameraPosition arg0)
				{
					if( latLongs.size() != 0 )
					{
						mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 50));
					}
					mMap.setOnCameraChangeListener( null );
				}
			});
			
			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				public void onInfoWindowClick(Marker marker) {
					Intent informationScreen = new Intent(MapViewActivity.this, MarkerInformation.class);
					
					informationScreen.putExtra("info", marker.getTitle());
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
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void cycleLocations(View view)
	{
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		LatLng curr;
		System.out.println("Length:" + latLongs.size());
		
		//Haven't initialized anything yet
		if ( latLongs.size() == 0 )
		{
			//initializeActivityList();
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
