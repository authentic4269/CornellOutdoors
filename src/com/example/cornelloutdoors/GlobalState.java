package com.example.cornelloutdoors;

import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Application;
import android.graphics.Typeface;

public class GlobalState extends Application{
	// activities really should be a linked list
	public HashMap<String, CornellActivity> activities;
	public LinkedList<String> userActivities;
	
	public String[] activityTypes;
	public Typeface font;
	
	
	public String serverString = "https://sleepy-wave-3087.herokuapp.com/";
	public String configfile = "cornelloutdoorsconfig";
	
	public String getServer()
	{
		return serverString;
	}
	
	public LinkedList<String> getUserActivities()
	{
		return userActivities;
	}
	
	public void setUserActivities(LinkedList<String> userActivities)
	{
		this.userActivities = userActivities;
		return;
	}
	
	public void setActivityTypes(String[] activityTypes)
	{
		this.activityTypes = activityTypes;
		return;
	}

	public void setActivities(JSONArray locationsArray) {
		JSONObject m;
		this.activities = new HashMap<String, CornellActivity>();
		try {
			for (int i = 0; i < locationsArray.length(); i++)
			{
				CornellActivity newactivity = new CornellActivity();
				m = locationsArray.getJSONObject(i);
				newactivity.latitude = Double.parseDouble(m.getString("latitude"));
				newactivity.longitude = Double.parseDouble(m.getString("longitude"));
				newactivity.name = m.getString("name");
				newactivity.cost = m.getString("cost");
				newactivity.hours = m.getString("hours");
				newactivity.type = m.getString("type");
				newactivity.description = m.getString("description");
				this.activities.put(newactivity.name, newactivity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<String, CornellActivity> getActivities()
	{
		return this.activities;
	}
	
	public void setFont( Typeface font )
	{
		this.font = font;
	}
	
	public Typeface getFont()
	{
		return font;
	}

	public void setRatings(JSONArray jsonArray) {
		JSONObject m;
		try {
			synchronized (this.activities) {
				for (int i = 0; i < jsonArray.length(); i++)
				{
					m = jsonArray.getJSONObject(i);
					if (this.activities.containsKey(m.getString("activity"))) {
						CornellActivity a = this.activities.get(m.getString("activity"));
						a.rating = Float.parseFloat(m.getString("rating"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
