package com.example.cornelloutdoors;

import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Application;

public class GlobalState extends Application{
	public LinkedList<String> userActivities;
	
	
	public String serverString = "http://sleepy-wave-3087.herokuapp.com/";
	private String configfile = "cornelloutdoorsconfig";
	
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
	
	

}
