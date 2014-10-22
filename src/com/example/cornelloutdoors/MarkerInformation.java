package com.example.cornelloutdoors;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MarkerInformation extends ActionBarActivity {
	private JSONObject info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_information);
        String markerInfo = getIntent().getStringExtra("info");
        try {
			info = new JSONObject( markerInfo );
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        displayInformation();
    }
	
	public void displayInformation()
	{
		try {
			TextView place = (TextView) findViewById(R.id.place);
			TextView hours = (TextView) findViewById(R.id.hours);
			TextView cost = (TextView) findViewById(R.id.cost);
			TextView description = (TextView) findViewById(R.id.description);
		
			place.setText(info.getString("name"));
			hours.setText(info.getString("hours"));
			cost.setText(info.getString("cost"));
			description.setText(info.getString("description"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
