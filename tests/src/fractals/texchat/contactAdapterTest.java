/**
 * 
 */
package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.test.InstrumentationTestCase;

import junit.framework.TestCase;

/**
 * @author Janine
 *
 */
public class contactAdapterTest extends InstrumentationTestCase {

	private contactAdapter contactAdapterTest;
	private ArrayList<RosterEntry> entriesTest;	
	private ArrayList<RosterEntry> entriesTest2;
	private Context contextTest;
	private Context context2;
	/**
	 * @param name
	 */
	public contactAdapterTest(String name) {
		super();
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
		contextTest = getInstrumentation().getTargetContext();
		entriesTest = new ArrayList<RosterEntry>();
		RosterEntry testRosterEntry = null;
		entriesTest.add(testRosterEntry);
		entriesTest2 = new ArrayList<RosterEntry>();
		
		contactAdapterTest = new contactAdapter(contextTest, entriesTest);
		
		entriesTest2 = contactAdapterTest.getRoster();
		context2 = contactAdapterTest.getContext();
		
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		contextTest = null;
		entriesTest = null;
		
		super.tearDown();
	}

	/*private final void testContext() {
		assertNotNull("Context is NOT null...", context2);
	}
	/**
	 * Test method for {@link fractals.texchat.contactAdapter#getCount()}.
	 */
	/*public final void testGetCount() {
		int counter = entriesTest2.size();
		
		assertNotNull("More than 0 items, not Null..."+counter, entriesTest2);
	}

	/**
	 * Test method for {@link fractals.texchat.contactAdapter#getItem(int)}.
	 */
	/*public final void testGetItem() {
		assertNotNull("Roster is not empty...", entriesTest2.get(0));
		
		//assertNotNull("Roster is not empty...", entriesTest.get(1));
		
		//assertNotNull("Roster is not empty...", entriesTest.get(2));
	}*/
}
