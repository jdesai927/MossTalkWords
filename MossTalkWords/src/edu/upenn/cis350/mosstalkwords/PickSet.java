package edu.upenn.cis350.mosstalkwords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class PickSet extends Activity {
	public final static String currentSetPath = "edu.upenn.cis350.mosstalkwords.currentSetPath";
	public final static String currentSet = "edu.upenn.cis350.mosstalkwords.currentSet";
	private Spinner stimspinner;
	private Spinner diffspinner;
	private String difficulty;
	private String category;
	
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
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		diffspinner.setAdapter(diffadapter);
		stimspinner.setOnItemSelectedListener(new CategorySelectedListener());
		diffspinner.setOnItemSelectedListener(new DifficultySelectedListener());
	}
	
	public void onStartButtonClick(View view){
		
		Intent i = new Intent(this, MainActivity.class);
	
		
		i.putExtra(currentSetPath, category+difficulty);
		i.putExtra(currentSet, getSetArray(category+difficulty));
		startActivity(i);
	}
	
	public String[] getSetArray(String key){
		if(true){
			return getResources().getStringArray(R.array.nonlivingthingshard);	
		}
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
		 
		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			 difficulty = parent.getItemAtPosition(pos).toString();	
			 difficulty = difficulty.replaceAll("\\s","");
			 difficulty = difficulty.toLowerCase();
		  }
		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
		  }
		 
		}
	
	
	public class CategorySelectedListener implements OnItemSelectedListener {
		 
		  public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
			 category = parent.getItemAtPosition(pos).toString();	
			 category = category.replaceAll("\\s","");
			 category = category.toLowerCase();
		  }
		  @Override
		  public void onNothingSelected(AdapterView<?> arg0) {
		  }
		 
		}
}
