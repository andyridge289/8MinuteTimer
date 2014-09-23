package com.andyridge.minutetimerlite;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import static com.andyridge.minutetimerlite.lib.Constants.Exercise;
import static com.andyridge.minutetimerlite.lib.Constants.PAUSED;
import static com.andyridge.minutetimerlite.lib.Constants.RUNNING;
import static com.andyridge.minutetimerlite.lib.Constants.STOPPED;
import static com.andyridge.minutetimerlite.lib.Constants.TAG;

public class TimerFragment extends Fragment {

    private static final String EXERCISE = "exercise";
    private static final String EXERCISE_INDEX = "exercise_index";
    private static final String STATE = "state";
    private static final String TIME = "time";

    private TextView nameText;
    private TextView nextText;

    private ImageButton startButton;
    private ImageView pauseButton;
    private ImageView stopButton;

    private PieView pie;

    private static int state = STOPPED;

    private Exercise exercise;

    private int index = 0;
    private int currentTime = 0;

    private final Handler handler = new Handler();
    private Timer countdownTask;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimerFragment.
     */
    public static TimerFragment newInstance(Exercise exercise, int[] data) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt(EXERCISE, exercise.index);

        if(data != null) {
            Log.d(TAG, "Fragment got: " + data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "]");

            args.putInt(EXERCISE_INDEX, data[1]);
            args.putInt(STATE, data[2]);
            args.putInt(TIME, data[3]);
        }

        fragment.setArguments(args);
        return fragment;
    }

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Find out what exercise we're running.
     *
     * @param savedInstanceState The instance state to restore from
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            exercise = Exercise.values()[getArguments().getInt(EXERCISE)];
            index = getArguments().getInt(EXERCISE_INDEX);
            state = getArguments().getInt(STATE, STOPPED);
            currentTime = getArguments().getInt(TIME, 0);
        }
    }

    /**
     * Initialise everything and sort the wake lock out
     *
     * @param inflater the layout inflater
     * @param container the container into which we are being put
     * @param savedInstanceState The instance state to load
     * @return The inflated fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        startButton = (ImageButton) v.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                if(state == PAUSED) {
                    resume();
                } else if(state == STOPPED) {
                    start();
                }
            }
        });

        pauseButton = (ImageView) v.findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                pause();
            }
        });

        stopButton = (ImageView) v.findViewById(R.id.stop_button);
        stopButton.setEnabled(false);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                stop();
            }
        });

        nameText = (TextView) v.findViewById(R.id.exercise_name);
        nextText = (TextView) v.findViewById(R.id.nextText);

        pie = (PieView) v.findViewById(R.id.pieview);
        pie.goToStart(exercise.timings[0]);

        nameText.setText(exercise.names[0]);
        nextText.setText(exercise.names[1]);
        getActivity().setTitle(exercise.name);

        if(index < exercise.timings.length - 1) {
            nextText.setText(exercise.names[1]);
        } else {
            nextText.setText("None");
        }

        nameText.setText(exercise.names[index]);
        getActivity().setTitle(exercise.name);

        switch(state) {
            case RUNNING:
                pie.goToIndex(currentTime, exercise.timings[index], true);
                stopButton.setEnabled(true);
                startButton.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
                break;
            case PAUSED:
                pie.goToIndex(currentTime, exercise.timings[index], true);
                stopButton.setEnabled(true);
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                break;
            case STOPPED:
                pie.goToStart(exercise.timings[index]);
                stopButton.setEnabled(false);
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                break;
        }

        // Get the thread that is doing the counting down
        countdownTask = Timer.getInstance(exercise, index, currentTime, this, false);
        handler.post(countdownTask);

        return v;
    }

    /**
     * The handler runnable tells us to redraw so that we can keep track of the changes it makes.
     *
     * @param time The time that has passed for this exercise.
     * @param total The
     */
    public void setPie(int time, int total, int index) {
        this.currentTime = time;
        this.index = index;
        pie.goToIndex(time, total, true);
    }

    /**
     * Starts the fragment_timer.
     */
    private void start() {
        pie.goToStart(exercise.timings[0]);

        if(index < exercise.timings.length - 1) {
            nextText.setText(exercise.names[index + 1]);
        } else {
            nextText.setText("None");
        }

        nameText.setText(exercise.names[index]);

        state = RUNNING;

        stopButton.setEnabled(true);
        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);

        ((HomeActivity) getActivity()).startTimer();
        startTimer(0, true);
    }

    /**
     * Resume the fragment_timer
     */
    private void resume() {
        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        stopButton.setEnabled(true);
        ((HomeActivity) getActivity()).startTimer();
        startTimer(index, false);
    }

    /**
     * Stops & resets the fragment_timer.
     */
    private void stop() {
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        stopButton.setEnabled(false);
        currentTime = 0;
        index = 0;
        countdownTask = Timer.getInstance(exercise, index, currentTime, this, true);

        pie.goToStart(exercise.timings[0]);
        nameText.setText(exercise.names[0]);
        if(index < exercise.timings.length - 1) {
            nextText.setText(exercise.names[index + 1]);
        } else {
            nextText.setText("None");
        }

        ((HomeActivity) getActivity()).stopTimer();
        state = STOPPED;
    }

    /**
     * Pauses the fragment_timer
     */
    private void pause() {
        state = PAUSED;
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).stopTimer();
    }

    public void onResume() {
        super.onResume();
        countdownTask = Timer.getInstance(this.exercise, this.index, this.currentTime, this, false);
    }

    /**
     * Start the fragment_timer indicated by the index.
     *
     * @param i The index of the fragment_timer to start
     */
    void startTimer(final int i, boolean restart)
    {
        index = i;

        if(state == PAUSED)
        {
            state = RUNNING;
        }
        else
        {
            pie.goToStart(exercise.timings[index]);
            nameText.setText(exercise.names[index]);
        }

        if(index < exercise.timings.length - 1)
        {
            nextText.setText(exercise.names[index + 1]);
        }
        else
        {
            nextText.setText("None");
        }

        if(restart)
            currentTime = 0;

        handler.removeCallbacks(countdownTask);
        handler.postDelayed(countdownTask, 1000);
    }

    /**
     *
     */
    void done()   {
        pie.goToStart(exercise.timings[0]);
        nameText.setText(exercise.names[0]);
        nextText.setText(exercise.names[1]);
        stop();
    }

    public void backPressed() {
        state = STOPPED;
    }

    public int[] getData() {
        if(exercise != null)
            return new int[]{ exercise.index, index, state, currentTime };
        else return null;
    }

    public int getState() {
        return state;
    }

    public PieView getPie() {
        return pie;
    }

    public Handler getHandler() {
        return handler;
    }
}