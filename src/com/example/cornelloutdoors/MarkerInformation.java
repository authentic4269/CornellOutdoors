package com.example.cornelloutdoors;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
			Button takeme = (Button) findViewById(R.id.takemethere);
			
			
			place.setText(curActivity.name);
			place.setTypeface(gs.getFont());
			hours.setText(curActivity.hours);
			hours.setTypeface(gs.getFont());
			cost.setText(curActivity.cost);
			cost.setTypeface(gs.getFont());
			description.setText(curActivity.description);
			description.setTypeface(gs.getFont());
			
			takeme.setOnTouchListener( new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Button takeme = (Button) v.findViewById(R.id.takemethere);
					if( event.getAction() == MotionEvent.ACTION_DOWN )
					{
						takeme.setBackgroundResource(R.drawable.darkblue);
					}
					else if( event.getAction() == MotionEvent.ACTION_UP )
					{
						takeme.setBackgroundResource(R.drawable.lightblue);
					}
					return false;
				}
			} );
			takeme.setTypeface(gs.getFont());
			takeme.setTextColor(Color.WHITE);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void showMap(View view)
	{
		LocationManager lM = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Location loc = lM.getLastKnownLocation(lM.NETWORK_PROVIDER);
		double mylong = loc.getLongitude();
		double mylat = loc.getLatitude();
		final Intent directionIntent = new Intent(android.content.Intent.ACTION_VIEW, 
			    Uri.parse("http://maps.google.com/maps?saddr=" + mylat + "," + mylong + 
			    		"&daddr=" + curActivity.latitude + "," + curActivity.longitude));
		directionIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		startActivity(directionIntent);
	}
}
