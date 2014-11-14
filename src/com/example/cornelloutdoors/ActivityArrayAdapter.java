package com.example.cornelloutdoors;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
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
	  
	   public ActivityArrayAdapter(Activity context, ArrayList<CornellActivity> values,
			   CornellActivityCompare comparator, LocationManager locationManager, MapDisplay mapDisplay)
	   {
		   super(context, R.layout.cornellactivity_row, values);
		   this.context = context;
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
			      rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar arg0, float arg1,
							boolean arg2) {
						thisactivity.rating = arg1;
						
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

	public void filter(String t) {
		if (t.length() != 0)
		{
			displayList.clear();
			for (CornellActivity a : dataList)
			{
				if (a.name.contains(t) || a.description.contains(t) || a.type.contains(t))
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
