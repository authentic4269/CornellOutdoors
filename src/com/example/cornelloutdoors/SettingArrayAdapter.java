package com.example.cornelloutdoors;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.cornelloutdoors.MainActivity.Setting;

public class SettingArrayAdapter extends ArrayAdapter<Setting> {
	  private final Activity context;
	  private ArrayList<Setting> list;
	  
	   public SettingArrayAdapter(Activity context, ArrayList<Setting> values)
	   {
		   super(context, R.layout.setting_row, values);
		   this.context = context;
		   this.list = values;
	   }
	   
	   static class ViewHolder {
		   protected CheckBox checkbox;
	   }
	   
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
		              element.checked = buttonView.isChecked();

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
		    holder.checkbox.setChecked(list.get(position).checked);
		    return view;
	   }


}
