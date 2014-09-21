package com.andyridge.minutetimerlite;

import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.andyridge.minutetimerlite.lib.Constants;

import java.util.HashMap;

import static com.andyridge.minutetimerlite.lib.Constants.LOCALES;
import static com.andyridge.minutetimerlite.lib.Constants.TAG;


public class Home extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, TextToSpeech.OnInitListener {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private static TextToSpeech tts;
    private HashMap<String, String> alarmStream;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tts = new TextToSpeech(this, this);

        alarmStream = new HashMap<String, String>();
        alarmStream.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                String.valueOf(AudioManager.STREAM_ALARM));

        setVolumeControlStream(AudioManager.STREAM_ALARM);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        Fragment active = null;

        if (position == NavigationDrawerFragment.Page.ABS.index) {
            active = TimerFragment.newInstance(Constants.Exercise.ABS);
        } else if (position == NavigationDrawerFragment.Page.ARMS.index) {
            active = TimerFragment.newInstance(Constants.Exercise.ARMS);
        } else if (position == NavigationDrawerFragment.Page.LEGS.index) {
            active = TimerFragment.newInstance(Constants.Exercise.LEGS);
        } else if (position == NavigationDrawerFragment.Page.BUNS.index) {
            active = TimerFragment.newInstance(Constants.Exercise.BUNS);
        } else if (position == NavigationDrawerFragment.Page.STRETCH.index) {
            active = TimerFragment.newInstance(Constants.Exercise.STRETCH);
        } else if (position == NavigationDrawerFragment.Page.SETTINGS.index) {
            active = SettingsFragment.newInstance();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, active)
                .commit();
    }

    public void setTitle(CharSequence newTitle) {
        super.setTitle(newTitle);
        mTitle = newTitle;
        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void onDestroy() {

        //Close the Text to Speech Library
        if(tts != null) {

            tts.stop();
            tts.shutdown();
            Log.d(TAG, "TTS Destroyed");
        }
    }

    public void onBackPressed() {
        // TODO Call back pressed on the right fragment
        super.onBackPressed();
    }

    public static void resetLocale()
    {
        if(tts != null)
            tts.setLanguage(LOCALES[MinuteMenu.locale]);
    }


    public void onInit(int status)
    {
        tts.setLanguage(LOCALES[MinuteMenu.locale]);
    }


    public void speak(String word) {
        tts.speak(word, TextToSpeech.QUEUE_ADD, alarmStream);
    }
}
