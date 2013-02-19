package edu.upenn.cis350.mosstalkwords;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
	private Button _hintAButton;
	private Button _hintBButton;
	private Button _hintCButton;
	private Button _micButton;
	private Button _skipButton;
	private Button _feedbackButton;
	private MediaPlayer _mediaPlayer;
	
	private int score;
	private String feedback_result = "";
	private String [] _currentSet;
	
	
	
	private String buildUrl(String extension) {
		_currentSet = getIntent().getStringArrayExtra("edu.upenn.cis350.mosstalkwords.currentSet");
		//return "hi";
		return "https://s3.amazonaws.com/mosstalkdata/" + _currentPath + "/" + _currentSet[_currentIndex] + extension;
		
	}
	
	private void loadImage() throws ClientProtocolException, IOException {
		setImage(buildUrl(".jpg"),_imgView);
		
		//_imgView.setImageBitmap(BitmapFactory.decodeFile(buildUrl(".jpg")));
	//	_imgView.setVisibility(View.VISIBLE);
	}
	
	private void setImage(String urlStr, ImageView iv) throws ClientProtocolException, IOException {
	    DefaultHttpClient httpClient = new DefaultHttpClient();
	    HttpGet request = new HttpGet(urlStr);
	    HttpResponse response = httpClient.execute(request);
	    InputStream is = response.getEntity().getContent();
	    TypedValue typedValue = new TypedValue();
	    typedValue.density = TypedValue.DENSITY_NONE;
	    Drawable drawable = Drawable.createFromResourceStream(null, typedValue, is, "src");
	    iv.setImageDrawable(drawable);
	}
	
	private void playSound(String hint) {
		try {
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
        Toast.makeText(getBaseContext(), _currentPath, Toast.LENGTH_LONG).show();
        try {
			loadImage();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        _hintAButton = (Button) findViewById(R.id.hintbuttona);
        _hintBButton = (Button) findViewById(R.id.hintbuttonb);
        _hintCButton = (Button) findViewById(R.id.hintbuttonc);
        _micButton = (Button) findViewById(R.id.micbutton);
        _skipButton = (Button) findViewById(R.id.skipbutton);
        _feedbackButton = (Button) findViewById(R.id.feedbackbutton);
        
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        
        _hintAButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("_phrase");
			}
		});
        
        _hintBButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("_rhyme");
			}
		});
        
        _hintCButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("");
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
				try {
					loadImage();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
        
        _feedbackButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        		boolean voiceCorrect = false;  //simulating a result from the voice recognition for now
        		
		        //if voice succeeded, display feedback
		        giveFeedback(voiceCorrect,"apple",null);
		        
		        //based on what button the user pressed (saved in feedback_result), stay with
		        //  this pic or move on
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
    
    

    /**
	 * Method that displays a dialog showing the user whether they said the right 
	 * answer or not, and giving them the option of continuing or trying again.
	 * @param isSuccess  whether or not the user said the correct word
	 * @param word_said  the word that the user said
	 * @param answer  the correct answer (if null, it is ignored.  if not null, and
	 * isSuccess is false, the dialog assumes we want the user to move on and they 
	 * are shown the correct answer and only the Continue button)
	 */
	private void giveFeedback(boolean isSuccess, String word_said, String answer) {
		
		//build the dialog
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		
		b.setCancelable(false);
		
		
		if(isSuccess) {  //only give them continue button if they got it right
			b.setTitle("Correct!");
			b.setIcon(R.drawable.checkmark);
			b.setMessage("You said: " + word_said);
			
			b.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					feedback_result="continue";
					score += 1;
		        	TextView st = (TextView) findViewById(R.id.score);
		        	st.setText(Integer.toString(score));
				}
			});
			
		}
		else if(isSuccess == false && answer != null) {  //got it wrong, but time to move on
			b.setTitle("Try the next picture!");
			b.setIcon(R.drawable.wrong);
			b.setMessage("The correct answer was: " + answer);
			
			b.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					feedback_result="continue";
				}
			});
		}
		else {
			b.setTitle("Not quite!");
			b.setIcon(R.drawable.wrong);
			b.setMessage("You said: " + word_said);

			b.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					feedback_result="again";
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
