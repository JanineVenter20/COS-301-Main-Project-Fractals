package fractals.texchat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class XMPPClient extends Activity {

		public static String username = "";
		public static String password = "";
		final static String hostName = "192.168.2.106";
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
			public void processMessage(Chat arg0, Message arg1) {
				Log.println(RESULT_OK, "Output...", arg1.toString());
			}
		};
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmppclient);
        setTitle("LOGIN");
        
        if (!filledIn()) {
        	Intent login = new Intent(this, LoginPage.class);
        	startActivityForResult(login, 1);       
        }
        else {
        	login();
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_xmppclient, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	login();
    	Intent chat = new Intent(this, ChatActivity.class);
        startActivity(chat);
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
			Log.i("logged in", username+" " + password + " presence packet sent..");
			
		   	cm = conn.getChatManager();
		   	cm.addChatListener(cml);
		   	activeChat = cm.createChat("stephan1@fractals.net", "test", ml);		   	 
		   	activeChat.addMessageListener(ml);
		   	
		   	roster = conn.getRoster();
		   	Collection<RosterEntry> entries = roster.getEntries();
		   	List<RosterEntry> entryList = new ArrayList<RosterEntry>(entries);
		   	contactLV = (ListView)findViewById(R.id.ContactListView);
		   	contactLV.setAdapter(new ArrayAdapter<RosterEntry>(this, R.layout.contactlayout, entryList));
		   	Log.i("output", "2");

		   	
		} catch (XMPPException e) {
			e.printStackTrace();
		}
    }
}





