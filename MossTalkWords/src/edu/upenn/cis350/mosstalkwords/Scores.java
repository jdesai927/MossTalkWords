package edu.upenn.cis350.mosstalkwords;

import java.io.Serializable;

public class Scores implements Serializable {
	
	private static final long serialVersionUID = -3552875309528922831L; //for serialization
	
	private int total_score;
	private int [] high_scores;
		
	/**
	 * Initialize a new Scores object for keeping track of a user's scores.
	 */
	public Scores () {
		total_score = 0;
		high_scores = new int[6];
	}
	
	
	/**
	 * Returns the total score (accumulated over all of the 
	 * games the user has played)
	 * @return the total score
	 */
	public int getTotalScore() {
		return total_score;
	}
	
	
	/**
	 * Set the total score
	 * @param val the new value 
	 * @return true if success, false if val is < 0
	 */
	public boolean setTotalScore(int val) {
		if(val > 0) {
			total_score = val;
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * Given a stimulus set, returns the high score for that set
	 * @param set the name of the stimulus set
	 * @return the high score, or -1 if set was not a valid name
	 */
	public int getHighScore(String set) {
		
		if(set.equals("livingthingseasy")){
			return high_scores[0];
		}
		else if (set.equals("livingthingsmedium")) {
			return high_scores[1];
		}
		else if (set.equals("livingthingshard")) {
			return high_scores[2];
		}
		else if(set.equals("nonlivingthingseasy")){
			return high_scores[3];
		}
		else if (set.equals("nonlivingthingsmedium")) {
			return high_scores[4];
		}
		else if (set.equals("nonlivingthingshard")) {
			return high_scores[5];
		}
		else {
			return -1;
		}
		
	}
	
	
	/**
	 * Sets the high score of a stimulus set.
	 * @param set  The stimulus set to set
	 * @param val  The value of the new high score
	 * @return true if success, false if val is negative or if 
	 * the set does not exist
	 */
	public boolean setHighScore(String set, int val) {
		
		if(val < 0)
			return false;
		
		if(set.equals("livingthingseasy")){
			high_scores[0] = val;
			return true;
		}
		else if (set.equals("livingthingsmedium")) {
			high_scores[1] = val;
			return true;
		}
		else if (set.equals("livingthingshard")) {
			high_scores[2] = val;
			return true;
		}
		else if(set.equals("nonlivingthingseasy")){
			high_scores[3] = val;
			return true;
		}
		else if (set.equals("nonlivingthingsmedium")) {
			high_scores[4] = val;
			return true;
		}
		else if (set.equals("nonlivingthingshard")) {
			high_scores[5] = val;
			return true;
		}
		else {
			return false;
		}
		
	}
	
	

}
