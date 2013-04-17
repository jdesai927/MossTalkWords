package edu.upenn.cis350.mosstalkwords;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EndSet extends Activity {

	public String set;
	public int setscore;
	public boolean newStreak;
	
	TextView totalscoretext;
	TextView setscoretext;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
		
		//the name of the set
		set = getIntent().getStringExtra("set");
		TextView settext = (TextView) findViewById(R.id.endset_set);
		settext.setText(getSetName(set));
		
		//the set score
		setscore = getIntent().getIntExtra("setscore", 0);
		setscoretext = (TextView) findViewById(R.id.endset_setscore);
		setscoretext.setText(Integer.toString(setscore));
		
		Scores scores = new Scores(getApplicationContext());
		
		//the high score
		TextView highscoretext = (TextView) findViewById(R.id.endset_highscore);
		highscoretext.setText("High Score: " + scores.getHighScore(set));
		if(setscore == scores.getHighScore(set) && setscore > 0) {
			//highlight the fact that they got a new high score
			highscoretext.setTextColor(Color.parseColor("#288C8C"));
			highscoretext.setTypeface(null, Typeface.BOLD);
			
			//display the high score stamp
			ImageView highscorestamp = (ImageView) findViewById(R.id.endset_stamp);
			highscorestamp.setImageResource(R.drawable.high_score_stamp);
		}
		
		//the total score  (set it to what it was before this set, because we will
		//  animate the new points)
		int prev_total = scores.getTotalScore() - setscore;
		
		totalscoretext = (TextView) findViewById(R.id.endset_totalscore);
		totalscoretext.setText(Integer.toString(prev_total));
		
		//the streak
		newStreak = getIntent().getBooleanExtra("newstreak", false);
		TextView streaktext = (TextView) findViewById(R.id.endset_streak);
		streaktext.setText("Longest Streak: " + scores.getHighestStreak());
		
		if(newStreak && scores.getHighestStreak() > 0) {
			//highlight the fact that they got a new streak 
			streaktext.setTextColor(Color.parseColor("#288C8C"));
			streaktext.setTypeface(null, Typeface.BOLD);
		}
		
		
		//call the asynctask to increment the previous total to the new total
		new IncScore().execute(prev_total, scores.getTotalScore(), setscore);
		
	}
	
	/**
	 * When user clicks finish, end this activity
	 * @param view
	 */
	public void onEndButtonClick(View view){
		setResult(RESULT_OK);
		finish();
	}
	
	
	public String getSetName(String set) {
		
		if(set.equals("livingthingseasy")) {
			return "Living Things Easy";
		}
		else if(set.equals("livingthingsmedium")) {
			return "Living Things Medium";
		}
		else if(set.equals("livingthingshard")) {
			return "Living Things Hard";
		}
		else if(set.equals("nonlivingthingseasy")) {
			return "Non Living Things Easy";
		}
		else if(set.equals("nonlivingthingsmedium")) {
			return "Non Living Things Medium";
		}
		else if(set.equals("nonlivingthingshard")) {
			return "Non Living Things Hard";
		}
		else{
			return null;
		}
		
	}
	
	
	
	/**
	 * AsyncTask class used for animating the score updating.
	 * Takes in prev_total, new_total, and set_score, and counts 
	 * prev_total up to new_total while counting set_score down to 0.
	 */
	private class IncScore extends AsyncTask<Integer, Integer, Void> {		
		
		protected Void doInBackground(Integer... scores) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			
			while(this.isCancelled() == false && scores[0] <= scores[1] && scores[2] >= 0) {	
				
				publishProgress(scores[0], scores[2]); //Android calls onProgressUpdate
				scores[0]++;
				scores[2]--;
				
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {}
			}
			
			return null;
		}
		
		
		protected void onProgressUpdate(Integer... current) {
			totalscoretext.setText(current[0].toString());
			setscoretext.setText(current[1].toString());
		}
		
		protected void onPostExecute(Void voids) {
			
		}

	}

}
