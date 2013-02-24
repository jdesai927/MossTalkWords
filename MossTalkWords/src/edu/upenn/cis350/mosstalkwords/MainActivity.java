package edu.upenn.cis350.mosstalkwords;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private ImageView _imgView;
	private String _currentPath;
	private int _currentIndex;
	private Button _hintPhraseButton;
	private Button _hintRhymeButton;
	private Button _hintPronounceButton;
	private Button _micButton;
	private Button _skipButton;
	private MediaPlayer _mediaPlayer;
	
	private int _score = 0;
	private int _numHintsUsed = 0;
	private int _numTries = 0;
	private String _feedbackResult = "";
	private String[] _currentSet;
	
	
	
	private String buildUrl(String extension) {
		_currentSet = getIntent().getStringArrayExtra("edu.upenn.cis350.mosstalkwords.currentSet");
		return "https://s3.amazonaws.com/mosstalkdata/" + _currentPath + 
				"/" + _currentSet[_currentIndex] + extension;
		
	}
	
	private class LoadFilesTask extends AsyncTask<String, Integer, Drawable>{

		@Override
		protected Drawable doInBackground(String... url) {
			Drawable draw = null;
			DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpGet request = new HttpGet(url[0]);
		    HttpResponse response;
			try {
				response = httpClient.execute(request);
				InputStream is;
				is = response.getEntity().getContent();
				TypedValue typedValue = new TypedValue();
			    typedValue.density = TypedValue.DENSITY_NONE;
			    draw = Drawable.createFromResourceStream(null, typedValue, is, "src");
			//  _imgView.setImageDrawable(drawable);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		 return draw;
		}
		
	}
	
	private void loadImage() throws ClientProtocolException, IOException, InterruptedException, ExecutionException {
		 AsyncTask<String, Integer, Drawable> at = new LoadFilesTask().execute(buildUrl(".jpg"));
		 Drawable draw = at.get();
		 if (draw != null){
		 _imgView.setImageDrawable(draw);
		 }
	}
	
	private void playSound(String hint) {
		try {
			if (_mediaPlayer.isPlaying()) {
				_mediaPlayer.stop();
			}
			_mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			_mediaPlayer.setDataSource("https://s3.amazonaws.com/mosstalkdata/nonlivingthingshard/boomerang.wav");
			//_mediaPlayer.setDataSource(buildUrl(hint + ".wav"));
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
        _imgView = (ImageView) findViewById(R.id.image);
        _currentIndex = 0;
       
        _currentPath = getIntent().getStringExtra("edu.upenn.cis350.mosstalkwords.currentSetPath");
        try {
			try {
				loadImage();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        _hintPhraseButton = (Button) findViewById(R.id.hintbuttona);
        _hintRhymeButton = (Button) findViewById(R.id.hintbuttonb);
        _hintPronounceButton = (Button) findViewById(R.id.hintbuttonc);
        _micButton = (Button) findViewById(R.id.micbutton);
        _skipButton = (Button) findViewById(R.id.skipbutton);
        
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        
        _hintPhraseButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("phrase");
				_numHintsUsed++;
			}
		});
        
        _hintRhymeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("rhyme");
				_numHintsUsed++;
			}
		});
        
        _hintPronounceButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("");
				_numHintsUsed++;
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
				
				_numHintsUsed = 0;
				_numTries = 0;
				try {
					loadImage();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
                
                String correctAnswer = _currentSet[_currentIndex];
                
                for(String str: result)
                {
                	if(str.equals(correctAnswer))
                	{
                		giveFeedback(true, str);
                		return;
                	}
                }
                
                giveFeedback(false, result.get(0));
            }
            break;
        }
 
        }
    }
    
    

    /**
	 * Method that displays a dialog showing the user whether they said the right 
	 * answer or not, and giving them the option of continuing or trying again.
	 * @param isSuccess  whether or not the user said the correct word
	 * @param word_said  the word that the user said
	 */
	private void giveFeedback(boolean isSuccess, String word_said) {
		
		//build the dialog
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		
		b.setCancelable(false);
		
		
		if(isSuccess) {  //only give them continue button if they got it right
			b.setTitle("Correct!");
			b.setIcon(R.drawable.checkmark);
			b.setMessage("You said: " + word_said);
			
			b.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					_feedbackResult="continue";
					_score += 3-_numHintsUsed;
		        	TextView st = (TextView) findViewById(R.id.score);
		        	st.setText(Integer.toString(_score));

					_numTries = 0;
		        	_numHintsUsed = 0;
				}
			});
			
		}
		else if(isSuccess == false && _numTries >= 3) {  //got it wrong, but time to move on
			b.setTitle("Try the next picture!");
			b.setIcon(R.drawable.wrong);
			b.setMessage("The correct answer was: " + _currentSet[_currentIndex]);
			
			b.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					_feedbackResult="continue";
					
					_numTries = 0;
		        	_numHintsUsed = 0;
				}
			});
		}
		else {
			b.setTitle("Not quite!");
			b.setIcon(R.drawable.wrong);
			b.setMessage("You said: " + word_said);

			b.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					_feedbackResult="again";
					_numTries++;
				}
			});
		}
		

		b.show();  //show the dialog
		
		
		//play the audio feedback
		if(isSuccess) {
			MediaPlayer mp = MediaPlayer.create(this, R.raw.correct);
			mp.start();
		}
			
	}

}
