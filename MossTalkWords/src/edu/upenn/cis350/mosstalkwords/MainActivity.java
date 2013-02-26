package edu.upenn.cis350.mosstalkwords;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	public final static String currentSavedScore = "edu.upenn.cis350.mosstalkwords.currentSavedScore";
	private ImageView _imgView;
	private String _currentPath;
	private int _currentIndex;
	private Button _hintPhraseButton;
	private Button _hintRhymeButton;
	private Button _hintPronounceButton;
	private Button _micButton;
	private Button _skipButton;
	private MediaPlayer _mediaPlayer;
	
	public int _score = 0;
	public int _numHintsUsed = 0;
	private int _numTries = 0;
	private String _feedbackResult = "";
	public AlertDialog ad;
	
	private String[] _currentSet;

	private String buildUrl(String extension) {
		_currentSet = getIntent().getStringArrayExtra("edu.upenn.cis350.mosstalkwords.currentSet");
		return "https://s3.amazonaws.com/mosstalkdata/" + _currentPath + 
				"/" + _currentSet[_currentIndex] + extension;
		
	}
	private String buildCachePath(String extension){
		if(getApplicationContext() != null){
			if(getApplicationContext().getCacheDir() != null){
				if(getApplicationContext().getCacheDir().getPath() != null){
					if(_currentSet == null){
						return "curr set null!";
					}
						else
						{
							return getApplicationContext().getCacheDir().getPath()+"/"+_currentSet[_currentIndex]+extension; 
						}
					
				}
				else{
					return "path";
				}
			}
			else{
				return "get Cache Dir";
			}
		}
		else{
			return "App context";
		}
	}
	
	private class LoadFilesTask extends AsyncTask<String, Integer, Boolean>{
		@Override
		protected Boolean doInBackground(String... set) {
			boolean b = false;
			String [] extensions = {".jpg", "_phrase.wav", "_rhyme.wav", ".wav"};
			try {
			 for(int j = 0; j< extensions.length; j++){
				String extension = extensions[j];
				for (int i = 0; i<_currentSet.length; i++){
					URL ur = new URL("https://s3.amazonaws.com/mosstalkdata/" + _currentPath + 
				"/" + _currentSet[i] + extension);
					File file = new File(getApplicationContext().getCacheDir(),_currentSet[i]+extension);
					URLConnection ucon = ur.openConnection();
					InputStream is = ucon.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is);
					ByteArrayBuffer baf = new ByteArrayBuffer(50);
					int current = 0;
					while ((current = bis.read()) != -1)
						baf.append((byte) current);
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(baf.toByteArray());
					fos.close();
					b = true;
				}
				
			} 
			}catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		 return b;
		}
		
	}
	
	private void loadImage() throws ClientProtocolException, IOException, InterruptedException, ExecutionException {	
		BitmapFactory.Options options = new BitmapFactory.Options();
		 options.inSampleSize = 2;
		 options.outHeight = (_imgView.getHeight())/2;
		 options.outWidth= _imgView.getWidth();
		Bitmap myBitmap= null;
		 try{
			 myBitmap = BitmapFactory.decodeFile(buildCachePath(".jpg"),options);
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
			
		 if (myBitmap != null){
		_imgView.setScaleType(ScaleType.FIT_XY);
		 _imgView.setImageBitmap(myBitmap);
		 }
	}
	
	
	private void playSound(String hint) {
			
		try {
			if (_mediaPlayer.isPlaying()) {
				_mediaPlayer.stop();
			}
			
			_mediaPlayer.reset();
			//_mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			_mediaPlayer.setDataSource("https://s3.amazonaws.com/mosstalkdata/" + _currentPath + 
					"/" + _currentSet[_currentIndex] + hint + ".wav");
			//_mediaPlayer.setDataSource("https://s3.amazonaws.com/mosstalkdata/nonlivingthingseasy/banana.wav");
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
        _currentSet = getIntent().getStringArrayExtra("edu.upenn.cis350.mosstalkwords.currentSet");
        _score = getIntent().getIntExtra("edu.upenn.cis350.mosstalkwords.newScore", 0);
        TextView st = (TextView) findViewById(R.id.score);
    	st.setText(Integer.toString(_score));
        AsyncTask<String, Integer, Boolean> downloadFiles = new LoadFilesTask().execute("");
        
    	try {
			loadImage();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				playSound("_phrase");
				if(_numHintsUsed < 3)
					_numHintsUsed++;	
			}
		});
        
        _hintRhymeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("_rhyme");
				if(_numHintsUsed < 3)
					_numHintsUsed++;
			}
		});
        
        _hintPronounceButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playSound("");
				if(_numHintsUsed < 3)
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
				nextImage();
			}
		});
        
    }
    
    public void nextImage(){
    	_currentIndex++;
		
		if(checkEndOfSet() == true){
			return;
		}
		else{
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
		_numHintsUsed = 0;
		_numTries = 0;
		}
    }
    
    private boolean checkEndOfSet(){
    	boolean end = false;
    	if(_currentIndex >= _currentSet.length){
    		end = true;
    		Intent i = new Intent(this, PickSet.class);
    		i.putExtra(currentSavedScore, _score);
    		startActivity(i);
    	}
    	return end;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case 1: {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String correctAnswer = _currentSet[_currentIndex];
                
                for(String str: result)
                {
                	if(str.equals(correctAnswer) || str.contains(correctAnswer))
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
		        	nextImage();
					
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
					nextImage();
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
		
		ad = b.create();
		ad.show();  //show the dialog
		
		//play the audio feedback
		if(isSuccess) {
			MediaPlayer mp = MediaPlayer.create(this, R.raw.correct);
			mp.start();
		}
			
	}
	
	 public static void trimCache(Context context) {
	      try {
	         File dir = context.getCacheDir();
	         if (dir != null && dir.isDirectory()) {
	            deleteDir(dir);
	         }
	      } catch (Exception e) {
	         // TODO: handle exception
	      }
	   }

	   public static boolean deleteDir(File dir) {
	      if (dir != null && dir.isDirectory()) {
	         String[] children = dir.list();
	         for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	               return false;
	            }
	         }
	      }

	      // The directory is now empty so delete it
	      return dir.delete();
	   }
	   
	   @Override
	   protected void onDestroy() {
		  _currentIndex = 0;
	      super.onDestroy();
	      try {
	         trimCache(this);
	      } catch (Exception e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	   }

}
