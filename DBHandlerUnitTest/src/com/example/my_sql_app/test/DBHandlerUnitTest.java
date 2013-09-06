package com.example.my_sql_app.test;

import com.example.my_sql_app.DatabaseHandler;
import com.example.my_sql_app.Packet;

import android.test.AndroidTestCase;

public class DBHandlerUnitTest extends AndroidTestCase {
	
	//CONSTRUCTOR
	public DBHandlerUnitTest()
	{
	}
	
	//CHECK IF THE DB EXISTS - true WHEN THE DB EXIST false WHEN IT DOESN'T
	public void testcreateDB()
	{
		DatabaseHandler mytestdb = new DatabaseHandler(getContext());
		boolean result = mytestdb.createDB();
		boolean expected = true;
		assertEquals("DATABASE FAILED TO CREATE!", expected, result);
	}
	
	//CHECK IF THE DATA SAVES TO THE DATABASE SUCCESSFULY - true WHEN SAVED false WHEN FAILED
	public void testaddToMessages()
	{
		DatabaseHandler mytestdb = new DatabaseHandler(getContext());
		
		//TRY TO SAVE A PACKET
		final Packet sendPacket = new Packet("999un", "999un", true); 
		boolean result = mytestdb.addToMessages(sendPacket);
		boolean expected = true;
		
		//CLEANUP
		if(result == true)
		{
			mytestdb.deleteLine(sendPacket);
		}
		
		assertEquals("MESSAGE FAILED TO SAVE!", expected, result);	
	}
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
}
