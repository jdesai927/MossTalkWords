package edu.upenn.cis350.mosstalkwords;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PickSet extends Activity {
	public final static String currentSetPath = "edu.upenn.cis350.mosstalkwords.currentSetPath";
	public final static String currentSet = "edu.upenn.cis350.mosstalkwords.currentSet";
	private ArrayList<String> categories;
	private Spinner stimspinner;
	private Spinner diffspinner;
	private String difficulty;
	private String category;
	private Scores scores;
	private TextView highscore;
	private TreeMap<String, ArrayList<String>> catToWords;
	private TreeMap<String, Integer> catToSizeOfCat = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> catToNumWordCompleted = new TreeMap<String, Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
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
		
		scores = new Scores(getApplicationContext());
	
		
		//initialize category and difficulty
		category = "livingthings";
		difficulty = "easy";
		
		AsyncTask<String, Integer, Boolean> downloadCatsWords = new LoadCategoriesWords().execute("");
	}
	
	//Method that returns the percentage of the category that's been completed (eg. 8/10, 0/10 etc.)
	//Fiona should use this method to display either this string or a star if the percentage is 10/10 or 9/9
	//Also, when the user plays a set and the catToNumWordCompleted changes you should call
	//catToNumWordCompleted.put(category, newValueOfNumCompleted) to update the mapping and then call this
	//method again to get the updated string for PercentageOfCategoryCompleted
	public String getPercentageOfCategoryCompleted(String category)
	{
		return catToNumWordCompleted.get(category) + "/" + catToSizeOfCat.get(category);
	}

	public void onStartButtonClick(View view){

		Intent i = new Intent(this, MainActivity.class);
		i.putExtra(currentSetPath, category+difficulty);
		i.putStringArrayListExtra(currentSet, getSet(category+difficulty));
		Log.i("info", category+difficulty);
		Log.i("info", catToWords.keySet().toString());
		startActivity(i);
	}

	public ArrayList<String> getSet(String key){
		return catToWords.get(key);
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
		
		protected void onPostExecute(Boolean result) {
			AsyncTask<String, Integer, Boolean> downloadFirstFiles = new LoadOneFile().execute("");
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}

	}

	private class LoadCategoriesWords extends AsyncTask<String, Integer, Boolean>{
		@Override
		protected Boolean doInBackground(String... set) {
			categories = new ArrayList<String>();
			catToWords = new TreeMap<String, ArrayList<String>>();
			boolean b = false;
			try {
					URL ur = new URL("https://s3.amazonaws.com/mosstalkdata/categories.txt");
					BufferedReader categoryReader = new BufferedReader(new InputStreamReader(ur.openStream()));
					String lineRead;
					while ((lineRead = categoryReader.readLine()) != null){
						b = true;
						categories.add(lineRead);
					}
					categoryReader.close();
					for (String cat: categories){
						try{
						URL urwords = new URL("https://s3.amazonaws.com/mosstalkdata/" + cat + "/words.txt");
						BufferedReader wordsReader = new BufferedReader(new InputStreamReader(urwords.openStream()));
						String word;
						ArrayList<String> wordslist = new ArrayList<String>();
						int count = 0;
						while ((word = wordsReader.readLine()) != null){
							wordslist.add(word);
							count++;
						}
						catToWords.put(cat, wordslist);
						catToSizeOfCat.put(cat, count);
						
						int numCompleted = scores.getNumCompleted(cat);
						catToNumWordCompleted.put(cat, numCompleted);
						
						wordsReader.close();
						}
						catch(FileNotFoundException e){
							e.printStackTrace();
						}
					}
			}catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		 return b;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			AsyncTask<String, Integer, Boolean> downloadFirstFiles = new LoadOneFile().execute("");
		}

	}

	private class LoadOneFile extends AsyncTask<String, Integer, Boolean>{
		@Override
		protected Boolean doInBackground(String... set) {
			boolean b = false;
			try {
				for (String cat: categories ){
					if(getSet(cat) != null){
					URL ur = new URL("https://s3.amazonaws.com/mosstalkdata/" + cat + 
							"/" + getSet(cat).get(0) + ".jpg");

					File file = new File(getApplicationContext().getCacheDir(),  getSet(cat).get(0) +".jpg");
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
}
