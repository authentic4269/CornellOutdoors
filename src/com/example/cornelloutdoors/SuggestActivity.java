package com.example.cornelloutdoors;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SuggestActivity extends ActionBarActivity {
	LatLng location;
	ArrayList<String> activities;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_activity);
        
        double lat = getIntent().getDoubleExtra("lat", 0.0);
        double lon = getIntent().getDoubleExtra("lon", 0.0);
        location = new LatLng( lat, lon );
        ArrayList<String> activities = (ArrayList<String>) getIntent().getSerializableExtra("activities");
        
        //Populate Spinner
        Spinner spinner = (Spinner) findViewById(R.id.sa_activity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, activities);
        spinner.setAdapter( adapter );
        
    }
	
	public void entered(View view)
	{
		finish();
	}

}
