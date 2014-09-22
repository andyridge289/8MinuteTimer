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
    private ShapeDrawable innerPie;
    private ArcShape arc;
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
        innerArc = new ArcShape(START, this.currentAngle);

        textPaint = new Paint();

		pie = new ShapeDrawable(arc);
		pie.getPaint().setColor(Color.rgb(131, 168, 255));

        innerPie = new ShapeDrawable(innerArc);
        innerPie.getPaint().setColor(Color.rgb(243, 243, 243));

        invalidate();
        requestLayout();
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
//        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
//        width = resolveSizeAndState(minw, widthMeasureSpec, 1);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        size = (int) (Math.min(viewWidth, viewHeight) * 0.8);
        textSize = (int) (0.5 * size);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
//        int minh = MeasureSpec.getSize(width) - getPaddingBottom() + getPaddingTop();
//        height = resolveSizeAndState(MeasureSpec.getSize(minh), heightMeasureSpec, 0);


        // This only needs to be as tall as the pie chart itself
		setMeasuredDimension(viewWidth, viewHeight);
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

    public void invalidate() {
        super.invalidate();
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        arc = new ArcShape(currentAngle + START, END - currentAngle);

    	pie.setShape(arc);

        int xCentre = viewWidth / 2;
        int yCentre = viewHeight / 2;

        int top = yCentre - size / 2;
        int bottom = yCentre + size / 2;
        int left = xCentre - size / 2;
        int right = xCentre + size / 2;
    	
    	pie.setBounds(left, top, right, bottom);
    	pie.draw(canvas);

        innerPie.setBounds(left + size / 16, top + size / 16, right - size / 16, bottom - size / 16);
        innerPie.draw(canvas);

    	textPaint.setStyle(Paint.Style.FILL);
    	textPaint.setAntiAlias(true);
    	textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.rgb(21, 65, 166));

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
