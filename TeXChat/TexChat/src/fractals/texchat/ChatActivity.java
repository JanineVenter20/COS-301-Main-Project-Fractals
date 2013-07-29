package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import fractals.texchat.R.id;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ChatActivity extends Activity {

	String contact;
	ListView messageLV;
	ArrayList<Message> messages = new ArrayList<Message>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = this.getIntent().getExtras();
        contact = extras.getString("contact");
        
        messageLV = (ListView)findViewById(id.messageView);
        messageLV.setAdapter(new messageAdapter(this, messages));
        
        MessageListener ml = new MessageListener() {
			public void processMessage(Chat chat, Message mess) {
				messages.add(mess);
			}
		};
        
        ChatManager chatMan = MainActivity.conn.getChatManager();
        final Chat chat = chatMan.createChat(contact, ml);
        
        Toast.makeText(this, "chatting with " + contact , Toast.LENGTH_SHORT).show();
        
        OnClickListener ocl = new OnClickListener() {
			public void onClick(View v) {
				try {
					EditText et = (EditText)findViewById(id.messageInput);
					chat.sendMessage(et.getText().toString());
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		};
		
		PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		MainActivity.conn.addPacketListener(new PacketListener() {
			
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
        getMenuInflater().inflate(R.layout.activity_chat, menu);
        return true;
    }



}
