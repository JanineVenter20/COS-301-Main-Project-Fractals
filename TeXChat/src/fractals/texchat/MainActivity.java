package fractals.texchat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.OfflineMessageManager;

import fractals.texchat.R.menu;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static String username = "";
	public static String password = "";
	static String hostName = "192.168.137.1";
	static String service = "fractals.texchat";
	static int port = 5222;
	
	public static String me = "You",
			them;
	
	public static ConnectionConfiguration ccf;
	public static XMPPConnection conn;
	public static Roster roster;
	public static Chat activeChat;
	
	ListView contactLV;
	Context context = this;
	Activity c = this;
	public static DatabaseHandler dbHandler;
	public static MessageListener ml;
		 
	String contact;
	public static ArrayList<Message> messages= new ArrayList<Message>();
	public static messageAdapter mad;
	ArrayList<RosterEntry> entryList = new ArrayList<RosterEntry>();
	public static contactAdapter cad;
	
	public static ChatManager cm;
	
	public static boolean emtpyList = false;
	TextView nc;
	
	String first =  "T";
	String remain = "eXChat";
	
	
	PopupMenu pop;
	protected ProgressDialog progressDialog;
	  
    private NotificationCompat.Builder builder;
    private PendingIntent notifyPIntent;
    private NotificationManager manager;
    private Intent notificationIntent;
    
    private void addNotification(String name, String messageInNot) {
	    builder = new NotificationCompat.Builder(this);  
	    builder.setSmallIcon(R.drawable.nottification_icon);  
	    builder.setAutoCancel(true);
	    builder.setContentTitle(name);  
	    builder.setContentText(messageInNot);
	
	    notificationIntent = this.getIntent();  
	    notifyPIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);     
	    builder.setContentIntent(notifyPIntent); 
	    
	    // Add as notification  
	    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
	    manager.notify(0, builder.build());
    }
	
	class LoginTask extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute(){ 
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Logging In...");
			progressDialog.show();
		   
		}

		@Override
		protected void onPostExecute(String result){
		   super.onPostExecute(result);
		   Handler handler = new Handler();
		   handler.postDelayed(new Runnable() {
		       public void run() {
		    	   try {
		    		   progressDialog.dismiss();
		    	   } catch (IllegalArgumentException e) { }
		       }}, 50); 
		   c.runOnUiThread(new Runnable() {
				public void run() {
					getNames();

				}
			});
		}
		
		
		@Override
		protected String doInBackground(String... params) {
			login();
			
			c.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (messages != null)
					mad.notifyDataSetChanged();
					showHide();
				}
			});
			return null;
		}
	}
	
	class logoutTask extends AsyncTask<String, String, String> {

		
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		   progressDialog = new ProgressDialog(context);
		   progressDialog.setMessage("Logging Out...");
		   progressDialog.show();
		   progressDialog.setCancelable(false);
		}

		@Override
		protected void onPostExecute(String result){
		   super.onPostExecute(result);
		   Handler handler = new Handler();
		   handler.postDelayed(new Runnable() {
		       public void run() {
		    	   try {
		    		   progressDialog.dismiss();
		    	   } catch (IllegalArgumentException e) { }
		       }}, 50);  // 50 milliseconds
		   
		   c.runOnUiThread(new Runnable() {
				public void run() {
					entryList.clear();
					roster = conn.getRoster();
					entryList.addAll(roster.getEntries());
					cad.notifyDataSetChanged();
					emtpyList = false;
				}
			});
		}

		@Override
		protected String doInBackground(String... params) {
			if (conn.isConnected())
				if (conn.isAuthenticated()) {
					conn.disconnect();
					first = "Not Logged In";
					runOnUiThread(new Runnable() {
			            public void run() {
							TextView myTitleText = (TextView) findViewById(R.id.myTitle);
					    	if (myTitleText != null) {
					        	myTitleText.setText("  " + first);
					        	myTitleText.setTextColor(Color.WHITE);
					    	}
			            }
			        });
				}
			return null;
		}
	}
	
    static class ConnTask extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... params) {
			if (!conn.isConnected())
				try {
					conn.connect();
				} catch (XMPPException e) {
					System.out.println(e.getMessage());
				}
			return null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		/*****************************************/
		setContentView(R.layout.activity_main);
		/*****************************************/
		if(customTitleSupported){
			System.out.println("CUSTOM SUPPORTED!!!");
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		}
		if (dbHandler == null)
			dbHandler = new DatabaseHandler(this);
		
		try {
		first = username.substring(0,1).toUpperCase() + username.substring(1, username.length());
		
		runOnUiThread(new Runnable() {
            public void run() {
				TextView myTitleText = (TextView) findViewById(R.id.myTitle);
		    	if (myTitleText != null) {
		    		
		        		myTitleText.setText("  " + first);
		        		myTitleText.setTextColor(Color.WHITE);
		    	}
            }
        });
		} catch (IndexOutOfBoundsException i) {}
		
		nc = (TextView)findViewById(R.id.no_contacts_message);
		nc.setVisibility(View.GONE);

		getPrefs();
		
		if (conn == null)
			createConnection(hostName, port, service);
		new ConnTask().execute("some string");
		
		contactLV = (ListView)findViewById(R.id.ContactListView);
	   	cad = new contactAdapter(this, entryList);
	   	contactLV.setAdapter(cad) ;
			
		if (roster == null) {
			getRoster();
		}
		
		if (!filledIn()) {
			startActivityForResult(new Intent(this, LoginPage.class), 1);
		} else {
			getNames();
		}
		
		messages = null;
	
		ml = new MessageListener() {
			
			boolean known = false;
			
			@Override
			public void processMessage(Chat chat, Message mess) {
				if (mess.getBody() == null) return;	
				String s = mess.getBody().replace("'", "''");
				for (RosterEntry entry : roster.getEntries())
					if(chat.getParticipant().contains(entry.getUser())) {
						known = true;
						System.out.println(known);
					}
					if (messages != null)
						if (chat.getParticipant().equals(activeChat.getParticipant()))	
							messages.add(mess);
					if (known) {
						addNotification(chat.getParticipant(), s);
					dbHandler.addToMessages(new Packet(MainActivity.username, chat.getParticipant(),s, true , false));
				} else {
					try {
						roster.createEntry(chat.getParticipant(), chat.getParticipant(), null);
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				}
				
				c.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (messages != null)
						mad.notifyDataSetChanged();
					}
				});
			}
		};
		if (conn.isAuthenticated()) {
			ArrayList<Message> unsent = dbHandler.getUnsentMessages(MainActivity.username);
	    	if (!unsent.isEmpty()) {
	    		for (Message m : unsent) {
	    			conn.sendPacket(m);
	    		}
	    	} 
	    	c.runOnUiThread(new Runnable() {
				public void run() {
					dbHandler.deleteUnsent();
				}
			});
	    	
    	}
	}
	
	private void getPrefs() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		hostName = pref.getString("hostName", "192.168.137.1");
		service = pref.getString("serverName", "fractals.texchat");
		port = Integer.parseInt(pref.getString("portNumber", "5222"));
	}
	
	public void getRoster(){ 
		roster = conn.getRoster();
		roster.addRosterListener(new RosterListener() {
			@Override
			public void presenceChanged(Presence pres) { getNames(); System.out.println("!"); }
			@Override
			public void entriesUpdated(Collection<String> arg0) { getNames(); System.out.println("!!"); }
			@Override
			public void entriesDeleted(Collection<String> arg0) { getNames(); System.out.println("!!!"); }
			@Override
			public void entriesAdded(Collection<String> arg0) { getNames(); System.out.println("!!!!"); }
		});
	}
	
	private void createConnection(String host,int p, String serv) {
		ccf = new ConnectionConfiguration(host, p, serv);
		conn = new XMPPConnection(ccf);
		cm = conn.getChatManager();
		cm.addChatListener(new ChatManagerListener() {
			public void chatCreated(Chat chat, boolean locally) {
				if (!locally) {
					activeChat = chat;
					activeChat.addMessageListener(ml);
				} else {
					activeChat = chat;
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.contact_context, menu);
		//super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.action_login: 
			startActivityForResult(new Intent(this, LoginPage.class), 1);
			break;
		case R.id.action_logout:
			new logoutTask().execute("logout");
			dbHandler.addRememberMe(username, password, false);
			break;
		case R.id.action_add_contact :
			startActivityForResult(new Intent(this, AddContactActivity.class), 2);
			break;
	    default:
			break;
		}
		return true;
	}

	public boolean filledIn() {
    	if (username.equals("") || password.equals(""))
    			return false;
    	else return true;
    }
	
	public void getNames() {
		entryList.clear();
		roster = conn.getRoster();
		entryList.addAll(roster.getEntries());
		
		checkContactList();
		c.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				cad.notifyDataSetChanged();
			}
		});	
	}
	
	public boolean login()  {
        ccf.setCompressionEnabled(false);
        ccf.setSASLAuthenticationEnabled(false);
        ccf.setSendPresence(false);
        try {
        	if(!conn.isConnected())
        		conn.connect();

        	OfflineMessageManager ofm = new OfflineMessageManager(conn);
        	
        	if (!conn.isAuthenticated()) {
        		conn.login(username, password);
        		
        		first = username.substring(0,1).toUpperCase();
	    		remain = username.substring(1, username.length());
				
		        runOnUiThread(new Runnable() {
		            public void run() {
						TextView myTitleText = (TextView) findViewById(R.id.myTitle);
				    	if (myTitleText != null) {
				    		
				        		myTitleText.setText("  " + first + remain);
				        		myTitleText.setTextColor(Color.WHITE);
				    	}
		            }
		        });    
        	}
        	
        	if (conn.isAuthenticated()) {
    		Iterator<Message> it = ofm.getMessages();
    		while (it.hasNext()) {
    			Message ms =it.next();
    			addNotification(ms.getFrom(), ms.getBody());
    			dbHandler.addToMessages(new Packet(MainActivity.username, ms.getFrom(),ms.getBody(), true , false));
    		}
        	conn.sendPacket(new Presence(Type.available));
        	ofm.deleteMessages();
        	}
        	
        	
        	
		} catch (XMPPException e) {
			c.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(context, "Could not log in...", Toast.LENGTH_SHORT).show();
				}
			});
			startActivityForResult(new Intent(this, LoginPage.class), 1);
			return false;
		}
        
        
        return true;
    } 
	
	public void showHide() {
		if(emtpyList) {
			nc.setVisibility(View.VISIBLE);
		} else {
			nc.setVisibility(View.GONE);
		}
	}
	
	public void checkContactList() {
		if(roster.getEntries().isEmpty()) {	emtpyList = true; }
		else { emtpyList  = false; }
	}

	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		getNames();
		checkContactList();
		c.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showHide();
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;
		switch (requestCode) {
		case 1: 
			getPrefs();
			createConnection(hostName, port, service);
			new ConnTask().execute("..");
			new LoginTask().execute("..");
			break;
		case 2:
			String usern = data.getStringExtra("username");
			String alias = data.getStringExtra("alias");
			try {
				roster.createEntry(usern, alias, null);
			} catch (XMPPException e) {
				Toast.makeText(context, "Could add not the contact", Toast.LENGTH_SHORT).show();
			}
			getNames();
			break;
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		try  {
			if (conn.isAuthenticated()) {
				menu.findItem(R.id.action_login).setVisible(false);
				menu.findItem(R.id.action_logout).setVisible(true);
				menu.findItem(R.id.action_add_contact).setVisible(true);
			} else {
				menu.findItem(R.id.action_login).setVisible(true);
				menu.findItem(R.id.action_logout).setVisible(false);
				menu.findItem(R.id.action_add_contact).setVisible(false);
			}
		} catch (IndexOutOfBoundsException ind) {
			return false;
		}
		return true;
	}
	
}
