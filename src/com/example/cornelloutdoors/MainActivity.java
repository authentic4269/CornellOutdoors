package com.example.cornelloutdoors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.content.Context;
import android.content.Intent;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

public class MainActivity extends ActionBarActivity {
	
	private LocationManager locationManager;
	private GoogleMap mMap;
	public LinkedList<String> userActivities;
	public HashMap<String, JSONObject> markers;
	JSONArray locationsArray;
	private String serverString = "http://sleepy-wave-3087.herokuapp.com/";
	private String configfile = "cornelloutdoorsconfig";
	public String[] activityTypes = new String[] {"Rock Climbing", "Hiking", "Paddling", "Swimming", "Skiing", 
			"Running", "Weightlifting", "Sailing", "Golfing", "Basketball", "Football", "Ping Pong", "Table Tennis"
	};
	

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		boolean deleted = deleteFile( configfile );
		if( deleted )
		{
			System.out.println( "deleted \n");
		}
		super.onCreate(savedInstanceState);
		try {
			BufferedReader settingsInput = new BufferedReader(new InputStreamReader(openFileInput(configfile), "UTF-8"));
			String line;
			
			userActivities = new LinkedList<String>();
			while ((line = settingsInput.readLine()) != null)
			{
				userActivities.add(line);
			}
			setContentView(R.layout.activity_main);
			if (savedInstanceState == null) {
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, new PlaceholderFragment()).commit();
			}
		} 
		
		
		catch (FileNotFoundException e)
		{
			// have the user set up their initial settings
			setContentView(R.layout.activity_settings);
			final ListView settingsListView = (ListView) findViewById(R.id.list);
			final ArrayList<Setting> settings = new ArrayList<Setting>();
			for (int i = 0; i < activityTypes.length; i++)
			{
				settings.add(new Setting(activityTypes[i]));
			}
			final SettingArrayAdapter adapter = new SettingArrayAdapter(this, 
					settings);
			settingsListView.setAdapter(adapter);
			final Button nextButton = (Button) findViewById(R.id.continuebutton);
			
			nextButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					userActivities = new LinkedList<String>();
					for (int i = 0; i < adapter.getCount(); i++)
					{
						if (adapter.getItem(i).checked)
						{
							userActivities.add(adapter.getItem(i).name);
						}
					}
					try {
						FileOutputStream fos = openFileOutput(configfile, Context.MODE_PRIVATE);
						for (int i = 0; i < userActivities.size(); i++)
						{
							fos.write((userActivities.get(i) + '\n').getBytes());
						}
						fos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					setContentView(R.layout.activity_main);
					if (savedInstanceState == null) {
						getSupportFragmentManager().beginTransaction()
								.add(R.id.container, new PlaceholderFragment()).commit();
					}
				}
				
			});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				String queryString = serverString + '?';
				String line;
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
				for (int i = 0; i < locationsArray.length(); i++)
				{
					m = locationsArray.getJSONObject(i);
					LatLng loc = new LatLng(Double.parseDouble(m.getString("latitude")), 
							Double.parseDouble(m.getString("longitude")));
					Marker newmarker = mMap.addMarker(new MarkerOptions()
							.position(loc)
							.title(m.getString("name")));

					markers.put(m.getString("name"), m);
					
				}
				mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker marker) {
						JSONObject markerData = markers.get(marker.getTitle());
						System.out.println(markerData);
						Intent informationScreen = new Intent(MainActivity.this, MarkerInformation.class);
						
						informationScreen.putExtra("info", markerData.toString());
						startActivity( informationScreen );
						return;
					}
					
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

	}


	public void findCurrentLocation(View view)
	{
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		//Grab Location and Add Marker
        Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        LatLng curr = new LatLng(loc.getLatitude(),loc.getLongitude());
        initializeActivityList();
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
	
	public class Setting {
		public String name;
		public boolean checked;
		
		public Setting(String n)
		{
			name = n;
			checked = false;
		}
		
	}
	 

}
