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
import org.jivesoftware.smackx.ChatState;
import org.jivesoftware.smackx.ChatStateListener;

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
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static String username = "";
	public static String password = "";
	static String hostName = "192.168.137.1";
	static String service = "fractals.texchat";
	static int port = 5222;
	
	public static String me = "You", // local alias saved in pref..
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
		
	//oorgedra 
	String contact;
	public static ArrayList<Message> messages= new ArrayList<Message>();
	public static messageAdapter mad;
	ArrayList<RosterEntry> entryList = new ArrayList<RosterEntry>();
	public static contactAdapter cad;
	
	public static ChatManager cm;
		
	class LoginTask extends AsyncTask<String, String, String> {
		
		@Override
			protected String doInBackground(String... params) {
				if (!conn.isConnected()) login();
				return null;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (dbHandler == null)
			dbHandler = new DatabaseHandler(this);
		
		
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
					System.out.println(chat.getParticipant());
					if (!locally) {
						activeChat = cm.createChat(chat.getParticipant(), ml);
						mad = new messageAdapter(c, messages);
					} else {
						activeChat = chat;
					}
				}
			});
		}
		
		contactLV = (ListView)findViewById(R.id.ContactListView);
	   	cad = new contactAdapter(this, entryList);
	   	contactLV.setAdapter(cad) ;
			
		if (roster == null) {
			roster = conn.getRoster();
			roster.addRosterListener(new RosterListener() {
				@Override
				public void presenceChanged(Presence pres) {  
					getNames();
				}
				@Override
				public void entriesUpdated(Collection<String> arg0) {  getNames(); }
				@Override
				public void entriesDeleted(Collection<String> arg0) {  getNames(); }
				@Override
				public void entriesAdded(Collection<String> arg0) { getNames(); }
			});
		}
		
		//System.out.println(hostName+"\n"+ service+"\n"+port );
		
		if (!filledIn()) {
			startActivityForResult(new Intent(this, LoginPage.class), 1);
		} else {
			getNames();
		}
		
		messages = new ArrayList<Message>();
	
	
		ml = new ChatStateListener() {
			@Override
			public void stateChanged(Chat arg0, ChatState arg1) { }
			@Override
			public void processMessage(Chat chat, Message mess) {
				if (mess.getBody() == null) return;
				messages.add(mess);			
				String s = mess.getBody().replace("'", "''");
				System.out.println(chat.getParticipant());
				dbHandler.addToMessages(new Packet(chat.getParticipant(),s, false));
				c.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mad.notifyDataSetChanged();
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
			System.exit(0);
			break;
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
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
		entryList.addAll(roster.getEntries());
		c.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				cad.notifyDataSetChanged();
				
			}
		});	
	}
	
	public void login() {
        ccf.setCompressionEnabled(false);
        ccf.setSASLAuthenticationEnabled(false);
        	
        try {
			conn.connect();
			conn.login(username, password );
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}
        done = true;
    }
	
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
			Toast.makeText(context, "presence set to \"online\"", Toast.LENGTH_SHORT).show();
			while(!done) {}
				getNames();
		}
	}
	
	
	
}
