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
        
        setTitle("Activity Information");
        
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
			place.setTypeface(gs.getFont());
			hours.setText("Hours: " + curActivity.hours);
			hours.setTypeface(gs.getFont());
			cost.setText("Cost: " + curActivity.cost);
			cost.setTypeface(gs.getFont());
			description.setText("Description:\n" + curActivity.description);
			description.setTypeface(gs.getFont());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
