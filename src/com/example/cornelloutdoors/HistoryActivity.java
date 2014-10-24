package com.example.cornelloutdoors;

import android.app.Activity;
import android.os.Bundle;

import com.androidplot.xy.*;

public class HistoryActivity extends Activity {
	

private XYPlot stepsPlot = null;
private SimpleXYSeries stepsSeries = null;
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
	}
}
