package com.example.cornelloutdoors;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MarkerInformation extends ActionBarActivity {
	GlobalState gs;
	public HashMap<String, CornellActivity> markers;
	CornellActivity curActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_information);
        String markerInfo = getIntent().getStringExtra("info");
        gs = (GlobalState) getApplication();
        synchronized( gs.activities )
        {
        	markers = gs.getActivities();
        }
        
        curActivity = markers.get( markerInfo );

        displayInformation();
    }
	
	public void displayInformation()
	{
		try {
			TextView place = (TextView) findViewById(R.id.place);
			TextView hours = (TextView) findViewById(R.id.hours);
			TextView cost = (TextView) findViewById(R.id.cost);
			TextView description = (TextView) findViewById(R.id.description);
		
			place.setText(curActivity.name);
			hours.setText(curActivity.hours);
			cost.setText(curActivity.cost);
			description.setText(curActivity.description);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
