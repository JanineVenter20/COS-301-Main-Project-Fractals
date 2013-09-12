/**
 * 
 */
package fractals.texchat;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.ServiceTestCase;

/**
 * @author Janine
 *
 */
public class messageAdapterTest extends InstrumentationTestCase {

	private messageAdapter messageAdapterTest;
	private Context contextTest;
	private Context context2;
	private ArrayList<Message> messagesTest; 


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
		messagesTest = new ArrayList<Message>();
		
		Message mess = new Message("Test 1");
		messagesTest.add(mess);
		
		Message mess2 = new Message("Test 2");
		messagesTest.add(mess2);
		
		System.out.println(messagesTest);
		
		messageAdapterTest = new messageAdapter(contextTest, messagesTest);
		
		context2 = messageAdapterTest.getContext();
		
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		messageAdapterTest = null;
		messagesTest = null;
		contextTest = null;
		
		super.tearDown();
	}

	/**
	 * Test method for {@link fractals.texchat.messageAdapter#getCount()}.
	 */
	public final void testContext() {
		assertNotNull("Context NOT null...", context2);
		//assertNull("Context null...", context2);
	}
	
	public final void testGetCount() {
		int testMessageCounter = messagesTest.size();
		
		assertNotNull("Message count is Not NULL..."+testMessageCounter, messagesTest);
		//assertNull("Message count is NULL...", messagesTest);
		
		
	}

	/**
	 * Test method for {@link fractals.texchat.messageAdapter#getItem(int)}.
	 */
	public final void testGetItem() {
		assertNotNull("Testing first messages in Array List Not NULL...", messagesTest.get(0));
		//assertNull("Testing first messages in Array List NULL...", messagesTest.get(0));
	}
}
