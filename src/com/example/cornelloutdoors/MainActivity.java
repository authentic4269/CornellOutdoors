package com.example.cornelloutdoors;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
