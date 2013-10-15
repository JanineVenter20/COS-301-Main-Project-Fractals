package fractals.texchat.test;

import fractals.texchat.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

	//PRIVATE MEMBERS
	private MainActivity myActivity;
	
	//CONSTRUCTOR
	public MainActivityTest() {
		super("fractals.texchat", MainActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		myActivity = getActivity();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	//TEST METHODS SHOULD BE RUN ONE AT A TIME OTHERWISE THE TEST CRASHES?!

/*	
	public void testGetNames() {
		String testMessage = "";
		
		//TEST IF YOU CAN EVEN ACCESS THE ROSTER
		boolean canGetRoster = myActivity.conn.getRoster() != null;
		if(!canGetRoster){ testMessage += " CAN/'T GET THE ROSTER "; }
		
		//TEST IF THE getNames() METHOD RETURNS SUCCESSFULLY
		boolean canGetNames;
		String resultGetNames = myActivity.getNames();
		if(resultGetNames.equals("failed")){ canGetNames = false; testMessage += " THE getNames() METHOD FAILED "; }
		else { canGetNames = true; }
			
		boolean result;
		boolean expected = true;
		if((canGetRoster==true) && (canGetNames==true))
		{
			testMessage += " CONTACT LISTING SUCEEDED ";
			result = true;
		}
		else
		{
			testMessage += " CONTACT LISTING FAILED ";
			result = false;
		}
		assertEquals(testMessage, expected, result);
	}
*/

	public void testConnectToServer() {
		String testMessage = "FAILED TO CONNECT TO THE SERVER";
		boolean result = myActivity.ConnectToServer();
		boolean expected = true;
		assertEquals(testMessage, expected, result);
	}

}
