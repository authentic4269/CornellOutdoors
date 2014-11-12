package com.example.cornelloutdoors;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import com.example.cornelloutdoors.MainActivity.Setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class changepreferences extends ActionBarActivity{

		GlobalState gs;
		protected void onCreate( Bundle savedInstanceSpace )
		{
			super.onCreate( savedInstanceSpace );
			setContentView( R.layout.activity_settings );
			gs = (GlobalState) getApplication();
			final LinkedList<String> userActivities;
			synchronized (gs.activities) {
				userActivities = gs.getUserActivities();
			}
			final String configfile = gs.configfile;
			final String[] activityTypes = gs.activityTypes;
			
			final ListView settingsListView = (ListView) findViewById(R.id.list);
			final ArrayList<Setting> settings = new ArrayList<Setting>();
			final ArrayList<Integer> currentlyCheckedIndices = new ArrayList<Integer>();
			for( int i = 0; i < activityTypes.length; i++ )
			{
				settings.add( new Setting( activityTypes[i] ));
				if( userActivities.contains(activityTypes[i]))
				{
					currentlyCheckedIndices.add((Integer)i);
				}
			}
			
			final SettingArrayAdapter adapter = new SettingArrayAdapter(this, settings);
			//Precheck the current activities
			for( int i = 0; i < currentlyCheckedIndices.size(); i++ )
			{
				adapter.getItem( currentlyCheckedIndices.get(i)).checked = true;
			}
			
			settingsListView.setAdapter( adapter );
			final Button nextButton = ( Button ) findViewById( R.id.continuebutton );
			nextButton.setOnClickListener( new OnClickListener() {
				
				public void onClick(View arg0){
					userActivities.clear();
					for(int i = 0; i < adapter.getCount(); i++)
					{
						if (adapter.getItem(i).checked)
						{
							userActivities.add(adapter.getItem(i).name);
						}
					}
					gs.setUserActivities(userActivities);
					try
					{
						FileOutputStream fos = openFileOutput( configfile, Context.MODE_PRIVATE );
						for(int i = 0; i < userActivities.size(); i++)
						{
							fos.write((userActivities.get(i) + '\n').getBytes());
						}
						fos.close();
					} catch (Exception e){
						e.printStackTrace();
					}
					finish();
				}
			});
			
		}
}
