package edu.upenn.cis350.mosstalkwords.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import edu.upenn.cis350.mosstalkwords.PickSet;
import edu.upenn.cis350.mosstalkwords.R;
import edu.upenn.cis350.mosstalkwords.Scores;

public class PickSetTest extends ActivityInstrumentationTestCase2<PickSet> {

	private PickSet act;
	private Button finishButton;
	
	public PickSetTest() {
		super("edu.upenn.cis350.mosstalkwords", PickSet.class);
	}
	
	public void setUp() {
		
		act = (PickSet) this.getActivity();
	}
	
}
