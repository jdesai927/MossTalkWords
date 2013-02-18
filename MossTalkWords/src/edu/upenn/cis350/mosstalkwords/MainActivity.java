package edu.upenn.cis350.mosstalkwords;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private ImageView _imgView;
	private String _currentPath;
	private int _currentIndex;
	private Button _hintAButton;
	private Button _hintBButton;
	private Button _hintCButton;
	private Button _micButton;
	private Button _skipButton;
	private MediaPlayer _mediaPlayer;
	
	private String buildUrl(String extension) {
		return "https://s3.amazonaws.com/" + _currentPath + "/" + _currentIndex + extension;
	}
	
	private void loadImage() {
		_imgView.setImageBitmap(BitmapFactory.decodeFile(buildUrl(".jpg")));
	}
	
	private void playSound(String hint) {
		try {
			_mediaPlayer.setDataSource(buildUrl(hint + ".wav"));
			_mediaPlayer.prepare();
			_mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        _currentIndex = 1;
        _currentPath = getIntent().getStringExtra("currentSetPath");
        
        loadImage();
        
        _hintAButton = (Button) findViewById(R.id.hintbuttona);
        _hintBButton = (Button) findViewById(R.id.hintbuttonb);
        _hintCButton = (Button) findViewById(R.id.hintbuttonc);
        _micButton = (Button) findViewById(R.id.micbutton);
        _skipButton = (Button) findViewById(R.id.skipbutton);
        
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        
        _hintAButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("a");
			}
		});
        
        _hintBButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("b");
			}
		});
        
        _hintCButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("c");
			}
		});
        
        _micButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		    	
		        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

		        try {
		            startActivityForResult(intent, 1);
		        } catch (ActivityNotFoundException a) {
		        	a.printStackTrace();
		            Toast.makeText(getApplicationContext(),
				                   "Oops! Your device doesn't support Speech to Text",
				                   Toast.LENGTH_SHORT).show();
		        }
			}
		});
        
        _skipButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				_currentIndex++;
				loadImage();
			}
		});
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case 1: {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Toast.makeText(getApplicationContext(),
		                       "Your result is: " + result.get(0),
		                       Toast.LENGTH_LONG).show();
            }
            break;
        }
 
        }
    }

}
