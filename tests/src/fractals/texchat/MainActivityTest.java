/**
 * 
 */
package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import junit.framework.TestCase;

/**
 * @author Janine
 *
 */
public class MainActivityTest extends TestCase {

	private MainActivity mainActivityTest;
	/**
	 * @param name
	 */
	public MainActivityTest(String name) {
		super(name);
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	protected static void tearDownAfterClass() throws Exception {
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		mainActivityTest = new  MainActivity();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		
		mainActivityTest.username = "";
		mainActivityTest.password = "";
		
		mainActivityTest = null;
	}

	/**
	 * Test method for {@link fractals.texchat.MainActivity#login()}.
	 */
	public final void testLogin() {
		mainActivityTest.username = "test";
		mainActivityTest.password = "test";
		
		mainActivityTest.login();
		
		boolean actual = mainActivityTest.done;
		boolean expected = true;
		
		assertEquals("Log in success...", expected, actual);
	}
	
	/**
	 * Test method for {@link fractals.texchat.MainActivity#getNames()}.
	 */
	public final void testGetNames() {
		//testLogin();
		
		ListView testContactLV = mainActivityTest.contactLV;
		
		assertNull("The contact list roster is empty...", testContactLV);
		//assertNotNull("The contact list roster is not empty...", testContactLV);
	}

}
