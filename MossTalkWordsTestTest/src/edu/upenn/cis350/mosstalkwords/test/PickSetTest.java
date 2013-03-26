package edu.upenn.cis350.mosstalkwords.test;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;
import edu.upenn.cis350.mosstalkwords.PickSet;

public class PickSetTest extends ActivityInstrumentationTestCase2<PickSet> {

	private PickSet act;
	
	public PickSetTest() {
		super("edu.upenn.cis350.mosstalkwords", PickSet.class);
	}
	
	public void setUp() {
		
		act = (PickSet) this.getActivity();
	}
	
	/**
	 * test if categories and words are downloaded successfully
	 * @throws InterruptedException
	 */
	public void testCatDownload() throws InterruptedException {
		
		int i = 0;
		
		while(act.getDownloadCatsStatus() != AsyncTask.Status.FINISHED && i < 200) {
			Thread.sleep(500);
			i++;
		}
		
		if(i >= 200)
			fail();
	}
	
	
	/**
	 * Test whether we can get a set, and that the set is correct
	 * @throws InterruptedException
	 */
	public void testGetSet() throws InterruptedException {
		
		//wait for download
		while(act.getDownloadCatsStatus() != AsyncTask.Status.FINISHED ) {
			Thread.sleep(500);
		}
		
		ArrayList<String> res = act.getSet("nonlivingthingshard");
		
		assertNotNull(res);
		
		assertTrue(res.contains("parachute"));
		assertTrue(res.contains("freezer"));
		assertTrue(res.contains("marker"));
		assertTrue(res.contains("icicle"));
		assertTrue(res.contains("boomerang"));
		assertTrue(res.contains("calculator"));
		assertTrue(res.contains("stadium"));
		assertTrue(res.contains("motorcycle"));
		assertTrue(res.contains("toothbrush"));
		assertTrue(res.contains("spatula"));
		
	}
	
	
	
}
