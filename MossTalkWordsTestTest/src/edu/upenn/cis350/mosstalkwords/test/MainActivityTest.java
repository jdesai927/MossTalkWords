package edu.upenn.cis350.mosstalkwords.test;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import edu.upenn.cis350.mosstalkwords.MainActivity;
import edu.upenn.cis350.mosstalkwords.PickSet;
import edu.upenn.cis350.mosstalkwords.R;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity act;
	private Button hintPhraseButton;
	private Button hintRhymeButton;
	private Button hintPronounceButton;
	private Button skipButton;
	
	public MainActivityTest() {
		super("edu.upenn.cis350.mosstalkwords", MainActivity.class);
	}
	
	public void setUp() {		
		//create a new intent to manually tell MainActivity what stimulus set to use
		//  (this substitutes for what PickSet would normally pass to MainActivity)
		Intent i = new Intent();
		i.putExtra(PickSet.currentSetPath, "nonlivingthingshard");
		i.putExtra(PickSet.currentSet, 
				getInstrumentation().getTargetContext().getApplicationContext().getResources().getStringArray(
						edu.upenn.cis350.mosstalkwords.R.array.nonlivingthingshard));
		
		setActivityIntent(i);  //later MainActivity calls to getIntent will return this (we need this to 
								// initialize set directories, etc

		act = (MainActivity) this.getActivity();
		
		hintPhraseButton = (Button) act.findViewById(R.id.hintbuttona);
        hintRhymeButton = (Button) act.findViewById(R.id.hintbuttonb);
        hintPronounceButton = (Button) act.findViewById(R.id.hintbuttonc);
        skipButton = (Button) act.findViewById(R.id.skipbutton);
	}
	
	//HINT TESTS DONT WORK RIGHT NOW BECAUSE MEDIA PLAYER CRASHES IN EMULATOR
//	/**
//	 * Test if clicking the Phrase hint button increments the 
//	 * numHintsUsed.
//	 */
//	public void testHintPhrase() {
//		assertEquals("hints used at startup", 0, act._numHintsUsed);
//
//		act.runOnUiThread(new Runnable() {
//			public void run() {
//				hintPhraseButton.performClick();
//			}
//		});
//		
//		getInstrumentation().waitForIdleSync();  // wait for the UI to finish
//		
//		assertEquals("hints used after phrase click", 1, act._numHintsUsed);
//	}
//	
//	/**
//	 * Test if clicking the Rhyme hint button increments the 
//	 * numHintsUsed.
//	 */
//	public void testHintRhyme() {
//		assertEquals("hints used at startup", 0, act._numHintsUsed);
//		
//		act.runOnUiThread(new Runnable() {
//			public void run() {
//				hintRhymeButton.performClick();
//			}
//		});
//		
//		getInstrumentation().waitForIdleSync();  // wait for the UI to finish
//		
//		assertEquals("hints used after rhyme click", 1, act._numHintsUsed);
//	}
//	
//	/**
//	 * Test if clicking the Pronounce hint button increments the 
//	 * numHintsUsed.
//	 */
//	public void testHintPronounce() {
//		assertEquals("hints used at startup", 0, act._numHintsUsed);
//		
//		act.runOnUiThread(new Runnable() {
//			public void run() {
//				hintPronounceButton.performClick();
//			}
//		});
//		
//		getInstrumentation().waitForIdleSync();  // wait for the UI to finish
//		
//		assertEquals("hints used after pronounce click", 1, act._numHintsUsed);
//	}
//	
	/**
	 * Test if clicking the skip button resets the numHintsUsed.
	 */
	public void testHintReset() {
		act._numHintsUsed = 3;
		
		act.runOnUiThread(new Runnable() {
			public void run() {
				skipButton.performClick();
			}
		});
		
		getInstrumentation().waitForIdleSync();  // wait for the UI to finish
			
		assertEquals("hints used after skipping", 0, act._numHintsUsed);
	}
	
	//VOICE TEST DOESNT WORK RIGHT NOW BECAUSE I CANT SEEM TO GET THE ALERT DIALOG STARTING
	//  AND CLICKING TO WORK WHEN TESTING, SO SCORE ISNT UPDATED
//	/**
//	 * Test what happens after a correct voice recognition result by simulating the
//	 * voice recognition's resulting intent and passing it to MainActivity.onActivityResult()
//	 */
//	public void testVoiceCorrect() {
//		Intent i = new Intent();
//		ArrayList<String> voice_results = new ArrayList<String>();
//		voice_results.add("freezer");
//		
//		i.putStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS, voice_results);
//		
//		act.onActivityResult(1, act.RESULT_OK, i);
//		
//		getInstrumentation().waitForIdleSync();  //wait for the AlertDialog to pop up
//		
//		act.runOnUiThread(new Runnable() {
//			public void run() {
//				act.ad.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
//			}
//		});
//		
//		getInstrumentation().waitForIdleSync();  //wait for the app to process a "continue" click
//		
//		assertEquals("score updated for correct voice result",3,act._score);
//	}
	
	
	
	
}
