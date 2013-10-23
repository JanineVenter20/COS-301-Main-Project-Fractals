package fractals.texchat;

import java.util.ArrayList;
import java.util.Collection;

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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
	boolean done = false;
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
	
	PopupMenu pop;
	
	//Janine ***********************************************//
	protected ProgressDialog progressDialog;				//
	//END **************************************************//
	
	class LoginTask extends AsyncTask<String, String, String> {
	
//Janine ***********************************************************************//
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
		   //progressDialog.dismiss();
		   Handler handler = new Handler();
		   handler.postDelayed(new Runnable() {
		       public void run() {
		    	   progressDialog.dismiss();
		       }}, 3000);  // 3000 milliseconds
		}
//END ************************************************************************//
		
		@Override
		protected String doInBackground(String... params) {
			login();
			c.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (messages != null)
					mad.notifyDataSetChanged();
					
				}
			});
			return null;
		}
	}
	
	class logoutTask extends AsyncTask<String, String, String> {
//Janine *************************************************************************//
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		   progressDialog = new ProgressDialog(context);
		   progressDialog.setMessage("Logging Out...");
		   progressDialog.show();
		   
		}

		@Override
		protected void onPostExecute(String result){
		   super.onPostExecute(result);
		   //progressDialog.dismiss();
		   Handler handler = new Handler();
		   handler.postDelayed(new Runnable() {
		       public void run() {
		    	   progressDialog.dismiss();
		       }}, 3000);  // 3000 milliseconds
		}
//END ************************************************************************//
		
		@Override
		protected String doInBackground(String... params) {
			if (conn.isConnected())
				if (conn.isAuthenticated()) {
					conn.disconnect();
					getNames();
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
		setContentView(R.layout.activity_main);
		
		if (dbHandler == null)
			dbHandler = new DatabaseHandler(this);
		
		
		nc = (TextView)findViewById(R.id.no_contacts_message);
		nc.setVisibility(View.GONE);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		hostName = pref.getString("hostName", "192.168.137.1");
		service = pref.getString("serverName", "fractals.texchat");
		port = Integer.parseInt(pref.getString("portNumber", "5222"));
		
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
					dbHandler.addToMessages(new Packet(chat.getParticipant(),s, false));
					
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
	}
	
	public void getRoster(){ 
		roster = conn.getRoster();
		roster.addRosterListener(new RosterListener() {
			@Override
			public void presenceChanged(Presence pres) { getNames(); }
			@Override
			public void entriesUpdated(Collection<String> arg0) { getNames(); }
			@Override
			public void entriesDeleted(Collection<String> arg0) { getNames(); }
			@Override
			public void entriesAdded(Collection<String> arg0) { getNames(); }
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.add_contact :
			startActivityForResult(new Intent(this, AddContactActivity.class), 2);
			break;
		case R.id.action_login: 
			startActivityForResult(new Intent(this, LoginPage.class), 1);
			break;
		case R.id.action_logout:
			new logoutTask().execute("logout");
			dbHandler.addRememberMe(username, password, false);
			break;
		case R.id.action_delete_all:
			item.setEnabled(false);
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
	
	public void login()  {
        ccf.setCompressionEnabled(false);
        ccf.setSASLAuthenticationEnabled(false);
        //ProgressDialog progressBar = new ProgressDialog(this);
        
        try {
        	if(!conn.isConnected()){
        		conn.connect();
        		//ProgressDialog.show(MainActivity.this, "Please wait", "Logging in, please wait..", true);
        	}
        	if (!conn.isAuthenticated()) {
        		conn.login(username, password );
        		//ProgressDialog.show(MainActivity.this, "Please wait", "Logging in, please wait..", true);
        	}
		} catch (XMPPException e) {
			System.out.println(e.getMessage());
		}
        //progressBar.dismiss();
        done = true;
        
        checkContactList();
        runOnUiThread(new Runnable() {
            public void run() {
            	showHide();
            }
        });

    } 
	
	public void showHide() {
		if(emtpyList) {
			//no contacts to display
			//System.out.println("NO CONTACTS TO DISPLAY");
			nc.setVisibility(View.VISIBLE);
		} else {
			//display contacts
			//System.out.println("DISPLAY CONTACTS");
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
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;
		switch (requestCode) {
		case 1: 
			new LoginTask().execute("useless text");
			getNames();
		break;
		case 2:
			String usern = data.getStringExtra("username");
			String alias = data.getStringExtra("alias");
			try {
				roster.createEntry(usern, alias, null);
//				Presence p = new Presence(Type.subscribe);
//				p.setTo(usern);
//				conn.sendPacket(p);
			} catch (XMPPException e) {
				Toast.makeText(context, "Could not the contact", Toast.LENGTH_SHORT).show();
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
			} else {
				menu.findItem(R.id.action_login).setVisible(true);
				menu.findItem(R.id.action_logout).setVisible(false);
			}
		} catch (IndexOutOfBoundsException ind) {
			return false;
		}
		return true;
	}
}
