package com.example.cornelloutdoors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.cornelloutdoors.MainActivity.PlaceholderFragment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;


public class TrackingService extends Service {
	
	String datafile = "cornelloutdoorsdata";
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}
	
	public void onCreate() {
		Timer t = new Timer();
		t.schedule(new UpdateLocationsTask(), 0, 1000*60*5);
		t.schedule(new UpdateLocationsTask(), 0, 1000*60*60);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private class UpdateLocationsTask extends TimerTask {

		@Override
		public void run() {
			synchronized (datafile) {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location loc = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(datafile, true));	
				out.write(loc.getLatitude() + "," + loc.getLongitude() + "," + System.currentTimeMillis());
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
	}
}
