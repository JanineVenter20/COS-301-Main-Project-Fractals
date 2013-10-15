package fractals.texchat.test;


import org.jivesoftware.smack.XMPPException;

import fractals.texchat.MainActivity;
import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

public class LoginTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	//PRIVATE MEMBERS
	private MainActivity myActivity;
	
	//CONSTRUCTOR
	public LoginTest() {
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

	public void testLogin() {
		
		myActivity.runOnUiThread(new Runnable() {
			public void run() {
				boolean expected = true;
				boolean result = false;
				String testMessage = "LOGIN FAILED";
				
				//ENSURE A CONNECTION WITH THE SERVER, THEN TRY TO LOGIN
				try {
					MainActivity.conn.connect();
					MainActivity.conn.login("piekie", "piekie");
					result = true;
					
					System.out.println("*** SHOULD BE A SUCCESS ***");
					
				} catch (XMPPException e) {
					
					e.printStackTrace();
					result = false;
					
					System.out.println("*** SHOULD BE A FAIL ***");
				}
				finally {
					myActivity.conn.disconnect();
				}
				assertEquals(testMessage, expected, result);
			}
		}); 
		}
}
