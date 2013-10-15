package fractals.texchat.test;

import java.util.ArrayList;

import fractals.texchat.DatabaseHandler;
import fractals.texchat.DatabaseHandler.MessageDetail;
import fractals.texchat.Packet;
import android.test.AndroidTestCase;

public class DatabaseHandlerTest extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateDB() {
		//CHECK IF THE DATABASEHANDLER OBJECT IS NOT NULL AFTER THE CREATE STATEMENTS
		DatabaseHandler mytestdb = new DatabaseHandler(getContext());
		boolean result = mytestdb.createDB();
		boolean expected = true;
		assertEquals("DATABASE FAILED TO CREATE!", expected, result);
	}

	public void testAddToMessages() {
		DatabaseHandler mytestdb = new DatabaseHandler(getContext());
		
		//TRY TO SAVE A MESSAGE PACKET
		final Packet sendPacket = new Packet("999un", "999un", true); 
		boolean result = mytestdb.addToMessages(sendPacket);
		boolean expected = true;
		
		//CLEANUP
		if(result == true) {
			mytestdb.deleteLine(sendPacket);
		}
		
		assertEquals("MESSAGE FAILED TO SAVE!", expected, result);	
	}

	public void testDeleteMessages() {
		DatabaseHandler mytestdb = new DatabaseHandler(getContext());
		
		//INSERT DUMMY DATA
		final Packet sendPacket = new Packet("999un", "999un", true); 
		boolean saveDummyData = mytestdb.addToMessages(sendPacket);
		boolean result = false;
		boolean expected = true;
		
		if(saveDummyData) {
			//DATA IS IN THE DB TRY TO DELETE IT
			result = mytestdb.deleteMessages(sendPacket.getUser());
		}
		else { }
		
		assertEquals("MESSAGES FAILED TO DELETE!", expected, result);
	}
	
	public void testSelectMessages() {
		DatabaseHandler mytestdb = new DatabaseHandler(getContext());
		ArrayList<MessageDetail> tmp = new ArrayList<MessageDetail>(); 
		boolean result = false;
		boolean expected = true;
		
		//INSERT DUMMY DATA
		final Packet sendPacket1 = new Packet("999un", "999un_Message1", true); 
		boolean saveDummyData1 = mytestdb.addToMessages(sendPacket1);
		final Packet sendPacket2 = new Packet("999un", "999un_Message2", true); 
		boolean saveDummyData2 = mytestdb.addToMessages(sendPacket2);
		
		//RETRIEVE THE DATA - IF ARRAYLIST IS NULL IT FAILED
		saveDummyData1 = true;
		saveDummyData2 = true;
		if((saveDummyData1==true) && (saveDummyData2==true)) {
			tmp = mytestdb.selectMessages("999un", 10);
		}
		
		if(tmp.isEmpty()) {
			result = false;
		}
		else {
			result = true;
		}
		
		//CLEANUP
		if(result == true) {
			mytestdb.deleteMessages("999un");
		}
		
		assertEquals("MESSAGES WAS NOT SUCCESFULLY RETRIEVED!", expected, result);	
	}

}
