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
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;


public class MainActivity extends ActionBarActivity {
	public LinkedList<String> userActivities;
	public HashMap<String, JSONObject> markers;
	//public List<Marker> latLongs = new ArrayList<Marker>();
	
	JSONArray locationsArray;
	public String serverString = "http://sleepy-wave-3087.herokuapp.com/";
	private String configfile = "cornelloutdoorsconfig";
	public String[] activityTypes = new String[] {"Rock Climbing", "Hiking", "Paddling", "Swimming", "Skiing", 
			"Running", "Weightlifting", "Sailing", "Golfing", "Basketball", "Football", "Bowling",
			"Skating", "Tennis", "Frisbee"
		};
	
	GlobalState gs;
	

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gs = (GlobalState) getApplication();
		gs.setActivityTypes(activityTypes);
		gs.activities = new HashMap<String, CornellActivity>();
		userActivities = new LinkedList<String>();
		Typeface font = Typeface.createFromAsset(getAssets(), "ClementePDae-Light.ttf");
		gs.setFont( font );
		

		boolean deleted = false;
		deleteFile( configfile );
		if( deleted )
		{
			System.out.println( "deleted \n");
		}
		
		try {
			BufferedReader settingsInput = new BufferedReader(new InputStreamReader(openFileInput(configfile), "UTF-8"));
			String line;
			
			while ((line = settingsInput.readLine()) != null)
			{
				userActivities.add(line);
			}
			gs.setUserActivities(userActivities);
			ActivityLoader loader = new ActivityLoader();
			loader.execute();
			setContentView(R.layout.activity_main);
			setupMainButtons();
			
		} 
		
		
		catch (FileNotFoundException e)
		{
			// have the user set up their initial settings
			setContentView(R.layout.activity_settings);
			Button finish = (Button) findViewById(R.id.continuebutton);
			finish.setTypeface(font);
			final TextView initialQuestion = (TextView) findViewById(R.id.textView1);
			initialQuestion.setTypeface(font);
			
			final TextView chooseMore = (TextView) findViewById(R.id.textView2);
			chooseMore.setTypeface( font );
			
			final ListView settingsListView = (ListView) findViewById(R.id.list);
			final ArrayList<Setting> settings = new ArrayList<Setting>();
			for (int i = 0; i < activityTypes.length; i++)
			{
				settings.add(new Setting(activityTypes[i]));
			}
			final SettingArrayAdapter adapter = new SettingArrayAdapter(this, 
					settings);
			settingsListView.setAdapter(adapter);
			
			final Button continueButton = (Button) findViewById(R.id.continuebutton);
			
			continueButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					userActivities = new LinkedList<String>();
					for (int i = 0; i < adapter.getCount(); i++)
					{
						if (adapter.getItem(i).checked)
						{
							Setting s = adapter.getItem(i);
							userActivities.add(s.name);
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
					ActivityLoader loader = new ActivityLoader();
					loader.execute();
					setupMainButtons();
					
				}
				
			});
			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//Intent intent = new Intent(this, TrackingService.class);
		//startService(intent);
	}
	
	protected void setupMainButtons() {
		final Button historyButton = (Button) findViewById(R.id.historybutton);
		final Intent historyIntent = new Intent(this, HistoryActivity.class);
		historyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(historyIntent);
			}
			
		});
		historyButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Button mapButton = (Button) v.findViewById(R.id.historybutton);
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
		
		final Button listButton = (Button) findViewById(R.id.listbutton);
		final Intent listIntent = new Intent(this, ListViewActivity.class);
		
		listButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(listIntent);
			}
			
		});
		
		listButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Button mapButton = (Button) v.findViewById(R.id.listbutton);
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
		
		final Button settingsButton = (Button) findViewById(R.id.settingsbutton);
		settingsButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				changePreferences();
			}
		});
		settingsButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Button mapButton = (Button) v.findViewById(R.id.settingsbutton);
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
		
		final Intent mapIntent = new Intent(this, MapViewActivity.class);
		final Button mapButton = (Button) findViewById(R.id.mapbutton);
		mapButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				mapIntent.putExtra("Activity", "MainActivity");
				startActivity(mapIntent);
			}
		});
		
		mapButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Button mapButton = (Button) v.findViewById(R.id.mapbutton);
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
	}

	//Show the Map View
	public void showMap( View view )
	{
		Intent mapView = new Intent( MainActivity.this, MapViewActivity.class );
		startActivity( mapView );
		return;
	}
	
	//Show the Activity Preferences View
	public void changePreferences()
	{
		Intent settingsView = new Intent( MainActivity.this, ChangePreferences.class );
		startActivity( settingsView );
		
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
		// Handle action bar item clicks here. The action bar willactivit
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
	
	
	
	static public class Setting {
		public String name;
		public boolean checked;
		
		public Setting(String n)
		{
			name = n;
			checked = false;
		}
		
	}
	
	public class ActivityLoader extends AsyncTask<String, String, String> {

		GlobalState gs; 
		
		@Override
		protected String doInBackground(String... arg0) {
			String response = "";
			try {
				System.out.println( "Attempting to run background task \n");
				gs = (GlobalState) getApplication();
				String serverString = gs.getServer() + "getactivities"; 
				String queryString = serverString + '?';
				String line;
				userActivities = gs.getUserActivities();
				Iterator<String> iter = userActivities.iterator();
				while (iter.hasNext())
				{
					queryString = queryString + iter.next() + '&';
				}
				synchronized (gs.activities) {
					URL url = new URL(serverString);
					System.out.println( "URL: " + queryString);
					URLConnection connection = url.openConnection();
				
					BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					while (null != ((line = input.readLine())))
					{
						response = response + line; 
					}
					gs.setActivities(new JSONArray(response));
				}
			} catch (Exception e) 
			{
				System.out.println( "Error with background task \n");
				e.printStackTrace();
			}
			return response;
		}
		
//		@Override
//		protected void onPostExecute(String obj) {
//			try {
//				//gs.setActivities(new JSONArray(obj));
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
	  }
	 

}
