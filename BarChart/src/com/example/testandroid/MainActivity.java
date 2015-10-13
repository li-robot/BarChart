package com.example.testandroid;

import java.util.ArrayList;

import com.example.testandroid.BarChart.OnBarSelectedListener;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ArrayList<BarData> barDatas = new ArrayList<BarData>();

		BarChart barChart = new BarChart(this);
		 
		BarData barData  = new BarData("1月", 10);
		BarData barData1 = new BarData("2月", 3);
		BarData barData2 = new BarData("3月", 40);
		BarData barData3 = new BarData("4月", 33);
		BarData barData4 = new BarData("5月", 80);

		BarData barData5 = new BarData("6月", 50);
		BarData barData6 = new BarData("7月", 78);
		BarData barData7 = new BarData("8月", 64);
		BarData barData8 = new BarData("9月", 35);
		

		barDatas.add(barData);
		barDatas.add(barData1);
		barDatas.add(barData2);
		barDatas.add(barData3);
		barDatas.add(barData4);
		
		barDatas.add(barData);
		barDatas.add(barData1);
		barDatas.add(barData2);
		barDatas.add(barData3);
		barDatas.add(barData4);
		
		barDatas.add(barData);
		barDatas.add(barData1);
		barDatas.add(barData2);
		barDatas.add(barData3);
		barDatas.add(barData4);
		
		barDatas.add(barData);
		barDatas.add(barData1);
		barDatas.add(barData2);
		barDatas.add(barData3);
		barDatas.add(barData4);
		
		barDatas.add(barData);
		barDatas.add(barData1);
		barDatas.add(barData2);
		barDatas.add(barData3);
		barDatas.add(barData4);
		
		barDatas.add(barData);
		barDatas.add(barData1);
		barDatas.add(barData2);
		barDatas.add(barData3);
		barDatas.add(barData4);

		barDatas.add(barData5);
		barDatas.add(barData6);
		barDatas.add(barData7);
		barDatas.add(barData8);
		
		barChart.setData(barDatas);
		barChart.setMaxValue(60);
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
		setContentView(barChart);
		
		
	}
	
}
