package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
		private static final SecureCrypto sc = new SecureCrypto();
    
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

			//CALLED WHEN DB IS FIRST CREATED (getWritableDatabase())
			@Override 
			public void onCreate(SQLiteDatabase db) 
			{ 	
					//CREATE THE TALBES 
					// |_ PKMessage _|_ MessageBody _|_ MessageStamp _|_ User _|_ UsrUniqueKey _|_ Sent_1_Received_0 _| //
					String createMessagesTable = "CREATE TABLE " + tbMESSAGES + "(" +
							"PKMessage INTEGER PRIMARY KEY, " +
							"MessageBody TEXT, " +
							"MessageStamp DATETIME, " +
							"User TEXT, " +
							"Contact TEXT," +
							"Sent BOOLEAN," +
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
	
			//ON UPGRADE
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
	
			//CLEAR REMEMBER ME
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
				System.out.println("[ENCRYPTED] === " + pass);
					
				//SECURITY FEATURE - ENCRYPT SENSITIVE USER DATA
				String enc_pass = "none";
				boolean encrypted = false;
				try
				{
					enc_pass = SecureCrypto.bytesToHex(sc.encrypt(pass.trim()));
					encrypted = true;
				}
				catch(Exception e)
				{
					System.out.println("ENCRYPTION FAILED");
				}
				System.out.println("[ENCRYPTED] === " + enc_pass);
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
					if(encrypted)
					{
						// |_ PKUserLoggedIn _|_ User _|_ Password _|_ Remembered _|//
						String query = "INSERT INTO " + tbREMEMBERME + " (User, Password, Remembered) VALUES ('"+ user +"', '"+ enc_pass +"', '"+remember+"');"; 
						db.execSQL(query);
						
						//CHECK IF THE LINE IS INDEED IN THE DB AFTER THE INSERT - IF YOU CAN SELECT IT ITS THERE, OTHERWISE IT FAILED
						Cursor result;
						String check = "SELECT * FROM " + tbREMEMBERME + " WHERE User = '" + user + "' AND Password = '" + enc_pass + "';";
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
			
			public boolean deleteUnsent() {
				String sendAll = "UPDATE " + tbMESSAGES + " SET Sent = 'true'";
				db.execSQL(sendAll);
				return true;
			}
			
			
			public ArrayList<Message> getUnsentMessages(String u) {
				ArrayList<Message> messages = new ArrayList<Message>();
				
				Cursor c;
				String query = "SELECT * FROM " + tbMESSAGES + " WHERE User like '" + u +"%' AND Sent = 'false' ORDER BY messageStamp DESC;" ;
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
						Message ms = new Message();
						ms.setBody(c.getString(1));
						ms.setFrom(c.getString(3));
						ms.setTo(c.getString(4));
						messages.add(ms);
				
					} while(c.moveToNext());
				}
				c.close();
				return messages;
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
						//DECRYPT SENSITIVE USER DATA FOR USE
						String enc_pass = result.getString(2).trim();
						String dec_pass = "none";
						boolean decrypted = false;
						
						try
						{
							dec_pass = new String(sc.decrypt(enc_pass));
							decrypted = true;
						}
						catch(Exception e)
						{
							System.out.println("DECRYPTION FAILED");
						}
						System.out.println("[DECRYPTED] === " + dec_pass);
						if(decrypted)
						{
							loggedUser.add(result.getString(1)); 	//	user
							loggedUser.add(dec_pass); 				//	password 
							loggedUser.add(result.getString(3)); 	//	remembered
						}
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
						String c = packet.getContact(); 
						if (u.contains("\\")) u = u.substring(0, u.indexOf('\\'));
						if (c.contains("\\")) u = u.substring(0, c.indexOf('\\'));
						//System.out.println(u);
						// |_ PKMessage _|_ MessageBody _|_ MessageStamp _|_ User _|_ UsrUniqueKey _|_ Sent_1_Received_0 _| //
						String query = "INSERT INTO " + tbMESSAGES + " (MessageBody, MessageStamp, User, Contact, Sent, Sent_1_Received_0) VALUES ('" + packet.getMessage() +"', '" + packet.getTimeStamp() + "', '"+ u +"', '"+ c + "', '" + packet.getSent() + "', '"+ packet.getSent_Received()+"');"; 
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
			public ArrayList<MessageDetail> selectMessages(String u, String co, int limit)
			{	
			
				ArrayList<MessageDetail> messages = new ArrayList<MessageDetail>();
			
				Cursor c;
				String query = "SELECT * FROM " + tbMESSAGES + " WHERE User like '" + u +"%' AND Contact Like '" + co + "%' ORDER BY messageStamp DESC LIMIT '"+limit +"';" ;
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
						messages.add(new MessageDetail(c.getString(1), Boolean.parseBoolean(c.getString(6))));
				
					}while(c.moveToNext());
				
						//JUST FOR DISPLAY
						String to = "Messages sent to " + u;
						Log.i("INFORMATION", to);
						
						if(messages != null)
						{
							for(MessageDetail m : messages)
							{
								Log.i("INFORMATION", m.messageBody +"/"+ m.sent_received+"\n");
							}
						}
				}
				c.close();
				return messages;
			}
			
			//DELETE ALL MESSAGES FOR A USER
			public boolean deleteMessages(String user, String cont)
			{
					
				db.delete(tbMESSAGES, "User like '" + user + "%' AND Contact like '" + cont + "%'", null);
				Log.i("INFORMATION", "Messages deleted!");
				
				//TO BE SURE
				ArrayList<MessageDetail> messages = new ArrayList<MessageDetail>();
				messages = selectMessages(user, cont, 10);
				
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
