package com.uzmap.pkg.uzmodules.UIBarChart.widget;

import java.util.ArrayList;

import com.uzmap.pkg.uzkit.UZUtility;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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

	public ArrayList<BarData> getData() {
		return barDatas;
	}

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
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if (isFirst) {
			init(barDatas);
			isFirst = false;
		}
	}

	public void setData(ArrayList<BarData> barDatas) {
		this.barDatas = barDatas;
		init(barDatas);
	}

	public void updataData(ArrayList<BarData> barDatas) {

		this.barDatas.clear();
		this.bars.clear();

		this.barDatas = barDatas;
		init(this.barDatas);

		invalidate();

	}

	private Bitmap bgBitmap;

	public void setBgImage(Bitmap bitmap) {
		this.bgBitmap = bitmap;
	}

	private Bitmap barBgBitmap;

	public void setBarBgImage(Bitmap bitmap) {
		barBgBitmap = bitmap;
	}

	private Bitmap xAxisBgImage;

	public void setXAxisBgImage(Bitmap xAxisBgImage) {
		this.xAxisBgImage = xAxisBgImage;
	}

	private Bitmap yAxisBgImage;

	public void setYAxisBgImage(Bitmap yAxisBgImage) {
		this.yAxisBgImage = yAxisBgImage;
	}

	public void init(ArrayList<BarData> barData) {

		bars.clear();

		int left = barSpace / 2 + yAxisWidth;

		for (int i = 0; i < barData.size(); i++) {

			float barHeightF = (float)getHeight() / (float)maxValue * (float)(barData.get(i).yValue);

			int barHeight = (int) barHeightF;
			int bottomY = getHeight() - xAxisHeight;

			float ratio = getHeight() / maxValue;
			
			Bar bar = new Bar(left, bottomY - barHeight
					+ (int) (ratio * minValue), (left + barWidth), bottomY);
			left += barSpace + barWidth;
			bars.add(bar);
			
			if(i == barData.size() - 1) {
				
				left -= barSpace / 2;
				
				// the last space 
				Bar lastBar = new Bar(left, bottomY
						+ (int) (ratio * minValue), left, bottomY);
				bars.add(lastBar);
				
			}
			
		}
		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// draw bg
		drawBg(canvas);

		// draw bars
		drawBars(canvas);

		// draw x axis
		drawXAxis(canvas);
		
		// draw x axis lables
		drawXAxisLabel(canvas);

		// draw y axis
		drawYAxis(canvas);
		
		// draw y axis labels
		drawYAxisLabel(canvas);
		
		drawAxisDescribe(canvas);
		
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

		if (xAxisBgImage != null) {
			Rect srcRect = new Rect(0, 0, xAxisBgImage.getWidth(),
					xAxisBgImage.getHeight());
			Rect targetRect = new Rect(xAxisLeft, xAxisTop, xAxisRight,
					xAxisBottom);
			Paint paint = new Paint();
			canvas.drawBitmap(xAxisBgImage, srcRect, targetRect, paint);

		} else {

			Rect rect = new Rect(xAxisLeft, xAxisTop, xAxisRight, xAxisBottom);
			/**
			 * the paint of the axis
			 */
			Paint xAxisPaint = new Paint();
			xAxisPaint.setColor(this.xAxisBgColor);
			xAxisPaint.setStyle(Style.FILL);

			canvas.drawRect(rect, xAxisPaint);

		}
	}

	public void drawXAxisLabel(Canvas canvas) {

		for (int i = 0; i < bars.size() - 1; i++) {
			Bar tmp = bars.get(i);
			Rect rect = new Rect(tmp.left + moveOffset, tmp.bottom, tmp.right
					+ moveOffset, getHeight());

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStrokeWidth(3);

			paint.setTextSize(xAxisMarkSize);
			paint.setColor(xAxisMarkColor);

			drawTextInCenter(canvas, paint, rect, barDatas.get(i).xValue);
		}
	}
	
	public void setMoveOffset(int moveOffset){
		this.moveOffset = moveOffset;
	}

	public void drawYAxisLabel(Canvas canvas) {
		int range = maxValue - minValue;
		int labelCount = range / step;

		int stepValue = minValue;

		for (int i = 0; i <= labelCount; i++) {

			float barHeightF = (float)getHeight() / (float)maxValue * (float)stepValue;
			
			Log.i(TAG, " ====> barHeightF " + barHeightF);
			
			int top = getHeight() - (int) barHeightF - xAxisHeight
					+ getHeight() / maxValue * minValue;

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStrokeWidth(3);
			paint.setTextSize(yAxisMarkSize);
			paint.setColor(yAxisMarkColor);
			
			if(i == 0){
				Rect yValueRect = new Rect(0, top - 25, yAxisWidth, top - UZUtility.dipToPix(10));
				drawTextInCenter(canvas, paint, yValueRect, stepValue + "");
			} else {
				Rect yValueRect = new Rect(0, top - 25, yAxisWidth, top);
				drawTextInCenter(canvas, paint, yValueRect, stepValue + "");
			}
			
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

		if (yAxisBgImage != null) {
			
			Rect srcRect = new Rect(0, 0, yAxisBgImage.getWidth(),yAxisBgImage.getHeight());
			Rect targetRect = new Rect(yAxisLeft, yAxisTop, yAxisWidth,yAxisBottom);
			Paint paint = new Paint();
			canvas.drawBitmap(yAxisBgImage, srcRect, targetRect, paint);

		} else {

			Rect rect = new Rect(yAxisLeft, yAxisTop, yAxisWidth, yAxisBottom);

			/**
			 * the paint of the y axis
			 */
			Paint yAxisPaint = new Paint();
			yAxisPaint.setColor(this.yAxisBgColor);

			canvas.drawRect(rect, yAxisPaint);
			
			// bad way
			Rect rectPuding = new Rect(yAxisLeft, getHeight()-xAxisHeight, yAxisWidth, getHeight());
			yAxisPaint.setColor(xAxisBgColor);
			canvas.drawRect(rectPuding, yAxisPaint);
			
		}
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

			if (barBgBitmap != null) {

				Rect srcRect = new Rect(0, 0, barBgBitmap.getWidth(),
						barBgBitmap.getHeight());
				Rect targetRect = new Rect(tmp.left + moveOffset, 0, tmp.right
						+ moveOffset, tmp.bottom);
				canvas.drawBitmap(barBgBitmap, srcRect, targetRect, barBgPaint);

			} else {
				Rect barBgRect = new Rect(tmp.left + moveOffset, 0, tmp.right + moveOffset, tmp.bottom);
				canvas.drawRect(barBgRect, barBgPaint);
			}

			Rect barRect = new Rect(tmp.left + moveOffset, tmp.top, tmp.right + moveOffset, tmp.bottom);
			canvas.drawRect(barRect, barPaint);
			
			if(showIndex == i && isShowValue){
				
				Rect barDataRect = new Rect(tmp.left + moveOffset, 0, tmp.right + moveOffset, tmp.bottom);
				barDataLabelPaint.setColor(yAxisMarkColor);
				barDataLabelPaint.setTextSize(DensityUtil.dip2px(getContext(), 10));
				drawTextInCenter(canvas, barDataLabelPaint, barDataRect, String.valueOf(barDatas.get(i).yValue));
				
			}
		}
	}
	
	private Paint barDataLabelPaint = new Paint();
	private int showIndex = -1;
	private boolean isShowValue;
	
	public void showValue(int index){
		this.showIndex = index;
		this.invalidate();
	}
	
	public void setShowValue(boolean isShow){
		this.isShowValue = isShow;
	}

	// ----------------------------------
	public void drawBg(Canvas canvas) {

		if (bgBitmap != null) { // use image

			Paint paint = new Paint();
			Rect srcRect = new Rect(0, 0, bgBitmap.getWidth(),
					bgBitmap.getHeight());
			Rect desRect = new Rect(0, 0, getWidth(), getHeight());
			canvas.drawBitmap(bgBitmap, srcRect, desRect, paint);

		} else { // use color

			Paint bgPaint = new Paint();
			bgPaint.setColor(coordinateBg);
			bgPaint.setStyle(Style.FILL);
			Rect bgRect = new Rect(0, 0, getWidth(), getHeight());
			canvas.drawRect(bgRect, bgPaint);
			
		}
		
	}

	private int downX;
	private int moveOffset;

	private int moveX;
	private int downIndex;

	private boolean isLeftMost;
	private boolean isRightMost;
	
	private boolean isMoveFlag;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downIndex = checkClickRect((int) event.getX(), (int) event.getY());
			break;
		case MotionEvent.ACTION_MOVE:

			Bar firstBar = bars.get(0);
			Bar lastBar = bars.get(bars.size() - 1);

			if (firstBar.left + moveOffset <= firstBar.left
					&& lastBar.right + moveOffset >= getWidth()) {
				
				invalidate();
				moveOffset = (int) (event.getX() - downX);
				moveOffset += moveX;

				isLeftMost = false;
				isRightMost = false;

				if (firstBar.left + moveOffset > firstBar.left) {
					moveOffset = 0;
					isLeftMost = true;
				}

				if (lastBar.right + moveOffset < getWidth()){
					moveOffset = getWidth() - lastBar.right;
					isRightMost = true;
				}
				
			}
			
			if (firstBar.left + moveOffset > firstBar.left) {
				isLeftMost = true;
			}

			if (lastBar.right + moveOffset < getWidth()){
				isRightMost = true;
			}
			
			isMoveFlag = checkIsMove(downX, (int)event.getX());

			break;
		case MotionEvent.ACTION_UP:
			moveX = moveOffset;

			int index = checkClickRect((int) event.getX(), (int) event.getY());
			
			if (downIndex >= 0 && index >= 0 && downIndex == index
					&& mOnBarSelectedListener != null) {
				mOnBarSelectedListener.onBarSelected(index,
						barDatas.get(index).yValue);
			}

			if (isLeftMost && mOnMoveDetectListener != null 
					&& isMoveFlag) {
				isMoveFlag = false;
				mOnMoveDetectListener.onMoveLeftMost();
			}

			if (isRightMost && mOnMoveDetectListener != null 
					&& isMoveFlag) {
				isMoveFlag = false;
				mOnMoveDetectListener.onMoveRightMost();
			}
			break;
		}
		return true;
	}
	
	private String xDesc = "X轴";
	public void setXAxisDescribe(String desc){
		this.xDesc = desc;
	}
	
	private String yDesc = "Y轴";
	public void setYAxisDescribe(String desc){
		this.yDesc = desc;
	}
	
	public void drawAxisDescribe(Canvas canvas) {
		
		Paint xDescPaint = new Paint();
		xDescPaint.setColor(xAxisMarkColor);
		xDescPaint.setTextSize(xAxisMarkSize);
		
		Rect xDesRect = new Rect(0, getHeight() - xAxisHeight, UZUtility.dipToPix(20), getHeight());
		drawTextInCenter(canvas, xDescPaint, xDesRect, this.xDesc);
		
		
		Paint yDescPaint = new Paint();
		yDescPaint.setColor(yAxisMarkColor);
		yDescPaint.setTextSize(yAxisMarkSize);
		
		Rect yDescRect = new Rect(0, UZUtility.dipToPix(8), yAxisWidth, UZUtility.dipToPix(20));
		drawTextInCenterWithRotate(canvas, yDescPaint, yDescRect, this.yDesc, 90);
		
	}
	
	public boolean checkIsMove(int moveStart, int moveEnd){
		if(Math.abs(moveEnd - moveStart) > 150){
			return true;
		}
		return false;
	}

	public int checkClickRect(int x, int y) {
		if (bars == null || bars.size() == 0) {
			return -1;
		}

		for (int i = 0; i < bars.size(); i++) {
			Bar tmp = bars.get(i);
			if (x > tmp.left + moveOffset && x < tmp.right + moveOffset
					/* && y > tmp.top && y < tmp.bottom */) {
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

		if (firstBar.left + moveOffset >= firstBar.left) {
			moveOffset = 0;

			if (mOnMoveDetectListener != null) {
				// mOnMoveDetectListener.onMoveLeftMost();
			}
		}

		if (lastBar.right + moveOffset <= getWidth()) {
			moveOffset = getWidth() - lastBar.right;

			if (mOnMoveDetectListener != null) {
				// mOnMoveDetectListener.onMoveRightMost();
			}
		}

		if (firstBar.left + moveOffset < firstBar.left
				&& lastBar.right + moveOffset > getWidth()) {
			return true;
		}

		return false;
	}

	public void drawTextInCenter(Canvas canvas, Paint paint, Rect targetRect,
			String xVal) {

		FontMetricsInt fontMetrics = paint.getFontMetricsInt();

		// Reference from http://blog.csdn.net/hursing
		int baseline = targetRect.top
				+ (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top)
				/ 2 - fontMetrics.top;
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(xVal, targetRect.centerX(), baseline, paint);

	}
	
	public void drawTextInCenterWithRotate(Canvas canvas, Paint paint, Rect targetRect,
			String xVal, int angel) {

		FontMetricsInt fontMetrics = paint.getFontMetricsInt();

		// Reference from http://blog.csdn.net/hursing
		int baseline = targetRect.top
				+ (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top)
				/ 2 - fontMetrics.top;
		paint.setTextAlign(Paint.Align.CENTER);
		
		int centerY = targetRect.top + (targetRect.bottom - targetRect.top) / 2;
		int centerX = targetRect.left + (targetRect.right - targetRect.left) / 2;
		canvas.rotate(-90, centerX, centerY);
		
		canvas.drawText(xVal, targetRect.centerX(), baseline, paint);
		
	}
	

	public interface OnBarSelectedListener {
		public void onBarSelected(int index, int value);
	}

	private OnBarSelectedListener mOnBarSelectedListener;

	public void setOnBarSelectedListener(
			OnBarSelectedListener mOnBarSelectedListener) {
		this.mOnBarSelectedListener = mOnBarSelectedListener;
	}

	public interface OnMoveDetectListener {
		public void onMoveLeftMost();
		public void onMoveRightMost();
	}

	private OnMoveDetectListener mOnMoveDetectListener;

	public void setOnMoveDetectListener(
			OnMoveDetectListener mOnMoveDetectListener) {
		this.mOnMoveDetectListener = mOnMoveDetectListener;
	}
	
	public int getBarNumsPerScreen(){
		return (getWidth() - yAxisWidth) / (barWidth + barSpace);
	}
	
	@SuppressLint("NewApi")
	public void scrollTo(int index, boolean anim){
		if(index > (bars.size() - getBarNumsPerScreen())){
			return;
		}
		final Bar targetBar = bars.get(index);
		if(anim){
			ValueAnimator animator = ValueAnimator.ofInt(targetBar.left + moveOffset - barSpace, barSpace);
			animator.setDuration(500);
			animator.start();
			animator.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					int offset = (Integer)animation.getAnimatedValue() - targetBar.left + barSpace;
					moveOffset = offset;
					invalidate();
					moveX = moveOffset;
				}
			});
		} else {
			moveOffset = - targetBar.left + 2 * barSpace;
			invalidate();
			moveX = moveOffset;
		}
	}
	
}
