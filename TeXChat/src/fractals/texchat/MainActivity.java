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
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static String username = "eminating";
	public static String password = "Kalahar1";
	final static String hostName = "192.168.2.103";
	final static String service = "stephan-pc";
	final static int port = 5222;
	
	public static final ConnectionConfiguration ccf = new ConnectionConfiguration(hostName, port, service);
	public static final XMPPConnection conn = new XMPPConnection(ccf);
	Roster roster;
	Chat activeChat;
	ChatManager cm;

	ListView contactLV;
	
	ChatManagerListener cml = new ChatManagerListener() {			 
		public void chatCreated(Chat chat, boolean locally) {
			if (!locally) {
				activeChat = cm.createChat(chat.getParticipant(), "test", ml);
			}
		}
	};
	
	MessageListener ml = new MessageListener() {			
		@Override
		public void processMessage(Chat arg0, Message arg1) {
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!filledIn()) {
			setTitle("NOT LOGGED IN");
        	//Intent login = new Intent(this, LoginPage.class);
        	//startActivityForResult(login, 1);       
        }
        else {
        	login();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean filledIn() {
    	if (username.equals("") || password.equals(""))
    			return false;
    	else return true;
    } 
	
	public void login() {
        ccf.setCompressionEnabled(false);
        ccf.setSASLAuthenticationEnabled(false);
        	
        try {
			conn.connect();
			conn.login(username, password);
			conn.sendPacket(new Presence(Presence.Type.available));
			Toast.makeText(this, "presence set to \"online\"", Toast.LENGTH_SHORT).show();
			
		   			   	
		   	roster = conn.getRoster();
		   	Collection<RosterEntry> entries = roster.getEntries();
		   	ArrayList<RosterEntry> entryList = new ArrayList<RosterEntry>(entries);
		   	contactLV = (ListView)findViewById(R.id.ContactListView);
		   	contactLV.setAdapter(new contactAdapter(this, entryList)) ;		   	
		} catch (XMPPException e) {
			e.printStackTrace();
		}
    }
	
	
	
}
