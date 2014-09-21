package com.andyridge.minutetimerlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.andyridge.minutetimerlite.lib.Archive;
import com.andyridge.minutetimerlite.lib.Constants;

import java.util.HashMap;

import static com.andyridge.minutetimerlite.lib.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TimerFragment extends Fragment {
    private static final String EXERCISE = "exercise";

    private TextView nameText;
    private TextView nextText;

    private Button startButton;
    private Button stopButton;
    private Button pauseButton;
    private Button backButton;

    private TableRow startRow;
    private TableRow stopRow;

    private PieView pie;

    private int state = STOPPED;

    private ToneGenerator t = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);

    private int[] timings;
    private String[] names;
    private String name;
    private int exercise;

    private int index = 0;
    private int pauseTime = 0;
    private int currentTime = 0;

    private Handler handler = new Handler();
    private long startTime = -1;

    public static PowerManager.WakeLock wakeLock;

    private static TextToSpeech tts;
    private HashMap<String, String> alarmStream;

    private final Runnable countdownTask = new Runnable()
    {
        @Override
        public void run()
        {
            if(state > RUNNING)
            {
                // If we are either stopped or paused then we shouldn't tick.
                // we should also surrender the sleep lock
                return;
            }

            // Work out how many seconds have elapsed since the timer started
            final long start = startTime;
            long millis = System.currentTimeMillis() - start;

            int seconds = (int) (millis / 1000);
            seconds += pauseTime;

            // Work out what to set the text to, and whether to stop
            if(seconds < timings[index])
            {
                currentTime = timings[index] - seconds;
                //countdownText.setText("" + currentTime);
                pie.goToIndex(seconds, timings[index], true);

                handler.postDelayed(this, 1000);

                if(seconds == timings[index] - 1 && index < timings.length - 1 && MinuteMenu.readAloud)
                {
                    tts.speak(names[index + 1], TextToSpeech.QUEUE_ADD, alarmStream);
                }
            }
            else
            {

                if(index < timings.length - 1)
                {
                    // Create a new timer for the next exercise.
                    pauseTime = 0;
                    index++;

                    if(!MinuteMenu.readAloud)
                    {
                        t.startTone(TONES[MinuteMenu.sound], 1000);
                    }

                    startTimer(index);
                }
                else
                {
                    // Beep
                    t.startTone(TONES[MinuteMenu.sound], 1000);
                    Archive.saveMessage(exercise, Constants.Progress.COMPLETED.ordinal(), System.currentTimeMillis());
                    // We are done.
                    doneAlert();
                }


            }
        }

    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimerFragment.
     */
    public static TimerFragment newInstance(Exercise exercise) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt(EXERCISE, exercise.index);
        fragment.setArguments(args);
        return fragment;
    }
    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exercise = getArguments().getInt(EXERCISE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        //        // Set up the text to speech service
//        tts = new TextToSpeech(this, this);
//
//        alarmStream = new HashMap<String, String>();
//        alarmStream.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
//                String.valueOf(AudioManager.STREAM_ALARM));
//
//        setVolumeControlStream(AudioManager.STREAM_ALARM);
//
//        super.onCreate(icicle);
//        setContentView(R.layout.timer);
//
//        startButton = (Button) findViewById(R.id.startButton);
//        startButton.setOnClickListener(this);
//
//        stopButton = (Button) findViewById(R.id.stopButton);
//        stopButton.setOnClickListener(this);
//
//        backButton = (Button) findViewById(R.id.backButton);
//        backButton.setOnClickListener(this);
//
//        pauseButton = (Button) findViewById(R.id.pauseButton);
//        pauseButton.setOnClickListener(this);
//
//        startRow = (TableRow) findViewById(R.id.startRow);
//        stopRow = (TableRow) findViewById(R.id.stopRow);
//
////        countdownText = (TextView) findViewById(R.id.countdown);
//        nameText = (TextView) findViewById(R.id.name);
//        nextText = (TextView) findViewById(R.id.nextText);
//
//        Bundle b = this.getIntent().getExtras();
//        timings = b.getIntArray(TIMING);
//        names = b.getStringArray(NAMES);
//        name = b.getString(NAME);
//        exercise = b.getInt(VALUE);
//
//        Log.d(TAG, name);
//
//        pie = (PieView) findViewById(R.id.pieview);
//        pie.goToStart(timings[0], true);
//
//
//        //countdownText.setText("" + timings[0]);
//        nameText.setText(names[0]);
//        nextText.setText(names[1]);
//        this.setTitle(name);
//
//        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
//
//        // Get the wake lock if we need to keep the screen awake.
//        if(MinuteMenu.keepAwake)
//        {
//            if(!wakeLock.isHeld())
//            {
//                wakeLock.acquire();
//            }
//        }

        return v;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

//    @Override
//    public void onCreate(Bundle icicle)
//    {

//    }
//
//
//    @Override
//    public void onClick(View v)
//    {
//        if(v.equals(startButton))
//        {
//            if(state == PAUSED)
//                resume();
//            else
//                start();
//        }
//        else if(v.equals(stopButton))
//        {
//            stop();
//        }
//        else if(v.equals(pauseButton))
//        {
//            pause();
//        }
//        else if(v.equals(backButton))
//        {
//            stop();
//            finish();
//        }
//    }
//
//
//    /**
//     * Starts the timer.
//     */
//    private void start()
//    {
//        pauseTime = 0;
////		countdownText.setText("" + timings[index]);
//        pie.goToStart(timings[0], true);
//
//        if(index < timings.length - 1)
//        {
//            nextText.setText(names[index + 1]);
//        }
//        else
//        {
//            nextText.setText("None");
//        }
//
//        nameText.setText(names[index]);
//
//        if(state != PAUSED && MinuteMenu.readAloud)
//            tts.speak(names[index], TextToSpeech.QUEUE_ADD, alarmStream);
//
//        state = RUNNING;
//        startRow.setVisibility(View.GONE);
//        stopRow.setVisibility(View.VISIBLE);
//
//        Archive.saveMessage(exercise, Constants.Progress.STARTED.ordinal(), System.currentTimeMillis());
//        startTimer(0);
//    }
//
//    private void resume()
//    {
//        startRow.setVisibility(View.GONE);
//        stopRow.setVisibility(View.VISIBLE);
//
//        startTimer(0);
//    }
//
//
//    /**
//     * Stops & resets the timer.
//     */
//    private void stop()
//    {
//        startRow.setVisibility(View.VISIBLE);
//        stopRow.setVisibility(View.GONE);
////		countdownText.setText("" + timings[0]);
//        pie.goToStart(timings[0], true);
//        nameText.setText(names[0]);
//        if(index < timings.length - 1)
//        {
//            nextText.setText(names[index + 1]);
//        }
//        else
//        {
//            nextText.setText("None");
//        }
//
//        state = STOPPED;
//    }
//
//
//    /**
//     * Pauses the timer
//     */
//    private void pause()
//    {
//        pauseTime = timings[index] - pie.getCurrentTime(); //Integer.parseInt((String) countdownText.getText());
//
//        startRow.setVisibility(View.VISIBLE);
//        stopRow.setVisibility(View.GONE);
//
//        state = PAUSED;
//        startButton.setText("Resume");
//    }
//
//
    /**
     * We need to make sure the timer doesn't carry on ticking when they press back. Also
     * close the activity
     */

    @Override
    public void onDestroy()
    {
        //Close the Text to Speech Library
        if(tts != null) {

            tts.stop();
            tts.shutdown();
            Log.d(TAG, "TTS Destroyed");
        }

        super.onDestroy();
    }


    /**
     * Start the timer indicated by the index.
     *
     * @param i The index of the timer to start
     */
    private void startTimer(final int i)
    {
        index = i;

        if(state == PAUSED)
        {
            state = RUNNING;
        }
        else
        {
            //countdownText.setText("" + timings[index]);
            pie.goToStart(timings[index], true);
            nameText.setText(names[index]);
        }

        if(index < timings.length - 1)
        {
            nextText.setText(names[index + 1]);
        }
        else
        {
            nextText.setText("None");
        }

        startTime = System.currentTimeMillis();
        handler.removeCallbacks(countdownTask);
        handler.postDelayed(countdownTask, 1000);
    }

    // Make sure we surrender the wake lock when this activity is closed.
    @Override public void onStop()
    {
        super.onStop();

        // May as well just check this, we always want to surrender the lock.
        if(wakeLock.isHeld())
        {
            wakeLock.release();
        }
    }

    // Show the alert to say that we've finished.
    private void doneAlert()
    {
        // Show a dialog to say everything has finished.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setMessage("Done. Return to main menu?");

        // Goes back to the main menu, apparently quitting is BAAAAAAAD.
        dialogBuilder.setPositiveButton("Main Menu", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
//                getActivity().finish();
            }
        });
        dialogBuilder.setNegativeButton("Stay Here", null);

        // Reset the page.
        startRow.setVisibility(View.VISIBLE);
        stopRow.setVisibility(View.GONE);
        pie.goToStart(timings[0], true);
        //countdownText.setText("" + timings[0]);
        nameText.setText(names[0]);
        nextText.setText(names[1]);

        // Show the alert dialog - or should that be dialogue?
        AlertDialog alert = dialogBuilder.create();
        alert.setTitle("Done");
        alert.show();
    }

    /**
     * Called when the orientation of the application changes.
     */
    @Override
    public void onConfigurationChanged(Configuration config)
    {
        super.onConfigurationChanged(config);

//        setContentView(R.layout.timer);

        /*switch(resolution)
        {
        case QVGA:
    		setContentView(R.layout.timerqvga);
        	break;

        case HVGA:
    		setContentView(R.layout.timerhvga);
        	break;

        default:
    		setContentView(R.layout.timerwvga);
        	break;
        }*/

//        startButton = (Button) findViewById(R.id.startButton);
//        startButton.setOnClickListener(this);
//
//        stopButton = (Button) findViewById(R.id.stopButton);
//        stopButton.setOnClickListener(this);
//
//        backButton = (Button) findViewById(R.id.backButton);
//        backButton.setOnClickListener(this);
//
//        pauseButton = (Button) findViewById(R.id.pauseButton);
//        pauseButton.setOnClickListener(this);
//
//        startRow = (TableRow) findViewById(R.id.startRow);
//        stopRow = (TableRow) findViewById(R.id.stopRow);
//
//        //countdownText = (TextView) findViewById(R.id.countdown);
//        nameText = (TextView) findViewById(R.id.name);
//        nextText = (TextView) findViewById(R.id.nextText);
//
//        if(index < timings.length - 1)
//            nextText.setText(names[1]);
//        else
//            nextText.setText("None");
//        nameText.setText(names[index]);
//        this.setTitle(name);
//
//        switch(state)
//        {
//            case RUNNING:
////        	countdownText.setText("" + currentTime);
//                pie.goToIndex(currentTime, timings[index], true);
//                startRow.setVisibility(View.GONE);
//                stopRow.setVisibility(View.VISIBLE);
//                break;
//            case PAUSED:
////        	countdownText.setText("" + (timings[index] - pauseTime));
//                startButton.setText("Resume");
//                break;
//            case STOPPED:
////        	countdownText.setText("" + timings[index]);
//                pie.goToStart(timings[index], true);
//                break;
//        }
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

    public void backPressed() {
        if(wakeLock.isHeld())
        {
            wakeLock.release();
        }

        state = STOPPED;
    }

//	private void animateProgressMeter()
//	  {
//	    this.animationThread = new Thread()
//	    {
//	      public boolean minRecordingLengthReached = false;
//
//	      public void run()
//	      {
//	        while(state == RUNNING)
//	          try
//	          {
//
//	            MinuteTimer.this.handler.post(MinuteTimer.this.updateProgressMeter);
//	            if (l1 >= timings[index])
//	            {
//	            	MinuteTimer.this.recordingProgressView.sweepAngle = 0;
//
//	            }
//	            else
//	            {
////	              if ((!this.minRecordingLengthReached) && (l1 > MinuteTimer.this.min_recording_time))
////	            	  MinuteTimer.this.onMinRecordingLengthReached();
//	              Thread.sleep(100L);
//	            }
//	          }
//	          catch (InterruptedException localInterruptedException)
//	          {
//	          }
//	      }
//	    };
//	    this.animationThread.start();
//	  }

}
