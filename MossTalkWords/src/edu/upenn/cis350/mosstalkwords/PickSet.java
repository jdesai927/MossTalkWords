package edu.upenn.cis350.mosstalkwords;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class PickSet extends Activity {
	public final static String currentSetPath = "edu.upenn.cis350.mosstalkwords.currentSetPath";
	public final static String currentSet = "edu.upenn.cis350.mosstalkwords.currentSet";
	public final static String newScore = "edu.upenn.cis350.mosstalkwords.newScore";
	private Spinner stimspinner;
	private Spinner diffspinner;
	private String difficulty;
	private String category;
	private int savedscore;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		AsyncTask<String, Integer, Boolean> downloadOneFile = new LoadOneFile().execute("");
		savedscore = getIntent().getIntExtra("edu.upenn.cis350.mosstalkwords.currentSavedScore", 0);
		
		setContentView(R.layout.activity_pick);
		
		stimspinner = (Spinner) findViewById(R.id.set_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.stimulus_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stimspinner.setAdapter(adapter);

		diffspinner = (Spinner) findViewById(R.id.difficulty_spinner);
		ArrayAdapter<CharSequence> diffadapter = ArrayAdapter.createFromResource(this,
		        R.array.difficulty_array, android.R.layout.simple_spinner_item);
		diffadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		diffspinner.setAdapter(diffadapter);
		stimspinner.setOnItemSelectedListener(new CategorySelectedListener());
		diffspinner.setOnItemSelectedListener(new DifficultySelectedListener());
	
	}
	
	public void onStartButtonClick(View view){
		
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra(currentSetPath, category+difficulty);
		i.putExtra(currentSet, getSetArray(category+difficulty));
		i.putExtra(newScore, savedscore);
		startActivity(i);
	}
	
	public String[] getSetArray(String key){
		
		if (key.equals("nonlivingthingseasy")){
			return getResources().getStringArray(R.array.nonlivingthingseasy);
		}
		else if(key.equals("livingthingseasy")){
			return getResources().getStringArray(R.array.livingthingseasy);
		}
		else if(key.equals("nonlivingthingshard")){
			return getResources().getStringArray(R.array.nonlivingthingshard);
		}
		else{
			return getResources().getStringArray(R.array.livingthingshard);
		}
	}
	public class DifficultySelectedListener implements OnItemSelectedListener {
		
		@SuppressLint("DefaultLocale")
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			 difficulty = parent.getItemAtPosition(pos).toString();	
			 difficulty = difficulty.replaceAll("\\s","");
			 difficulty = difficulty.toLowerCase();
		 }
		  
		 @Override
		 public void onNothingSelected(AdapterView<?> arg0) {}
		 
		}
	
	
	public class CategorySelectedListener implements OnItemSelectedListener {
		 
		@SuppressLint("DefaultLocale")
		public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			 category = parent.getItemAtPosition(pos).toString();	
			 category = category.replaceAll("\\s","");
			 category = category.toLowerCase();
		}
		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
		 
		}
	
	private class LoadOneFile extends AsyncTask<String, Integer, Boolean>{
		@Override
		protected Boolean doInBackground(String... set) {
			boolean b = false;
			String [] categories = {"nonlivingthingseasy", "nonlivingthingshard"};//, "livingthingseasy", "livingthingshard"};
			try {
				for (int i = 0; i< categories.length; i++){
					URL ur = new URL("https://s3.amazonaws.com/mosstalkdata/" + categories[i] + 
				"/" + getSetArray(categories[i])[0] + ".jpg");
					
					File file = new File(getApplicationContext().getCacheDir(),  getSetArray(categories[i])[0] +".jpg");
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
				 
			}catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		 return b;
		}
		
	}
}
