package com.andyridge.minutetimerlite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.util.AttributeSet;
import android.view.View;

public class PieView extends View
{
	private int currentAngle = 0;
	private int currentTime = 0;
	
	private final int START = -90;
	private final int END = 360;

    private int state;
    private static final int STATE_RUNNING = 0;
    private static final int STATE_PAUSED = 1;
    private static final int STATE_STOPPED = 2;

    private int viewWidth;
    private int viewHeight;
    private int size;
    private int textSize;
	
	private ShapeDrawable pie;
    private ShapeDrawable midPie;
    private ShapeDrawable midPie2;
    private ShapeDrawable innerPie;

    private ArcShape arc;
    private ArcShape midArc;
    private ArcShape midArc2;
    private ArcShape innerArc;

    private Paint textPaint;
	
	public PieView(Context context) 
	{
		super(context);
		init(context);
	}
	
	public PieView(Context context, AttributeSet attributes)
	{
		super(context, attributes);
		init(context);
	}
	
	private void init(Context context)
	{
		this.currentAngle = 360;
        this.state = STATE_STOPPED;

        arc = new ArcShape(START, this.currentAngle);
        midArc = new ArcShape(START, this.currentAngle);
        midArc2 = new ArcShape(START, this.currentAngle);
        innerArc = new ArcShape(START, this.currentAngle);

        textPaint = new Paint();

		pie = new ShapeDrawable(arc);
		pie.getPaint().setColor(getResources().getColor(R.color.gold_light));

        midPie = new ShapeDrawable(midArc);
        midPie.getPaint().setColor(getResources().getColor(R.color.gold_xlight));

        midPie2 = new ShapeDrawable(midArc2);
        midPie2.getPaint().setColor(getResources().getColor(R.color.gold_xxlight));

        innerPie = new ShapeDrawable(innerArc);
        innerPie.getPaint().setColor(getResources().getColor(R.color.holo_light_bg));

        invalidate();
        requestLayout();
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        size = (int) (Math.min(viewWidth, viewHeight) * 0.8);
        textSize = (int) (0.5 * size);

        // This only needs to be as tall as the pie chart itself
		setMeasuredDimension(viewWidth, viewHeight);
	}

    public void goToStart(int time)
    {
    	currentAngle = 0;
    	currentTime = time;
        invalidate();
        postInvalidate();
    }
    
    public void goToIndex(int index, int total, boolean invalidate)
    {
    	currentTime = total - index;
    	currentAngle = (int) ((double) index / (double) total * 360);
    	
    	if(invalidate)
    		invalidate();
    }
    
    public void goToAngle(int angle, boolean invalidate)
    {
    	currentAngle = angle;
    	
    	if(invalidate)
    		invalidate();
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(R.color.grey_mid));

        int xCentre = viewWidth / 2;
        int yCentre = viewHeight / 2;

        int top = yCentre - size / 2;
        int bottom = yCentre + size / 2;
        int left = xCentre - size / 2;
        int right = xCentre + size / 2;

        arc = new ArcShape(currentAngle + START, END - currentAngle);
        pie.setShape(arc);
    	pie.setBounds(left, top, right, bottom);
    	pie.draw(canvas);

        final int W = 25;
        final int W2 = 40;
        final int W3 = 28;

        midArc = new ArcShape(currentAngle + START, END - currentAngle);
        midPie.setShape(midArc);
        midPie.setBounds(left + size / W2, top + size / W2, right - size / W2, bottom - size / W2);
        midPie.draw(canvas);

        midArc2 = new ArcShape(currentAngle + START, END - currentAngle);
        midPie2.setShape(midArc2);
        midPie2.setBounds(left + size / W3, top + size / W3, right - size / W3, bottom - size / W3);
        midPie2.draw(canvas);

        innerPie.setBounds(left + size / W, top + size / W, right - size / W, bottom - size / W);
        innerPie.draw(canvas);

        float textHeight = textPaint.descent() - textPaint.ascent();
        float textOffset = (textHeight / 2) - textPaint.descent();

        RectF bounds = new RectF(0, 0, viewWidth, viewHeight);
        canvas.drawText("" + currentTime, bounds.centerX(), bounds.centerY() + textOffset, textPaint);
    }
    
    public int getCurrentTime()
    {
    	return currentTime;
    }

}
