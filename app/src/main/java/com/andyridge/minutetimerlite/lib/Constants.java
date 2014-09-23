package com.andyridge.minutetimerlite.lib;

import java.util.Locale;

import android.media.ToneGenerator;

public abstract class Constants
{
	// TODO Fix the timings for the 8 minute buns

    public static enum Exercise {
        ABS(0, "8 Minute Abs", TIMING_ABS, NAME_ABS),
        ARMS(1, "8 Minute Arms", TIMING_ARMS, NAME_ARMS),
        LEGS(2, "8 Minute Legs", TIMING_LEGS, NAME_LEGS),
        BUNS(3, "8 Minute Buns", TIMING_BUNS, NAME_BUNS),
        STRETCH(4, "8 Minute Stretch", TIMING_STRETCH, NAME_STRETCH);

        public final int index;
        public final String name;
        public final int[] timings;
        public final String[] names;

        Exercise(int index, String name, int[] timings, String[] names) {
            this.index = index;
            this.name = name;
            this.timings = timings;
            this.names = names;
        }
    }
	
	public static final String TAG = "8MIN";

	public static final int RUNNING = 0;
	public static final int PAUSED = 1;
	public static final int STOPPED = 2;
	
	public static final int[] TIMING_ABS = { 45, 45, 45, 
		  45, 45, 45, 
		  45, 45, 45,
		  45, 30 };
	public static final String[] NAME_ABS = { "Basic Crunch", "Right Oblique Crunch", "Left Oblique Crunch", 
		   "Toe Touches", "Reverse Crunch", "Right Side Crunch",
		   "Left Side Crunch", "Push Throughs", "Leg Pushes",
		   "Alternating Curls", "Curls"};

	public static final int[] TIMING_ARMS = { 30, 30, 30,
		   30, 30, 30,
		   30, 30, 30,
		   30, 30, 30,
		   30, 30, 30,
		   30 };
	public static final String[] NAME_ARMS = { "Push Ups", "Flys", "Chest Press",
			"Standing Rows Right", "Standing Rows Left", "Upright Rows",
			"Lateral Raise", "Shoulder Press", "Front Raise",
			"Rear Raise", "Bicep Curls", "Tricep Kickback Right",
			"Tricep Kickback Left", "Knee Bicep Curl Right", "Knee Bicep Curl Left",
			"Tricep Extension"};

	public static final int[] TIMING_LEGS = { 30, 30, 30,
		   30, 30, 30,
		   30, 30, 30,
		   30, 30, 
		   30, 30,
		   30,
		   30, 30 };
	public static final String[] NAME_LEGS = { "Squats", "Quad Extension Left", "Quad Extension Right",
			"Outer Thigh Raise Left", "Outer Thigh Raise Right", "Inner Thigh Raise Left",
			"Inner Thigh Raise Right", "Hamstring Curl Left", "Hamstring Curl Right",
			"Outer Thigh Raise w/ Squat Left", "Outer Thigh Raise w/ Squat Right",
			"Front/Back Lunge Left", "Front/Back Lunge Right",
			"Bent Leg Inner Thigh Raise Left",
			"Bent Leg Inner Thigh Raise Right", "Plie w/ Calf Raise"};

	public static final int[] TIMING_BUNS = 
	{ 
		45, 45, 45, 
		65, 60, 
		60, 60,
		60, 60 
	};
	
	public static final String[] NAME_BUNS = 
	{ 
		"Basic Squat", "Left Lunges", "Right Lunges", 
		"Left Butt Kick", "Right Butt Kick", 
		"Left Bent Leg Press", "Right Bent Leg Press",
		"Left Butt Lift", "Right Butt Lift"
	};
	
	public static final int[] TIMING_STRETCH =
	{
		20, 20, 
		20, 20,
		20, 20, 
		
		20, 20,
		20, 20,
		20, 20,
		
		20, 20,
		20, 20,
		20, 20,
		
		20, 20,
		20, 20,
		20, 20
	};
	public static final String[] NAME_STRETCH =
	{
		"Neck-Right", "Neck-Left", 
		"Side Stretch", "Lumbar Roll",
		"Shoulder-Right", "Shoulder-Left", 
		
		"Tricep-Right", "Tricep-Left",
		"Standing Quad-Right", "Standing Quad-Left",
		"Groin Stretch-Right", "Calf Stretch-Right",
		
		"Groin Stretch-Left", "Calf Stretch-Left",
		"Hamstring-Right", "Hamstring-Left",
		"Buttocks-Right", "Buttocks-Left",
		
		"Hamstring Pull-Right", "Crossover-Right",
		"Hamstring Pull-Left", "Crossover-Left",
		"Knees to Chest", "Rack Stretch"
	};
	
	public static final int[] TONES = { ToneGenerator.TONE_CDMA_MED_L,
										ToneGenerator.TONE_CDMA_LOW_L,
										ToneGenerator.TONE_CDMA_HIGH_L,
										ToneGenerator.TONE_SUP_PIP,}; 
	
	public static final Locale[] LOCALES = { Locale.getDefault(), Locale.UK, Locale.US };
}
