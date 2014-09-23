package com.andyridge.minutetimerlite;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

import com.andyridge.minutetimerlite.lib.Constants;

import java.util.ArrayList;

import static com.andyridge.minutetimerlite.lib.Constants.RUNNING;
import static com.andyridge.minutetimerlite.lib.Constants.TONES;
import static com.andyridge.minutetimerlite.lib.Constants.TAG;

public class Timer implements Runnable {

    private boolean stop = false;

    private static Timer instance;

    private Constants.Exercise exercise;
    private int index;
    private int currentTime;

    private ArrayList<Long> times;

    private final ToneGenerator t = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);

    private TimerFragment tf;

    private Timer(Constants.Exercise exercise, int index, int currentTime, TimerFragment tf) {
        this.currentTime = currentTime;
        this.index = index;
        this.exercise = exercise;
        this.tf = tf;
        times = new ArrayList<Long>();
    }

    public static Timer getInstance(Constants.Exercise exercise, int index, int currentTime, TimerFragment tf, boolean reset) {
        if(instance == null) {
            instance = new Timer(exercise, index, currentTime, tf);
        } else if (reset || instance.exercise.index != exercise.index) {
            instance.setData(exercise, index, currentTime, tf);
        } else {
            instance.setFragment(tf);
        }

        return instance;
    }

    void setData(Constants.Exercise exercise, int index, int currentTime, TimerFragment tf) {
        this.exercise = exercise;
        this.index = index;
        this.currentTime = currentTime;
        this.tf = tf;
    }

    void setFragment(TimerFragment tf) {
        this.tf = tf;
    }

    public void stop() {
        stop = true;
    }
    public void unStop() {
        stop = false;
    }
    public boolean stopped() {
        return stop;
    }

    @Override
    public void run() {
        if(tf.getState() > RUNNING || stop) {
            Log.d(TAG, "Stopping " + tf.getState() + " - " + stop);
            // If we are either stopped or paused then we shouldn't tick.
            // we should also surrender the sleep lock
            return;
        }

        if(times.size() > 0) {
            long lastTime = times.get(times.size() - 1);
            if(System.currentTimeMillis() - lastTime < 1000) {
                return;
            }
        }

        times.add(System.currentTimeMillis());

        // Work out what to set the text to, and whether to stop
        if(currentTime < exercise.timings[index]) {
            tf.setPie(currentTime, exercise.timings[index], index);

            if(currentTime == 0 && HomeActivity.readAloud) {
                HomeActivity.speak(tf.getActivity(), exercise.names[index]);
            }

            currentTime++;
            if (!stop)
                tf.getHandler().postDelayed(this, 1000);
        } else if(index < exercise.timings.length - 1) {
            // Create a new fragment_timer for the next exercise.
            index++;
            currentTime = 0;

            if(!HomeActivity.readAloud) {
                t.startTone(TONES[HomeActivity.sound], 1000);
            }

            if (!stop) {
                tf.startTimer(index, true);
            }
        } else {
            t.startTone(TONES[HomeActivity.sound], 1000);
            tf.done();
        }

    }
}
