package edu.upenn.cis350.mosstalkwords;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndSet extends Activity {

	public String set;
	public int setscore;
	public boolean newStreak;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
		
		//the name of the set
		set = getIntent().getStringExtra("set");
		TextView settext = (TextView) findViewById(R.id.endset_set);
		settext.setText(getSetName(set));
		
		//the set score
		setscore = getIntent().getIntExtra("setscore", 0);
		TextView setscoretext = (TextView) findViewById(R.id.endset_setscore);
		setscoretext.setText("Set Score: " + setscore);
		
		Scores scores = new Scores(getApplicationContext());
		
		//the high score
		TextView highscoretext = (TextView) findViewById(R.id.endset_highscore);
		highscoretext.setText("High Score: " + scores.getHighScore(set));
		if(setscore == scores.getHighScore(set) && setscore > 0) {
			//highlight the fact that they got a new high score
			highscoretext.setTextColor(Color.rgb(20, 230, 50));
		}
		
		//the total score
		TextView totalscoretext = (TextView) findViewById(R.id.endset_totalscore);
		totalscoretext.setText("Total Score: " + scores.getTotalScore());
		
		//the streak
		newStreak = getIntent().getBooleanExtra("newstreak", false);
		TextView streaktext = (TextView) findViewById(R.id.endset_streak);
		streaktext.setText("Longest Streak: " + scores.getHighestStreak());
		if(newStreak && scores.getHighestStreak() > 0) {
			//highlight the fact that they got a new streak 
			streaktext.setTextColor(Color.rgb(20, 230, 50));
		}
		
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

}
