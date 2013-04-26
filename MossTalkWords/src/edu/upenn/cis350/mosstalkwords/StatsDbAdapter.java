package edu.upenn.cis350.mosstalkwords;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StatsDbAdapter extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "gamestats";
 
    // Stats table name
    private static final String TABLE_STATS = "stats";
 
    // Stats Table Columns names
    private static final String KEY_WORD = "word";
    private static final String KEY_HINT_WORD = "hint_word";
    private static final String KEY_HINT_PHRASE = "hint_phrase";
    private static final String KEY_HINT_RHYME = "hint_rhyme";
    private static final String KEY_NUM_HINTS = "num_hints";
    private static final String KEY_NUM_TRIES = "num_tries";
    private static final String KEY_GUESS = "guess";
    private static final String KEY_SUCCESS = "sucess";
    
    private SQLiteDatabase db;
 
    private Context ctx;
    
    public StatsDbAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	String CREATE_STATS_TABLE = String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY NOT NULL, " +
    			"%s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL, %s INTEGER NOT NULL)",
    			TABLE_STATS, KEY_WORD, KEY_NUM_TRIES, KEY_NUM_HINTS, KEY_HINT_WORD, KEY_HINT_PHRASE,
    			KEY_HINT_RHYME, KEY_GUESS, KEY_SUCCESS);
    	Log.d("statsdbadapter", "CREATE TABLE STATEMENT ==== "+CREATE_STATS_TABLE);
        db.execSQL(CREATE_STATS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
 
        Log.d("statsdbadapter", "onUpgrade called!");
        // Create tables again
        onCreate(db);
    }
    
    public void open()
    {
    	db = this.getWritableDatabase();
    }
    
    public void close()
    {
    	db.close();
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addStat(String word, int num_tries, int num_hints, int hint_word, int hint_phrase,
    		int hint_rhyme, String guess, int success)
    {
    	ContentValues values = new ContentValues();
    	values.put(KEY_WORD, word);
    	values.put(KEY_NUM_TRIES, num_tries);
    	values.put(KEY_NUM_HINTS, num_hints);
    	values.put(KEY_HINT_WORD, hint_word);
    	values.put(KEY_HINT_PHRASE, hint_phrase);
    	values.put(KEY_HINT_RHYME, hint_rhyme);
    	values.put(KEY_GUESS, guess);
    	values.put(KEY_SUCCESS, success);
    	
    	db.insert(TABLE_STATS, null, values);
    }
    
    public String getStats()
    {
    	Cursor cursor = db.query(TABLE_STATS, new String[] {KEY_WORD, KEY_NUM_TRIES, KEY_NUM_HINTS, KEY_HINT_WORD, KEY_HINT_PHRASE,
    			KEY_HINT_RHYME, KEY_GUESS, KEY_SUCCESS}, null, null, null, null, null);
    	
    	StringBuffer email_body = new StringBuffer();
        if(cursor != null)
        {
        	email_body.append("WORD\tUSER GUESS\tNUM OF TRIES\tNUM HINTS USED\tRHYME USED\tPHRASE USED\tWORD USED\tSUCCEEDED\n");
        	while(cursor.moveToNext())
        	{
        		String word = cursor.getString(cursor.getColumnIndex(KEY_WORD));
        		String guess = cursor.getString(cursor.getColumnIndex(KEY_GUESS));
        	    int number_tries = cursor.getInt(cursor.getColumnIndex(KEY_NUM_TRIES));
        		int num_hints = cursor.getInt(cursor.getColumnIndex(KEY_NUM_HINTS));
        		boolean rhyme_used = (cursor.getInt(cursor.getColumnIndex(KEY_HINT_RHYME)) == 1) ? true : false;
        		boolean phrase_used = (cursor.getInt(cursor.getColumnIndex(KEY_HINT_PHRASE)) == 1) ? true : false;
        		boolean word_used = (cursor.getInt(cursor.getColumnIndex(KEY_HINT_WORD)) == 1) ? true : false;
        	    boolean success = (cursor.getInt(cursor.getColumnIndex(KEY_SUCCESS)) == 1) ? true : false;
        				
        		email_body.append(word + "\t" + guess + "\t" + number_tries + "\t" + num_hints + "\t\t" + 
        						rhyme_used + "\t" + phrase_used +"\t" + word_used + "\t" + success + "\n");
        		Log.d("statsdbadapter", word + "\t" + guess + "\t" + number_tries + "\t\t" + num_hints + "\t\t" + 
								rhyme_used + "\t" + phrase_used +"\t" + word_used + "\t" + success + "\n");
        	}
        	String result = email_body.toString();
        	return result;
        }
        
        return new String();
 
    }
    
//    public void addScore(String name, int score)
//    {
//    	ContentValues values = new ContentValues();
//    	values.put(KEY_NAME, name);
//    	values.put(KEY_SCORE, score);
//    	
//    	db.insert(TABLE_SCORES, null, values);
//    }
//    
//    public int getScore(String name)
//    {
//    	Cursor cursor = db.query(TABLE_SCORES, new String[] {KEY_NAME, KEY_SCORE}, KEY_NAME + "= '" + name + "'", null, null, null, null);
//    	if (cursor != null)
//    	{
//    		if(cursor.moveToFirst())
//    		{
//	    		int score = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
//	    		cursor.close();
//	    		return score;
//    		}
//    	}
//    	return -1;
//    }
//    
//    public void updateScore(String name, int score)
//    {
//    	ContentValues values = new ContentValues();
//    	values.put(KEY_NAME, name);
//    	values.put(KEY_SCORE, score);
//    	
//    	db.update(TABLE_SCORES, values, KEY_NAME + "= '" + name + "'", null);
//    }
    
}