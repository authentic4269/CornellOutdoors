package com.example.cornelloutdoors;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.cornelloutdoors.MainActivity.Setting;

public class SettingArrayAdapter extends ArrayAdapter<Setting> {
	  private final Activity context;
	  private ArrayList<Setting> list;
	  AssetManager mngr;
	  
	   public SettingArrayAdapter(Activity context, ArrayList<Setting> values)
	   {
		   super(context, R.layout.setting_row, values);
		   this.context = context;
		   this.list = values;
		   mngr = context.getAssets();
	   }
	   
	   static class ViewHolder {
		   protected CheckBox checkbox;
	   }
	   
	   @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		    View view = null;
		    if (convertView == null) {
		      LayoutInflater inflator = context.getLayoutInflater();
		      view = inflator.inflate(R.layout.setting_row, null);
		      final ViewHolder viewHolder = new ViewHolder();
		      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
		      
		      viewHolder.checkbox
		          .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

		            @Override
		            public void onCheckedChanged(CompoundButton buttonView,
		                boolean isChecked) {
		              Setting element = (Setting) viewHolder.checkbox
		                  .getTag();
		              element.checked = isChecked;
		              
		              if( isChecked )
		              {
		            	  buttonView.setBackgroundResource(R.drawable.lightblue);  
		              }
		              else{
		            	  buttonView.setBackgroundResource(R.drawable.darkblue);
		              }
		            }
		          });
		      view.setTag(viewHolder);
		      viewHolder.checkbox.setTag(list.get(position));
		    } else {
		      view = convertView;
		      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		    }
		    ViewHolder holder = (ViewHolder) view.getTag();
		    holder.checkbox.setText(list.get(position).name);
		    holder.checkbox.setTextColor(Color.WHITE);
			Typeface font = Typeface.createFromAsset(mngr, "ClementePDae-Light.ttf");
			holder.checkbox.setTypeface(font);
		    holder.checkbox.setBackgroundResource(R.drawable.lightblue);
		    holder.checkbox.setChecked(list.get(position).checked);
		    
		    
			if( list.get(position).checked )
			{
				holder.checkbox.setBackgroundResource(R.drawable.darkblue);
			}
			else
			{
				holder.checkbox.setBackgroundResource(R.drawable.lightblue);
			}
		    holder.checkbox.setOnClickListener( new View.OnClickListener(){
		    	@Override
		    	public void onClick(View arg0){
		    		CheckBox checkbox = (CheckBox) arg0;
		    		if( checkbox.isChecked() )
		    		{
		    			checkbox.setBackgroundResource(R.drawable.darkblue);
		    		}
		    		else
		    		{
		    			checkbox.setBackgroundResource(R.drawable.lightblue);
		    		}
		    		
		    	}
		    });
		
		    
		    return view;
	   }


}
