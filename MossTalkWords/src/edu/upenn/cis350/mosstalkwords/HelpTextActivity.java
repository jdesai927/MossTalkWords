package edu.upenn.cis350.mosstalkwords;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelpTextActivity extends Activity {
	
	private Button _backButton;
	private TextView _helpText;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        
        _backButton = (Button) findViewById(R.id.backbutton);
        
        _backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
        
        _helpText = (TextView) findViewById(R.id.helptext);

        _helpText.setText("How to play: Tap the button with the microphone icon on the bottom" + 
                          " of the screen to start speaking. After you have spoken the word associated" +
        		          " with the picture, you will be told whether the word you spoke was correct or" +
                          " incorrect. You may attempt to pronounce the word associated with the picture " +
        		          "up to three times.\n\n" +
                          "Hints: The hint buttons are located on the top part of the screen.\n" +
                          "Tap the 'Phrase' button to hear the word associated with " +
        		          "the picture used in a phrase.\nTap the 'Word' button to hear the word " +
                          "associated with the picture pronounced.\nTap the 'Rhyme' button to hear " +
        		          "a word that rhymes with the word pronounced.\n\nSkipping: Tap the 'Skip' " +
                          "button to skip to the next picture. This will end your current streak and you" +
        		          " will not receive points for the word.");
	}

}
