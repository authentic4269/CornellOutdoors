package com.example.cornelloutdoors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.example.cornelloutdoors.ListViewActivity.CornellActivityCompare;
import com.example.cornelloutdoors.ListViewActivity.MapDisplay;
import com.example.cornelloutdoors.MainActivity.Setting;

public class ActivityArrayAdapter extends ArrayAdapter<CornellActivity> {
	  private final Activity context;
	  private ArrayList<CornellActivity> displayList;
	  private ArrayList<CornellActivity> dataList;
	  private CornellActivityCompare comparator;
	  private final MapDisplay map;
	  protected LocationManager locationManager;
	  private GlobalState gs;
	  private String uid;
	  
	   public ActivityArrayAdapter(Activity context, ArrayList<CornellActivity> values,
			   CornellActivityCompare comparator, LocationManager locationManager, MapDisplay mapDisplay, String uid)
	   {
		   super(context, R.layout.cornellactivity_row, values);
		   this.context = context;
		   this.uid = uid;
		   this.map = mapDisplay;
		   this.locationManager = locationManager;
		   this.displayList = values;
		   this.dataList = new ArrayList<CornellActivity>();
		   this.gs = (GlobalState) context.getApplication();
		   for (CornellActivity a : values)
		   {
			   dataList.add(a);
		   }
	   }
	   
	   class ViewHolder {
		   protected TextView name;
		   protected RatingBar rating;
		   protected TextView type;
		   protected TextView cost;
		   protected TextView hours;
		   protected TextView distance;
		   protected Button mapbutton;
	   }
	   
	   @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		    View view = null;
		    if (convertView == null) {
		      LayoutInflater inflator = context.getLayoutInflater();
		      view = inflator.inflate(R.layout.cornellactivity_row, null);
		      final CornellActivity thisactivity = displayList.get(position);
		      final ViewHolder viewHolder = new ViewHolder();
		      
		      viewHolder.type = (TextView) view.findViewById(R.id.type);
		      viewHolder.name = (TextView) view.findViewById(R.id.name);
		      viewHolder.cost = (TextView) view.findViewById(R.id.cost);
		      viewHolder.hours = (TextView) view.findViewById(R.id.hours);
		      viewHolder.rating = (RatingBar) view.findViewById(R.id.rating);
		      viewHolder.mapbutton = (Button) view.findViewById(R.id.map);
		      viewHolder.distance = (TextView) view.findViewById(R.id.distance);
		      
		      viewHolder.type.setText(thisactivity.type + ",");
		      viewHolder.hours.setText(thisactivity.hours);
		      viewHolder.name.setText(thisactivity.name);
		      viewHolder.cost.setText(thisactivity.cost);
		      if (thisactivity.rating != null)
		    	  viewHolder.rating.setRating(thisactivity.rating);
		      else
		    	  viewHolder.rating.setRating((float) 0.0);
		      
		      viewHolder.rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar arg0, float arg1,
							boolean arg2) {
						thisactivity.rating = arg1;
						RatingsLoader ratingUpdate = new RatingsLoader();
						ratingUpdate.execute(new Rating(thisactivity.name, arg1));
					}
			    	  
			      });
		      viewHolder.hours.setTypeface(gs.getFont());
		      viewHolder.name.setTypeface(gs.getFont());
		      viewHolder.cost.setTypeface(gs.getFont());
		      viewHolder.type.setTypeface(gs.getFont());
		      
		      Location loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
				if (loc != null)
				{
					double mylong = loc.getLongitude();
					double mylat = loc.getLatitude();
			        viewHolder.distance.setText(ListViewActivity.distance(thisactivity.longitude, thisactivity.latitude, mylong, mylat) + " mi");
			        viewHolder.distance.setTypeface(gs.getFont());
					viewHolder.mapbutton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							map.showMap(thisactivity);
							
						}
						
					});
				}
		      view.setTag(viewHolder);
		      return view;
		    } else {
			      final CornellActivity thisactivity = displayList.get(position);
			      final ViewHolder viewHolder = new ViewHolder();
			      TextView name = (TextView) convertView.findViewById(R.id.name);
			      TextView cost = (TextView) convertView.findViewById(R.id.cost);
			      TextView type = (TextView) convertView.findViewById(R.id.type);
			      TextView hours = (TextView) convertView.findViewById(R.id.hours);
			      RatingBar rating = (RatingBar) convertView.findViewById(R.id.rating);
			      Button mapbutton = (Button) convertView.findViewById(R.id.map);
			      viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
			      
			      type.setText(thisactivity.type + ",");
			      name.setText(thisactivity.name);
			      hours.setText(thisactivity.hours);
			      cost.setText(thisactivity.cost);
			      
			      if (thisactivity.rating != null)
			    	  rating.setRating(thisactivity.rating);
			      else 
			    	  rating.setRating((float)0.0);
			      rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar arg0, float arg1,
							boolean arg2) {
						thisactivity.rating = arg1;
						RatingsLoader ratingUpdate = new RatingsLoader();
						ratingUpdate.execute(new Rating(thisactivity.name, arg1));
					}
			    	  
			      });
			      Location loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
					if (loc != null)
					{
						double mylong = loc.getLongitude();
						double mylat = loc.getLatitude();
				        viewHolder.distance.setText(ListViewActivity.distance(thisactivity.longitude, thisactivity.latitude, mylong, mylat) + " mi");
				        viewHolder.distance.setTypeface( gs.getFont() );
						mapbutton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								map.showMap(thisactivity);
								
							}
							
						});
					}
					
					convertView.setTag(viewHolder);
					return convertView;
		    }
		   
	   }
	   
	   protected class Rating {
		   float rating; 
		   String activity;
		   
		   public Rating(String a, float r)
		   {
			   rating = r;
			   activity = a;
		   }
	   }
	   
		public class RatingsLoader extends AsyncTask<Rating, String, String> {

			@Override
			protected String doInBackground(Rating... params) {
				String response = "";
				try {
					System.out.println( "Attempting to run background task \n");

										
					String queryString = "user_id=" + uid + "&rating=" + params[0].rating + "&activity=" + params[0].activity;
					String line;
					
					URL url = new URL(gs.getServer() + "addrating?" + queryString.replace(" ", "%20"));
					URLConnection connection = url.openConnection();
					BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					while (null != ((line = input.readLine())))
					{
						response = response + line; 
					}
					
				} catch (Exception e) 
				{
					System.out.println( "Error with background task \n");
					e.printStackTrace();
				}
				return response;
			}
			
		}

	public void filter(String t) {
		if (t.length() != 0)
		{
			displayList.clear();
			for (CornellActivity a : dataList)
			{
				if (a.name.toLowerCase().contains(t) || a.description.toLowerCase().contains(t) || a.type.toLowerCase().contains(t))
				{
					displayList.add(a);
				}
			}
			
		}
		else
		{
			displayList.clear();
			for (CornellActivity a : dataList)
			{
				displayList.add(a);
			}
		}
		notifyDataSetChanged();
	}


}
