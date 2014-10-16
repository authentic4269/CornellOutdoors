package com.example.cornelloutdoors;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import android.content.Context;
import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

public class MainActivity extends ActionBarActivity {
	
	private LocationManager locationManager;
	private GoogleMap mMap;
	public LinkedList<String> userActivities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			BufferedReader settingsInput = new BufferedReader(new InputStreamReader(openFileInput("cornelloutdoorsconfig"), "UTF-8"));
			String line;
			
			userActivities = new LinkedList<String>();
			while ((line = settingsInput.readLine()) != null)
			{
				userActivities.add(line);
			}
			setContentView(R.layout.activity_main);
		} catch (FileNotFoundException e)
		{
			setContentView(R.layout.activity_settings);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	public void findCurrentLocation(View view)
	{
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		//Grab Location and Add Marker
        Location loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        LatLng curr = new LatLng(loc.getLatitude(),loc.getLongitude());
        mMap.addMarker(new MarkerOptions().position(curr));

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add("Settings");
		menu.add("History");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
