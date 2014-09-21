package com.andyridge.minutetimerlite;

import com.andyridge.minutetimerlite.lib.Constants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

//TODO Make it so that they can change the audio stream that it outputs to
//TODO Add instructions to say what the exercise in
//TODO Add disclaimer if people do damage to themselves
//TODO Add link to the exercise videos in the countdown screen
//TODO Add facility to skip to next exercise
//TODO Make it so that it reads out the name of the exercise on resume after pausing
		// After waiting a few seconds
//TODO Add a reminder to make them do the exercise at a certain time every day
//TODO Add the option to start the exercise half way through
//TODO Implement some statistics
	// - Remember what day they did each exercise on, etc

import static com.andyridge.minutetimerlite.lib.Constants.*;

public class MinuteMenu extends Activity implements OnClickListener
{
	private Button absButton;
	private Button armsButton;
	private Button legsButton;
	private Button bunsButton;
	private Button stretchButton;
	private Button aboutButton;
	private Button settingsButton;
	
	public static int deviceWidth;
	public static int deviceHeight;
	
	public static int sound = 0;
	
	public static boolean readAloud = false;
	public static int locale = 0;
	
	public static boolean keepAwake = false;
	
	@Override
	public void onAttachedToWindow() 
	{
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		
		setVolumeControlStream(AudioManager.STREAM_ALARM);
        
		setContentView(R.layout.menu);
		
		Display display = getWindowManager().getDefaultDisplay();
        deviceWidth = display.getWidth();
        deviceHeight = display.getHeight();
        
		absButton = (Button) findViewById(R.id.absButton);
        absButton.setOnClickListener(this);
        
        armsButton = (Button) findViewById(R.id.armsButton);
        armsButton.setOnClickListener(this);
        
        legsButton = (Button) findViewById(R.id.legsButton);
        legsButton.setOnClickListener(this);
        
        bunsButton = (Button) findViewById(R.id.bunsButton);
        bunsButton.setOnClickListener(this);
        
        stretchButton = (Button) findViewById(R.id.stretchButton);
        stretchButton.setOnClickListener(this);
        
        aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MinuteMenu.this, About.class);
				startActivity(i);
			}	
        });
        
        settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MinuteMenu.this, Settings.class);
				startActivity(i);
			}	
        });
        
        this.setTitle(getString(R.string.app_name));
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        // Sort out the preferences
        sound = Integer.parseInt(prefs.getString(this.getString(R.string.sound), "0"));
        
        readAloud = prefs.getBoolean(this.getString(R.string.read_aloud), false);
        locale = Integer.parseInt(prefs.getString(this.getString(R.string.locale), "0"));
        
        setLock(prefs.getBoolean(this.getString(R.string.keep_awake), false));
	}
	
	public static void setLock(boolean lock)
	{
		keepAwake = lock;
	}

	@Override
	public void onClick(View v) 
	{
		Intent i = new Intent(this, MinuteTimer.class);
		
		if(v.equals(absButton))
		{
			i.putExtra(TIMING, TIMING_ABS);
			i.putExtra(NAMES, NAME_ABS);
			i.putExtra(NAME, ABS);
			i.putExtra(VALUE, Exercise.ABS.ordinal());
		}
		else if(v.equals(armsButton))
		{
			i.putExtra(TIMING, TIMING_ARMS);
			i.putExtra(NAMES, NAME_ARMS);
			i.putExtra(NAME, ARMS);
			i.putExtra(VALUE, Exercise.ARMS.index);
		}
		else if(v.equals(legsButton))
		{
			i.putExtra(TIMING, TIMING_LEGS);
			i.putExtra(NAMES, NAME_LEGS);
			i.putExtra(NAME, LEGS);
			i.putExtra(VALUE, Exercise.LEGS.ordinal());
		}
		else if(v.equals(bunsButton))
		{
			i.putExtra(TIMING, TIMING_BUNS);
			i.putExtra(NAMES, NAME_BUNS);
			i.putExtra(NAME, BUNS);
			i.putExtra(VALUE, Exercise.BUNS.ordinal());
		}
		else if(v.equals(stretchButton))
		{
			i.putExtra(TIMING, TIMING_STRETCH);
			i.putExtra(NAMES, NAME_STRETCH);
			i.putExtra(NAME, STRETCH);
			i.putExtra(VALUE, Exercise.STRETCH.ordinal());
		}
		
		startActivity(i);
	}
}
