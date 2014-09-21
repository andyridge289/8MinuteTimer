package com.andyridge.minutetimerlite;

import com.andyridge.minutetimerlite.lib.Constants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;

public class Settings extends PreferenceActivity implements Constants, OnSharedPreferenceChangeListener
{	
	private final int CHECK_TTS_AVAILABILITY = 0;
	private SharedPreferences sharedPreferences = null;
	
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
        addPreferencesFromResource(R.xml.settings);
        
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{
		this.sharedPreferences = (this.sharedPreferences == null) ? sharedPreferences : this.sharedPreferences;
		
		if(key.equals(getString(R.string.sound)))
		{
	        MinuteMenu.sound = Integer.parseInt(sharedPreferences.getString(this.getString(R.string.sound), "0"));
		}
		else if(key.equals(getString(R.string.keep_awake)))
		{
			MinuteMenu.setLock(sharedPreferences.getBoolean(this.getString(R.string.keep_awake), false));
		}
		else if(key.equals(getString(R.string.read_aloud)))
		{
			
			MinuteMenu.readAloud = sharedPreferences.getBoolean(this.getString(R.string.read_aloud), false);
			
			if(MinuteMenu.readAloud)
			{
				Intent ttsCheck = new Intent();
				ttsCheck.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
				startActivityForResult(ttsCheck, CHECK_TTS_AVAILABILITY);
			}
		}
		else if(key.equals(getString(R.string.locale)))
		{
			MinuteMenu.locale = Integer.parseInt(sharedPreferences.getString(this.getString(R.string.locale), "0"));
			MinuteTimer.resetLocale();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == CHECK_TTS_AVAILABILITY)
		{
			if(resultCode != TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
			{
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() 
				{
				    @Override
				    public void onClick(DialogInterface dialog, int result) 
				    {
				        switch (result)
				        {
				        	case DialogInterface.BUTTON_POSITIVE:
				        		Intent ttsInstall = new Intent();
								ttsInstall.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
								startActivity(ttsInstall);
				        		break;

				        	case DialogInterface.BUTTON_NEGATIVE:
				        		Editor editor = sharedPreferences.edit();
				        		editor.putBoolean(getString(R.string.read_aloud), false);
				        		editor.commit();
				        		break;
				        }
				    }
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("The Text To Speech (TTS) library is required to read out exercises. Install now?")
					   .setPositiveButton("Yes", dialogClickListener)
				       .setNegativeButton("No", dialogClickListener)
				       .show();
			}
			
			// TTS is installed - don't think we need to do anything else here
		}
	}
}
