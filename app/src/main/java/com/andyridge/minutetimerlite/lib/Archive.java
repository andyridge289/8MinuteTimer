package com.andyridge.minutetimerlite.lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class Archive implements Constants 
{	
	public static void saveMessage(int exercise, int numComplete, long time)
	{
		Log.d(TAG, "" + exercise + " " + numComplete + " " + time);
	}
	
	// TODO Create database + table
	// TODO Make way of clearning table
	// TODO Make saveMessage add to the database
	
	private class DBHelper extends SQLiteOpenHelper {

	    private static final int DATABASE_VERSION = 1;
	    private static final String DATABASE_NAME = "8MinuteStorage";
	    
	    private static final String DICTIONARY_TABLE_NAME = "dictionary";
	    private static final String DICTIONARY_TABLE_CREATE =
	                "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" + " TEXT, " +" TEXT);";

	    DBHelper(Context context) 
	    {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) 
	    {
	        db.execSQL(DICTIONARY_TABLE_CREATE);
	    }

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) 
		{
			// TODO Auto-generated method stub
			
		}
	}
}
