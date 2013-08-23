package fractals.texchat;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	//DATABASE INFORMATION	
		private static final int dbVERSION = 1;
		private static final String dbNAME = "myDatabase";
    
    //TABLE NAMES
    	private static final String tbMESSAGES = "messages";
    	Context context;
    	private SQLiteDatabase db;
    
    //INNER CLASSES
    	class MessageDetail
    	{
    		//PRIVATE MEMBERS
	    		private String messageBody;
	    		private Boolean sent_received;
    		
	    	//METHODS	
	    		public MessageDetail(String m, Boolean sr)
	    		{
	    			this.messageBody = m;
	    			this.sent_received = sr;
	    		}
	    		public String getMessageBody()
	    		{
	    			return this.messageBody;
	    		}
	    		public Boolean getSentReceived()
	    		{
	    			return this.sent_received;
	    		}
    	}
    	
    //METHODS
    	
    	//CONSTRUCTOR
			public DatabaseHandler(Context context) 
			{
		        super(context, dbNAME, null, dbVERSION);
				this.context = context;
				db = getWritableDatabase();
		        Log.i("INFORMATION", "Constructor()");   
		    }
	
		@Override 
		//CALLED WHEN DB IS FIRST CREATED (getWritableDatabase())
			public void onCreate(SQLiteDatabase db) 
			{ 	
				//CREATE THE TALBES 
					// |_ PKMessage _|_ MessageBody _|_ MessageStamp _|_ User _|_ UsrUniqueKey _|_ Sent_1_Received_0 _| //
					String createMessagesTable = "CREATE TABLE " + tbMESSAGES + "(" +
							"PKMessage INTEGER PRIMARY KEY, " +
							"MessageBody TEXT, " +
							"MessageStamp DATETIME, " +
							"User TEXT, " +
							"UsrUniqueKey TEXT," +
							"Sent_1_Received_0 BOOLEAN);";
					
					db.execSQL(createMessagesTable);						
					Log.i("INFORMATION", "onCreate()");
			}
	
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
			{	
				//CREATE IF TABLES DON'T EXIST
					db.execSQL("DROP TABLE IF EXISTS " + tbMESSAGES);
					onCreate(db);	
				Log.i("INFORMATION", "onUpgrade()");
			}
	
		//ADD MESSAGE PACKET TO MESSAGES TABLE
			public void addToMessages(Packet packet) // To | Message | MessageNum | Sent/Received
			{
				//PUT PACKET VALUES IN DB
						this.db.beginTransaction();
						try
						{	
							// |_ PKMessage _|_ MessageBody _|_ MessageStamp _|_ User _|_ UsrUniqueKey _|_ Sent_1_Received_0 _| //
							String query = "INSERT INTO " + tbMESSAGES + " (MessageBody, MessageStamp, User, UsrUniqueKey, Sent_1_Received_0) VALUES ('" + packet.getMessage() +"', '" + packet.getTimeStamp() + "', '"+ packet.getUser() +"', '"+ packet.getUniqueKey() +"', '"+ packet.getSent_Received() +"');"; 
							db.execSQL(query);
							Log.i("INFORMATION", "Sent and in database!");
							db.setTransactionSuccessful();	
						}
						finally
						{
							db.endTransaction();
						}
			}
		
		//VIEW THE SENT/RECEIVED MESSAGES BETWEEN YOU AND ANOTHER USER (u) - RETURNS AN ALREADY ORDERED ARRAYLIST OF THE CONVERSATION	
			public ArrayList<MessageDetail> selectMessages(String u)
			{	
			
				ArrayList<MessageDetail> messages = new ArrayList<MessageDetail>();
			
				Cursor c;
				String query = "SELECT * FROM " + tbMESSAGES + " WHERE User = '" + u + "';" ;
				
				this.db.beginTransaction();
				try
				{
					c = db.rawQuery(query, null);
					db.setTransactionSuccessful();
				}
				finally
				{
					db.endTransaction();
				}
				
				if(c.moveToFirst())
				{
					do
					{
						messages.add(new MessageDetail(c.getString(1), Boolean.parseBoolean(c.getString(5))));
				
					}while(c.moveToNext());
				
					//JUST FOR DISPLAY
						String to = "Messages sent to " + u;
						Log.i("INFORMATION", to);
						
						if(messages != null)
						{
							for(MessageDetail m : messages)
							{
								Log.i("INFORMATION", m.messageBody +"/"+ m.sent_received.toString()+"\n");
							}
						}
				}
				return messages;
			}
}
