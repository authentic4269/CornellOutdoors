package com.example.cornelloutdoors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.cornelloutdoors.MainActivity.ActivityLoader;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SuggestActivity extends ActionBarActivity {
	LatLng location;
	ArrayList<String> activities;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_activity);
        GlobalState gs = (GlobalState) getApplication();
        ArrayList<String> activities = (ArrayList<String>) getIntent().getSerializableExtra("activities");
        final Double lat = (Double) getIntent().getDoubleExtra("lat", 0.0);
        final Double lon = (Double) getIntent().getDoubleExtra("lon", 0.0);
        
        //Set Text
        final TextView suggest = (TextView) findViewById(R.id.sa);
		suggest.setTypeface(gs.getFont());
		
		final EditText name = (EditText) findViewById(R.id.sa_name);
		name.setTypeface(gs.getFont());
		
		final EditText hours = (EditText) findViewById(R.id.sa_hours);
		hours.setTypeface(gs.getFont());
		
		final EditText description = (EditText) findViewById(R.id.sa_description);
		description.setTypeface(gs.getFont());
		
		final EditText cost = (EditText) findViewById(R.id.sa_cost);
		cost.setTypeface(gs.getFont());
		
		final Button enter = (Button) findViewById(R.id.sa_enter);
		enter.setTypeface(gs.getFont());
		

		
        //Populate Spinner
        final Spinner spinner = (Spinner) findViewById(R.id.sa_type);
        List<String> typeList = new ArrayList<String>();
        Iterator<String> iter = gs.userActivities.iterator();
        while (iter.hasNext())
        	 typeList.add(iter.next());
        ArrayAdapter<String> typeAdapter = 
        		new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
        spinner.setAdapter( typeAdapter );
        
		enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				CornellActivity a = new CornellActivity();
				a.name = name.getText().toString();
				a.hours = hours.getText().toString();
				a.description = description.getText().toString();
				a.cost = cost.getText().toString();
				a.type = spinner.getSelectedItem().toString();
				a.longitude = lon;
				a.latitude = lat;
				SubmitActivity loader = new SubmitActivity();
				loader.execute(a);
				Intent listView = new Intent( SuggestActivity.this, ListViewActivity.class );
				startActivity( listView );
			}
			
		});
    }
	
	public void entered(View view)
	{
	}
	
	public class SubmitActivity extends AsyncTask<CornellActivity, Void, Void> {

		@Override
		protected Void doInBackground(CornellActivity... arg0) {
			try {
				System.out.println( "Attempting to run background task \n");
				String response = "";
				String line;
				GlobalState gs = (GlobalState) getApplication();
				String serverString = gs.getServer() + "addactivity?";
				CornellActivity newActivity = arg0[0];
				if (arg0.length == 0)
					return null;
				serverString = serverString + "name=" + newActivity.name;
				serverString = serverString + "&cost=" + newActivity.cost;
				serverString = serverString + "&description=" + newActivity.description;
				serverString = serverString + "&hours=" + newActivity.hours;
				serverString = serverString + "&type=" + newActivity.type;
				serverString = serverString + "&latitude=" + newActivity.latitude;
				serverString = serverString + "&longitude=" + newActivity.longitude;
				System.out.println("Server String: " + serverString);
				URL url = new URL(serverString);
				URLConnection connection = url.openConnection();
				BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while (null != ((line = input.readLine())))
				{
					response = response + line; 
				}
				System.out.println("Server Response:" + response);

			} catch (Exception e) 
			{
				System.out.println( "Error with background task \n");
				e.printStackTrace();
			}
			return null;
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
