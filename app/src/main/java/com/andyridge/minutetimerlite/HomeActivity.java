package com.andyridge.minutetimerlite;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.andyridge.minutetimerlite.lib.Constants;

import java.util.HashMap;

import static com.andyridge.minutetimerlite.lib.Constants.LOCALES;
import static com.andyridge.minutetimerlite.lib.Constants.TAG;


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

public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, TextToSpeech.OnInitListener {

    private static final String PAGE = "page";
    private static final String EXERCISE_DATA = "exercise_data";

    private NavigationDrawerFragment mNavigationDrawerFragment;

    public static TextToSpeech tts;
    private static HashMap<String, String> alarmStream;

    private CharSequence mTitle;

    private NavigationDrawerFragment.Page page;
    private Fragment[] fragments;

    private int[][] data;

    public static int sound = 0;
    public static boolean readAloud = false;
    public static int locale = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragments = new Fragment[NavigationDrawerFragment.Page.values().length];

        data = new int[NavigationDrawerFragment.Page.values().length][4];
        for (int i = 0; i < NavigationDrawerFragment.Page.values().length; i++) {
            data[i] = null;
        }

        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Sort out the preferences
        sound = Integer.parseInt(sharedPreferences.getString(this.getString(R.string.sound), "0"));
        readAloud = sharedPreferences.getBoolean(this.getString(R.string.read_aloud), false);
        locale = Integer.parseInt(sharedPreferences.getString(this.getString(R.string.locale), "0"));

//        setLock(sharedPreferences.getBoolean(this.getString(R.string.keep_awake), false));

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

        if(savedInstanceState != null) {
            page = NavigationDrawerFragment.Page.values()[savedInstanceState.getInt(PAGE)];
            data[page.index] = savedInstanceState.getIntArray(EXERCISE_DATA);
            onNavigationDrawerItemSelected(page.index);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PAGE, page.index);
        if(page.index < NavigationDrawerFragment.Page.SETTINGS.index) {
            outState.putIntArray(EXERCISE_DATA, ((TimerFragment) fragments[page.index]).getData());
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        // Lots of things could be null in this set of steps, so check them all
        if(page != null && page.index < NavigationDrawerFragment.Page.SETTINGS.index) {
            TimerFragment tf = (TimerFragment) fragments[page.index];
            if (tf != null) {
                int[] fragmentData = tf.getData();
                if (fragmentData != null) {
                    data[page.index] = fragmentData;
                }
            }
        }

        if(position < NavigationDrawerFragment.Page.SETTINGS.index) {
            fragments[position] = TimerFragment.newInstance(Constants.Exercise.values()[position], data[position]);
        } else if(position == NavigationDrawerFragment.Page.SETTINGS.index) {
            fragments[position] = SettingsFragment.newInstance();
        } else {
            fragments[position] = AboutFragment.newInstance();
        }

        page = NavigationDrawerFragment.Page.values()[position];
        Fragment active = fragments[position];

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

    void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void onDestroy() {

        //Close the Text to Speech Library
        if(tts != null) {

            tts.stop();
            tts.shutdown();
            Log.d(TAG, "TTS Destroyed");
        }

        super.onDestroy();
    }

    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.close();
        } else if (page.index > NavigationDrawerFragment.Page.ABS.index) {
            if(page.index < NavigationDrawerFragment.Page.SETTINGS.index) {
                // If we are looking at a timer, we need to make it stop the running timer
                ((TimerFragment) fragments[page.index]).backPressed();
            }

            // Go back to the abs page, which is basically the home page
            onNavigationDrawerItemSelected(NavigationDrawerFragment.Page.ABS.index);
        } else {
            // We're on the abs page, this so we can leave
            super.onBackPressed();
        }
    }

    public static void resetLocale() {
        if(tts != null)
            tts.setLanguage(LOCALES[locale]);
    }


    public void onInit(int status) {
        tts.setLanguage(LOCALES[locale]);
    }

    public static void speak(String word) {
        tts.speak(word, TextToSpeech.QUEUE_ADD, alarmStream);
    }


    public void startTimer() {
        // Get the wake lock if we need to keep the screen awake.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void stopTimer() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    // Make sure we surrender the wake lock when this activity is closed.
    @Override public void onStop() {
        super.onStop();
    }
}
