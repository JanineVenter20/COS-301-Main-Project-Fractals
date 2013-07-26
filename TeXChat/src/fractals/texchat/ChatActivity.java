package fractals.texchat;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChatActivity extends Activity {

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        
        MessageListener ml = new MessageListener() {
			public void processMessage(Chat chat, Message mess) {
				Log.i("user..", mess.getFrom());
			}
		};
        
        ChatManager chatMan = XMPPClient.conn.getChatManager();
        final Chat chat = chatMan.createChat("stephan1@fractals.net", ml);
        
        OnClickListener ocl = new OnClickListener() {
			public void onClick(View v) {
				try {
					chat.sendMessage("helo!");
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		};
		
		PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		XMPPClient.conn.addPacketListener(new PacketListener() {
			
			public void processPacket(Packet arg0) {
				Message message = (Message)arg0;
				Log.i("message" , message.getBody());
			}
		}, filter);
        
        Button sendButton = (Button)findViewById(R.id.sendButton);
        Log.i("debug..",sendButton.toString());
        sendButton.setOnClickListener(ocl);
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_chat, menu);
        return true;
    }



}
