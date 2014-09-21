package com.andyridge.minutetimerlite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.util.AttributeSet;
import android.view.View;

import com.andyridge.minutetimerlite.lib.Constants;

public class PieView extends View implements Constants
{
	private int currentAngle = 0;
	private int currentTime = 0;
	
	// Need to change this depending on the screen size
	private int size = 100; // Circle so width == height
	private int textSize = 100;
	
	private final int START = -90;
	private final int END = 360;
	
	private ShapeDrawable pie;
	
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
		setFocusable(true);
		
		this.currentAngle = 100;
		
		if(MinuteMenu.deviceHeight >= 480)
		{
			size = 380;
			textSize = 160;
		}
		else if(MinuteMenu.deviceHeight >= 320)
		{
			size = 200;
			textSize = 150;
		}
		else if(MinuteMenu.deviceHeight >= 240)
		{
			size = 150;
			textSize = 100;
		}
		else
		{
			size = 100;
			textSize = 60;
		}
		
		pie = new ShapeDrawable(new ArcShape(START, this.currentAngle));
		pie.getPaint().setColor(Color.argb(125, 21, 65, 166));
	}
	

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{	
		// This only needs to be as tall as the pie chart itself
		setMeasuredDimension(MinuteMenu.deviceWidth, size + size/2);
	}
	
    public void goToEnd(boolean paramBoolean)
    {
    	
    	this.currentAngle = END;
    	
    	if (paramBoolean)
    		invalidate();
    }

    public void goToStart(int time, boolean paramBoolean)
    {
    	currentAngle = 0;
    	currentTime = time;
    	
    	if (paramBoolean)
    		invalidate();
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
    	pie.setShape(new ArcShape(currentAngle + START, END - currentAngle));
    	
    	// Ugh. Maths.
    	int left = MinuteMenu.deviceWidth/2 - size/2;
    	int right = MinuteMenu.deviceWidth/2 + size/2;
    	int top = size/8;
    	int bottom = size + size/8;
    	
    	pie.setBounds(left, top, right, bottom);
    	pie.draw(canvas);
    	
    	Paint textPaint = new Paint();
    	textPaint.setStyle(Paint.Style.FILL);
    	textPaint.setAntiAlias(true);
    	textPaint.setTextSize(textSize);
    	textPaint.setColor(Color.rgb(131, 168, 255));
    	
    	int textLeft = MinuteMenu.deviceWidth/2 - (int) (textSize * 0.57);
    	int textTop = 11*size/16 + textSize/8;
    	
    	canvas.drawText("" + currentTime, textLeft, textTop, textPaint);
    }
    
    public int getCurrentTime()
    {
    	return currentTime;
    }

}
