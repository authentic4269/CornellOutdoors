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
import java.util.List;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.content.Context;
import android.content.Intent;


public class MainActivity extends ActionBarActivity {
	public LinkedList<String> userActivities = new LinkedList<String>();
	public HashMap<String, JSONObject> markers;
	//public List<Marker> latLongs = new ArrayList<Marker>();
	
	JSONArray locationsArray;
	public String serverString = "http://sleepy-wave-3087.herokuapp.com/";
	private String configfile = "cornelloutdoorsconfig";
	public String[] activityTypes = new String[] {"Rock Climbing", "Hiking", "Paddling", "Swimming", "Skiing", 
			"Running", "Weightlifting", "Sailing", "Golfing", "Basketball", "Football", "Ping Pong", "Table Tennis"
	};
	
	GlobalState gs;
	

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		gs = (GlobalState) getApplication();
		boolean deleted = false;
		deleteFile( configfile );
		if( deleted )
		{
			System.out.println( "deleted \n");
		}
		super.onCreate(savedInstanceState);
		try {
			BufferedReader settingsInput = new BufferedReader(new InputStreamReader(openFileInput(configfile), "UTF-8"));
			String line;
			
			while ((line = settingsInput.readLine()) != null)
			{
				userActivities.add(line);
			}
			gs.setUserActivities(userActivities);
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
					for (int i = 0; i < adapter.getCount(); i++)
					{
						if (adapter.getItem(i).checked)
						{
							userActivities.add(adapter.getItem(i).name);
						}
					}
					gs.setUserActivities(userActivities);
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
			
			/*final Button historyButton = (Button) findViewById(R.id.history);
			final Intent historyIntent = new Intent(this, HistoryActivity.class);
			historyButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					startActivity(historyIntent);
				}
				
			});*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent intent = new Intent(this, TrackingService.class);
		startService(intent);
	}
	
	//Show the Map View
	public void showMap( View view )
	{
		Intent mapView = new Intent( MainActivity.this, MapViewActivity.class );
		startActivity( mapView );
		return;
	}
	
	//Show the Activity Preferences View
	public void changePreferences( View view )
	{
		setContentView(R.layout.activity_settings);
		final ListView settingsListView = (ListView) findViewById(R.id.list);
		final ArrayList<Setting> settings = new ArrayList<Setting>();
		final ArrayList<Integer> currentlyCheckedIndices = new ArrayList<Integer>();
		for (int i = 0; i < activityTypes.length; i++)
		{
			settings.add(new Setting(activityTypes[i]));
			if (userActivities.contains(activityTypes[i]))
			{
				currentlyCheckedIndices.add( (Integer) i );
			}
		}
		final SettingArrayAdapter adapter = new SettingArrayAdapter(this, 
				settings);
		//Precheck the current activities
		for(int i = 0; i < currentlyCheckedIndices.size(); i++)
		{
			adapter.getItem( currentlyCheckedIndices.get(i) ).checked = true;
		}
		
		settingsListView.setAdapter(adapter);
		final Button nextButton = (Button) findViewById(R.id.continuebutton);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				userActivities.clear();
				for (int i = 0; i < adapter.getCount(); i++)
				{
					if (adapter.getItem(i).checked)
					{
						userActivities.add(adapter.getItem(i).name);
					}
				}
				gs.setUserActivities(userActivities);
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
				getSupportFragmentManager().beginTransaction()
						.add(R.id.container, new PlaceholderFragment()).commit();
			}
			
		});
		
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
			
			setTouchListeners( rootView );
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
	
	static public void setTouchListeners(View view)
	{
		try{
			//ranked_list button
			System.out.println(view);
			Button mapButton = (Button) view.findViewById(R.id.ranked_list);
			mapButton.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Button mapButton = (Button) v.findViewById(R.id.ranked_list);
					if( event.getAction() == MotionEvent.ACTION_DOWN )
					{
						mapButton.setBackgroundResource(R.drawable.homebutton1);
					}
					else if( event.getAction() == MotionEvent.ACTION_UP )
					{
						mapButton.setBackgroundResource(R.drawable.homebutton1lighter);
					}
					return false;
				}
			});
			
			//map_view button
			mapButton = (Button) view.findViewById(R.id.map_view);
			mapButton.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Button mapButton = (Button) v.findViewById(R.id.map_view);
					if( event.getAction() == MotionEvent.ACTION_DOWN )
					{
						mapButton.setBackgroundResource(R.drawable.homebutton2);
					}
					else if( event.getAction() == MotionEvent.ACTION_UP )
					{
						mapButton.setBackgroundResource(R.drawable.homebutton2lighter);
					}
					return false;
				}
			});
			
			//change_preferences button
			mapButton = (Button) view.findViewById(R.id.change_preferences);
			mapButton.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Button mapButton = (Button) v.findViewById(R.id.change_preferences);
					if( event.getAction() == MotionEvent.ACTION_DOWN )
					{
						mapButton.setBackgroundResource(R.drawable.homebutton3);
					}
					else if( event.getAction() == MotionEvent.ACTION_UP )
					{
						mapButton.setBackgroundResource(R.drawable.homebutton3lighter);
					}
					return false;
				}
			});
			
			//history button
			mapButton = (Button) view.findViewById(R.id.history);
			mapButton.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Button mapButton = (Button) v.findViewById(R.id.history);
					if( event.getAction() == MotionEvent.ACTION_DOWN )
					{
						mapButton.setBackgroundResource(R.drawable.homebutton4);
					}
					else if( event.getAction() == MotionEvent.ACTION_UP )
					{
						mapButton.setBackgroundResource(R.drawable.homebutton4lighter);
					}
					return false;
				}
			});
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	 

}
