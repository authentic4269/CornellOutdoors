package com.example.cornelloutdoors;

import java.util.ArrayList;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cornelloutdoors.ListViewActivity.CornellActivityCompare;
import com.example.cornelloutdoors.MainActivity.Setting;

public class ActivityArrayAdapter extends ArrayAdapter<CornellActivity> {
	  private final Activity context;
	  private ArrayList<CornellActivity> displayList;
	  private ArrayList<CornellActivity> dataList;
	  private CornellActivityCompare comparator;
	  protected LocationManager locationManager;
	  
	   public ActivityArrayAdapter(Activity context, ArrayList<CornellActivity> values, CornellActivityCompare comparator, LocationManager locationManager)
	   {
		   super(context, R.layout.cornellactivity_row, values);
		   this.context = context;
		   this.locationManager = locationManager;
		   this.displayList = values;
		   this.dataList = new ArrayList<CornellActivity>();
		   for (CornellActivity a : values)
		   {
			   dataList.add(a);
		   }
	   }
	   
	   static class ViewHolder {
		   protected TextView name;
		   protected RatingBar rating;
		   protected TextView type;
		   protected TextView cost;
		   protected TextView description;
		   protected TextView hours;
		protected Button mapbutton;
	   }
	   
	   @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		    View view = null;
		    if (convertView == null) {
		      LayoutInflater inflator = context.getLayoutInflater();
		      view = inflator.inflate(R.layout.cornellactivity_row, null);
		      CornellActivity thisactivity = displayList.get(position);
		      final ViewHolder viewHolder = new ViewHolder();
		      viewHolder.name = (TextView) view.findViewById(R.id.name);
		      viewHolder.type = (TextView) view.findViewById(R.id.type);
		      viewHolder.cost = (TextView) view.findViewById(R.id.cost);
		      viewHolder.description = (TextView) view.findViewById(R.id.description);
		      viewHolder.hours = (TextView) view.findViewById(R.id.hours);
		      viewHolder.rating = (RatingBar) view.findViewById(R.id.rating);
		      viewHolder.mapbutton = (Button) view.findViewById(R.id.map);
		      
		      viewHolder.hours.setText("Hours: " + thisactivity.hours);
		      viewHolder.description.setText(thisactivity.description);
		      viewHolder.name.setText(thisactivity.name);
		      viewHolder.cost.setText("Cost: " + thisactivity.cost);
		      viewHolder.type.setText("Type: " + thisactivity.type);
		      
		      Location loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
				if (loc != null)
				{
					double mylong = loc.getLongitude();
					double mylat = loc.getLatitude();
			        viewHolder.mapbutton.setText(ListViewActivity.distance(thisactivity.longitude, thisactivity.latitude, mylong, mylat) + " Miles");
				}
				
		      
		      view.setTag(viewHolder);
		    } else {
		      view = convertView;
		    }
		    return view;
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
