package com.example.testandroid;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

public class BarChart extends View {

	public static final String TAG = "LYH";

	/**
	 * the height of the xAxis
	 */
	private int xAxisHeight = 50;

	private int maxValue = 400;
	private int minValue = 0;
	private int step = 50;

	private int barWidth = 90;
	private int barSpace = 100;

	private int yAxisWidth = 80;

	// ----------- bar setting ------------
	public void setBarWidth(int barWidth) {
		this.barWidth = barWidth;
	}

	public void setBarSpace(int barSpace) {
		this.barSpace = barSpace;
	}

	private int barColor = Color.RED;

	public void setBarColor(int barColor) {
		this.barColor = barColor;
	}

	private int barBg = Color.BLACK;

	public void setBarBg(int barBg) {
		this.barBg = barBg;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public void setStep(int step) {
		this.step = step;
	}

	// --------- y axis setting --------
	public void setYAxisWidth(int width) {
		this.yAxisWidth = width;
	}

	private int yAxisMarkColor = Color.BLACK;

	public void setYMarkColor(int color) {
		this.yAxisMarkColor = color;
	}

	private int yAxisMarkSize = DensityUtil.dip2px(getContext(), 15);

	public void setyAxisMarkSize(int markSize) {
		this.yAxisMarkSize = markSize;
	}

	private int yAxisBgColor = Color.YELLOW;

	public void setYAxisBgColor(int color) {
		this.yAxisBgColor = color;
	}

	// ---------- x axis setting --------
	public void setXAxisHeight(int height) {
		this.xAxisHeight = height;
	}

	private int xAxisMarkColor = Color.BLACK;

	public void setXAxisMarkColor(int markColor) {
		this.xAxisMarkColor = markColor;
	}

	private int xAxisMarkSize = DensityUtil.dip2px(getContext(), 15);

	public void setXAxisMarkSize(int markSize) {
		this.xAxisMarkSize = markSize;
	}

	private int xAxisBgColor = Color.YELLOW;

	public void setXAxisBgColor(int color) {
		this.xAxisBgColor = color;
	}

	private int coordinateBg = Color.TRANSPARENT;

	public void setCoordinateBg(int bg) {
		this.coordinateBg = bg;
	}

	private ArrayList<Bar> bars = new ArrayList<Bar>();
	private ArrayList<BarData> barDatas = new ArrayList<BarData>();

	public BarChart(Context context) {
		super(context);
	}

	public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public BarChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	// avoid load twice
	private boolean isFirst = true;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if (isFirst) {
			init(barDatas);
			isFirst = false;
		}
		
		Log.i(TAG, "onLayout");

	}

	public void setData(ArrayList<BarData> barDatas) {
		this.barDatas = barDatas;
		Log.i(TAG, "setData");
	}

	public void init(ArrayList<BarData> barData) {

		int left = barSpace + barWidth;

		for (int i = 0; i < barData.size(); i++) {

			float barHeightF = getHeight() / maxValue * barData.get(i).yValue;

			int barHeight = (int) barHeightF;
			int bottomY = getHeight() - xAxisHeight;

			float ratio = getHeight() / maxValue;

			Bar bar = new Bar(left, bottomY - barHeight + (int) (ratio * minValue), (left + barWidth), bottomY);
			left += barSpace + barWidth;
			bars.add(bar);
		}
		
		this.invalidate();

	}

	@Override
	protected void onDraw(Canvas canvas) {

		// draw bg
		drawBg(canvas);

		// draw x axis
		drawXAxis(canvas);

		// draw y axis
		drawYAxis(canvas);

		// draw bars
		drawBars(canvas);

		// draw x axis lables
		drawXAxisLabel(canvas);

		// draw y axis labels
		drawYAxisLabel(canvas);

	}

	// ----------------------------------

	public void drawXAxis(Canvas canvas) {

		int xAxisLeft = 0;

		int xAxisTop = getHeight() - xAxisHeight;

		/**
		 * right & bottom
		 */
		int xAxisRight = getWidth();
		int xAxisBottom = getHeight();

		Rect rect = new Rect(xAxisLeft, xAxisTop, xAxisRight, xAxisBottom);

		/**
		 * the paint of the axis
		 */
		Paint xAxisPaint = new Paint();
		xAxisPaint.setColor(this.xAxisBgColor);
		xAxisPaint.setStyle(Style.FILL);

		canvas.drawRect(rect, xAxisPaint);

	}

	public void drawXAxisLabel(Canvas canvas) {

		for (int i = 0; i < bars.size(); i++) {
			Bar tmp = bars.get(i);
			Rect rect = new Rect(tmp.left + moveOffset, tmp.bottom, tmp.right + moveOffset, getHeight());

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStrokeWidth(3);

			paint.setTextSize(xAxisMarkSize);
			paint.setColor(xAxisMarkColor);

			drawTextInCenter(canvas, paint, rect, barDatas.get(i).xValue);
		}

	}

