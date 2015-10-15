package com.example.testandroid;

import java.util.ArrayList;

import com.example.testandroid.BarChart.OnBarSelectedListener;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		//------------------------------------------
		
		final ArrayList<BarData> testBarDatas = new ArrayList<BarData>();
		
		BarData barData001  = new BarData("a", 10);
		BarData barData002 = new BarData("b", 3);
		BarData barData003 = new BarData("c", 40);
		
		testBarDatas.add(barData001);
		testBarDatas.add(barData002);
		testBarDatas.add(barData003);
		
		//------------------------------------------
		
		
		RelativeLayout layout = new RelativeLayout(this);
		
		ArrayList<BarData> barDatas = new ArrayList<BarData>();

		final BarChart barChart = new BarChart(this);
		 
		BarData barData  = new BarData("0", 10);
		BarData barData1 = new BarData("1", 3);
		BarData barData2 = new BarData("2", 20);
		BarData barData3 = new BarData("3", 10);
		BarData barData4 = new BarData("4", 35);
		
		barDatas.add(barData);
		barDatas.add(barData1);
		barDatas.add(barData2);
		barDatas.add(barData3);
		barDatas.add(barData4);
		
		barChart.setData(barDatas);
		barChart.setMaxValue(30);
		barChart.setStep(10);
		barChart.setMinValue(0);
		
		barChart.setBarWidth(60);
		barChart.setXAxisMarkColor(Color.RED);
		
		barChart.setOnBarSelectedListener(new OnBarSelectedListener() {
			@Override
			public void onBarSelected(int index, int value) {
				Toast.makeText(MainActivity.this, " " + index + " " + value , Toast.LENGTH_LONG).show();
			}
		});
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		barChart.setLayoutParams(params);
		layout.addView(barChart);
		
		
		Button btn = new Button(this);
		layout.addView(btn);
		btn.setVisibility(View.GONE);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ArrayList<BarData> testBarDatas = new ArrayList<BarData>();
				
				BarData barData001  = new BarData("a", 10);
				BarData barData002 = new BarData("b", 3);
				BarData barData003 = new BarData("c", 40);
				
				testBarDatas.add(barData001);
				testBarDatas.add(barData002);
				testBarDatas.add(barData003);
				barChart.updataData(testBarDatas);
			}
		});
		
		setContentView(layout);
		
		
	}
	
}
