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
    	private static final String tbREMEMBERME = "rememberme";
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
				createDB();
				Log.i("INFORMATION", "Constructor()");  
		    }
			
			//FOR TESTING PURPOSES
			public boolean createDB()
			{
				//FOR TESTING PURPOSES
				db = getWritableDatabase();
				//db = null;
				//ONLY FAIL IF THE DATABASE DOESN'T EXIST AFTER THAT STATEMENT
				if(db == null)
				{
					return false;
				}
				else
				{
					return true;
				}
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
					
					String createRememberMeTable = "CREATE TABLE " + tbREMEMBERME + "(" +
							"PKUserLoggedIn INTEGER PRIMARY KEY, " +
							"User TEXT, " +
							"Password TEXT, " +
							"Remembered BOOLEAN);";
					
					db.execSQL(createRememberMeTable);					
					
					Log.i("INFORMATION", "onCreate()");
			}
	
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
			{	
					//CREATE IF TABLES DON'T EXIST
					db.execSQL("DROP TABLE IF EXISTS " + tbMESSAGES);
					onCreate(db);	
					db.execSQL("DROP TABLE IF EXISTS " + tbREMEMBERME);
					onCreate(db);	
					Log.i("INFORMATION", "onUpgrade()");
			}
	
			
			public boolean clearRememberMe()
			{
				//REFRESH THE TABLE
				String deleteAll = "DELETE FROM " + tbREMEMBERME + ";";
				db.execSQL(deleteAll);
				return true;
			}
			
			//ADD LOGGED IN USER INFORMATION
			public boolean addRememberMe(String user, String pass, boolean remember)
			{
				
				boolean success = false;
				
				//PUT VALUES IN THE DB
				this.db.beginTransaction();
				try
				{
					boolean clear = clearRememberMe();
					if(clear)
					{
						System.out.println("Table cleared");
					}
					
					// |_ PKUserLoggedIn _|_ User _|_ Password _|_ Remembered _|//
					String query = "INSERT INTO " + tbREMEMBERME + " (User, Password, Remembered) VALUES ('"+ user +"', '"+ pass +"', '"+remember+"');"; 
					db.execSQL(query);
					
					//CHECK IF THE LINE IS INDEED IN THE DB AFTER THE INSERT - IF YOU CAN SELECT IT ITS THERE, OTHERWISE IT FAILED
					Cursor result;
					String check = "SELECT * FROM " + tbREMEMBERME + " WHERE User = '" + user + "' AND Password = '" + pass + "';";
					result = db.rawQuery(check, null);
					
					if(result.moveToFirst())
					{
						success = true;
					}
					else
					{
						success = false;
					}	
					result.close();
					
					db.setTransactionSuccessful();		
				}
				finally
				{
					db.endTransaction();
				}
				
				if(success == true)
				{
					Log.i("INFORMATION", "Sent and in the rememberme table!");
					return true;
				}
				else
				{
					Log.i("INFORMATION", "Sent failed to save!");
					return false;
				}
				
			}
			
			//GET LOGGED IN USER
			public ArrayList<String> getLoggedInUser()
			{
				
				ArrayList<String> loggedUser = new ArrayList<String>(); 
				
				//GET THE INFORMATION IN THE REMEMBERME TABLE
				Cursor result;
				String check = "SELECT * FROM " + tbREMEMBERME + ";";
				result = db.rawQuery(check, null);
				
				if(result.moveToFirst())
				{
					do
					{
						loggedUser.add(result.getString(1)); //	user
						loggedUser.add(result.getString(2)); //	password
						loggedUser.add(result.getString(3)); //	remembered
						
					}while(result.moveToNext());
				}
				result.close();
				return loggedUser;
			}
			
			//ADD MESSAGE PACKET TO MESSAGES TABLE
			public boolean addToMessages(Packet packet) // To | Message | MessageNum | Sent/Received
			{
					boolean success = false;
					
					//PUT PACKET VALUES IN DB
					this.db.beginTransaction();
					try
					{	
						String u = packet.getUser();
						if (u.contains("\\")) u = u.substring(0, u.indexOf('\\'));
						//System.out.println(u);
						// |_ PKMessage _|_ MessageBody _|_ MessageStamp _|_ User _|_ UsrUniqueKey _|_ Sent_1_Received_0 _| //
						String query = "INSERT INTO " + tbMESSAGES + " (MessageBody, MessageStamp, User, UsrUniqueKey, Sent_1_Received_0) VALUES ('" + packet.getMessage() +"', '" + packet.getTimeStamp() + "', '"+ u +"', '"+ packet.getUniqueKey() +"', '"+ packet.getSent_Received() +"');"; 
						db.execSQL(query);
						
						//CHECK IF THE LINE IS INDEED IN THE DB AFTER THE INSERT - IF YOU CAN SELECT IT ITS THERE, OTHERWISE IT FAILED
						Cursor result;
						String check = "SELECT * FROM " + tbMESSAGES + " WHERE MessageStamp = '" + packet.getTimeStamp() + "';";
						result = db.rawQuery(check, null);
						
						if(result.moveToFirst())
						{
							success = true;
						}
						else
						{
							success = false;
						}	
						result.close();
						db.setTransactionSuccessful();	
					}
					finally
					{
						db.endTransaction();
					}
					
					if(success == true)
					{
						Log.i("INFORMATION", "Sent and in the messages table!");
						return true;
					}
					else
					{
						Log.i("INFORMATION", "Sent failed to save!");
						return false;
					}
			}
			
			//TO DELETE A SPECIFIC MESSAGE
			public void deleteLine(Packet todelete)
			{
					String mess = todelete.getMessage();
					String user = todelete.getUser();					
					Log.i("INFORMATION", user + "|" + mess);	
					db.delete(tbMESSAGES, "MessageBody = '" + mess + "'", null);
			}
		
			//VIEW THE SENT/RECEIVED MESSAGES BETWEEN YOU AND ANOTHER USER (u) - RETURNS AN ALREADY ORDERED ARRAYLIST OF THE CONVERSATION	
			public ArrayList<MessageDetail> selectMessages(String u, int limit)
			{	
			
				ArrayList<MessageDetail> messages = new ArrayList<MessageDetail>();
			
				Cursor c;
				String query = "SELECT * FROM " + tbMESSAGES + " WHERE User like '" + u + "%' ORDER BY messageStamp DESC LIMIT '"+limit +"';" ;
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
				c.close();
				return messages;
			}
			
			//DELETE ALL MESSAGES FOR A USER
			public boolean deleteMessages(String user)
			{
					
				db.delete(tbMESSAGES, "User = '" + user + "'", null);
				Log.i("INFORMATION", "Messages deleted!");
				
				//TO BE SURE
				ArrayList<MessageDetail> messages = new ArrayList<MessageDetail>();
				messages = selectMessages(user, 10);
				
				if(messages.isEmpty())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
}