	public void drawYAxisLabel(Canvas canvas) {
		int range = maxValue - minValue;
		int labelCount = range / step;

		int stepValue = minValue;

		for (int i = 0; i <= labelCount; i++) {

			float barHeightF = getHeight() / maxValue * stepValue;
			int top = getHeight() - (int) barHeightF - xAxisHeight + getHeight() / maxValue * minValue;

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStrokeWidth(3);
			paint.setTextSize(yAxisMarkSize);
			paint.setColor(yAxisMarkColor);

			Rect yValueRect = new Rect(0, top - 25, yAxisWidth, top);
			drawTextInCenter(canvas, paint, yValueRect, stepValue + "");

			stepValue += step;
		}
	}

	public void drawYAxis(Canvas canvas) {

		int yAxisLeft = 0;
		int yAxisTop = 0;

		/**
		 * the width of the y axis
		 */

		int yAxisBottom = getHeight() - xAxisHeight;

		Rect rect = new Rect(yAxisLeft, yAxisTop, yAxisWidth, yAxisBottom);

		/**
		 * the paint of the y axis
		 */
		Paint yAxisPaint = new Paint();
		yAxisPaint.setColor(this.yAxisBgColor);

		canvas.drawRect(rect, yAxisPaint);
	}

	public void drawBars(Canvas canvas) {

		Paint barPaint = new Paint();
		barPaint.setColor(this.barColor);
		barPaint.setStyle(Style.FILL);

		/**
		 * bar background paint
		 */
		Paint barBgPaint = new Paint();
		barBgPaint.setColor(this.barBg);
		barBgPaint.setStyle(Style.FILL);

		for (int i = 0; i < bars.size(); i++) {
			Bar tmp = bars.get(i);
			/**
			 * draw bar background
			 */
			// float ratio = getHeight() / maxValue;
			// Rect barBgRect = new Rect(tmp.left + moveOffset, (int) (ratio * minValue) + xAxisHeight, tmp.right + moveOffset, tmp.bottom);
			
			Rect barBgRect = new Rect(tmp.left + moveOffset, 0, tmp.right + moveOffset, tmp.bottom);
			canvas.drawRect(barBgRect, barBgPaint);

			Rect barRect = new Rect(tmp.left + moveOffset, tmp.top, tmp.right + moveOffset, tmp.bottom);
			canvas.drawRect(barRect, barPaint);
		}
	}

	// ----------------------------------

	public void drawBg(Canvas canvas) {

		Paint bgPaint = new Paint();
		bgPaint.setColor(coordinateBg);
		bgPaint.setStyle(Style.FILL);
		Rect bgRect = new Rect(0, 0, getWidth(), getHeight());
		canvas.drawRect(bgRect, bgPaint);

	}

	private int downX;
	private int moveOffset;

	private int moveX;

	private int downIndex;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downIndex = checkClickRect((int) event.getX(), (int) event.getY());
			break;
		case MotionEvent.ACTION_MOVE:

			if (checkIfCanMove(bars)) {
				invalidate();
			}

			moveOffset = (int) (event.getX() - downX);
			moveOffset += moveX;

			break;
		case MotionEvent.ACTION_UP:
			moveX = moveOffset;

			int index = checkClickRect((int) event.getX(), (int) event.getY());
			if (downIndex > 0 && index > 0 && downIndex == index && mOnBarSelectedListener != null) {
				mOnBarSelectedListener.onBarSelected(index, barDatas.get(index).yValue);
			}
			break;
		}
		return true;
	}

	public int checkClickRect(int x, int y) {
		if (bars == null || bars.size() == 0) {
			return -1;
		}

		for (int i = 0; i < bars.size(); i++) {
			Bar tmp = bars.get(i);
			if (x > tmp.left + moveOffset && x < tmp.right + moveOffset && y > tmp.top && y < tmp.bottom) {
				return i;
			}
		}

		return -1;
	}

	public boolean checkIfCanMove(ArrayList<Bar> bars) {
		if (bars == null || bars.size() == 0) {
			return false;
		}

		int size = bars.size();

		Bar firstBar = bars.get(0);
		Bar lastBar = bars.get(size - 1);

		if (firstBar.left + moveOffset < firstBar.left && lastBar.right + moveOffset > getWidth()) {

			if (firstBar.left + moveOffset >= firstBar.left) {
				moveOffset = 0;
			}

			if (lastBar.right + moveOffset <= getWidth()) {
				moveOffset = getWidth() - lastBar.right;
			}
			return true;
		}

		return false;
	}

	public void drawTextInCenter(Canvas canvas, Paint paint, Rect targetRect, String xVal) {

		FontMetricsInt fontMetrics = paint.getFontMetricsInt();

		// Reference from http://blog.csdn.net/hursing
		int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(xVal, targetRect.centerX(), baseline, paint);

	}

	public interface OnBarSelectedListener {
		public void onBarSelected(int index, int value);
	}

	private OnBarSelectedListener mOnBarSelectedListener;

	public void setOnBarSelectedListener(OnBarSelectedListener mOnBarSelectedListener) {
		this.mOnBarSelectedListener = mOnBarSelectedListener;
	}

}
