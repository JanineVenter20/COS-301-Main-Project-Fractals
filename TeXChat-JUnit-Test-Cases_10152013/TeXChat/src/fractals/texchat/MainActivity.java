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
import org.jivesoftware.smack.packet.Presence.Type;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static String username = "";
	public static String password = "";
	static String hostName = "169.254.68.188";
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
	
	private static boolean emptyList = false;
	TextView nc;
		
	class LoginTask extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... params) {
			login();
			return null;
		}
	}
	
    static class ConnTask extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... params) {
			/**********************************************************/
			if (!conn.isConnected()) { ConnectToServer(); }
			/**********************************************************/
				//try {
					//conn.connect();
				//} catch (XMPPException e) {
					//System.out.println(e.getMessage());
				//}
			else {
				//Toast.makeText(context, "Could not connect", Toast.LENGTH_LONG).show();
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
		hostName = pref.getString("hostName", "undefined");
		service = pref.getString("serverName", "undefined");
		port = Integer.parseInt(pref.getString("portNumber", "0"));
		
		if (ccf == null) {
			ccf = new ConnectionConfiguration(hostName, port, service);
			conn = new XMPPConnection(ccf);
			cm = conn.getChatManager();
			cm.addChatListener(new ChatManagerListener() {
				public void chatCreated(Chat chat, boolean locally) {
					if (!locally) {
						activeChat = cm.createChat(chat.getParticipant(), ml);
						mad = new messageAdapter(c, messages);
					} else {
						activeChat = chat;
					}
				}
			});
		}
		
		new ConnTask().execute("some string");
		
		contactLV = (ListView)findViewById(R.id.ContactListView);
	   	cad = new contactAdapter(this, entryList);
	   	contactLV.setAdapter(cad) ;
			
		if (roster == null) {
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
		
		if (!filledIn()) {
			startActivityForResult(new Intent(this, LoginPage.class), 1);
		} else {
			getNames();
		}
		
		messages = null;
	
		ml = new MessageListener() {
			
			@Override
			public void processMessage(Chat chat, Message mess) {
				if (mess.getBody() == null) return;	
				String s = mess.getBody().replace("'", "''");
				boolean known = false;
				for (RosterEntry entry : roster.getEntries())
					if(chat.getParticipant().contains(entry.getUser()))
						known = true;
				if (them.equals(activeChat.getParticipant()))
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
						mad.notifyDataSetChanged();
						System.out.println("notified!");
					}
				});
			}
		};	
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_exit:
			finish();
			break;
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.add_contact :
			startActivity(new Intent(this, AddContactActivity.class));
			break;
		case R.id.action_login: 
			startActivity(new Intent(this, LoginPage.class));
			break;
		case R.id.action_logout:
			conn.sendPacket(new Presence(Type.unavailable));
			conn.disconnect();
			//startActivity(new Intent(this, LoginPage.class));
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
	/*********************************************************************/
	public String getNames() {
		
		String end = "failed";
		String tmp = "";
		try
		{
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
			end = "success";
		}
		finally
		{
			tmp = end;	
		}
		return tmp;
	}
	/*********************************************************************/
	/*********************************************************************/
	public static boolean ConnectToServer()
	{
		boolean connects = false;
		try {
			conn.connect();
			connects = true;
		} catch (XMPPException e) {
			e.printStackTrace();
			connects = false;
		}
		return connects;
	}
	/*********************************************************************/
	
	public boolean login() {
        boolean success = false;
		
		ccf.setCompressionEnabled(false);
        ccf.setSASLAuthenticationEnabled(false);
        	
        try {
        	if(!conn.isConnected())
				/**********************************************************/
        		//conn.connect();
        		ConnectToServer();
				/**********************************************************/
				conn.login(username, password );
				success = true;
		} catch (XMPPException e) {
			System.out.println(e.getMessage());
			success = false;
		}
        
        //check if contact list is empty, right after login succeeds
        checkContactList();
        runOnUiThread(new Runnable() {
            public void run() {
            	showHide();
            }
        });
        
        return success;
	} 
	
	public void showHide() {
		if(emptyList) {
			//no contacts to display
			//System.out.println("NO CONTACTS TO DISPLAY");
			nc.setVisibility(View.VISIBLE);
		} else {
			//display contacts
			//System.out.println("DISPLAY CONTACTS");
			nc.setVisibility(View.GONE);
		}
	}
	
	/*********************************************************************/
	public boolean checkContactList() {
		if(roster.getEntries().isEmpty()) {	emptyList = true; }
		else { emptyList = false; }
		return true;
	}
	/*********************************************************************/	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		getNames();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			new LoginTask().execute("useless text");
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		try  {
			if (conn.isAuthenticated()) {
				menu.findItem(R.id.action_login).setEnabled(false);
				menu.findItem(R.id.action_logout).setEnabled(true);
			} else {
				menu.findItem(R.id.action_login).setEnabled(true);
				menu.findItem(R.id.action_logout).setEnabled(false);
			}
		} catch (IndexOutOfBoundsException ind) {
			return false;
		}
		return true;
	}
	
}
