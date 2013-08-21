package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import fractals.texchat.R.id;

public class ChatActivity extends Activity {

	String contact;
	ArrayList<Message> messages;
	ListView messageLV;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = this.getIntent().getExtras();
        
        //System.loadLibrary("mimetex");
        
        contact = extras.getString("contact");
        
        messages = new ArrayList<Message>();
        messageLV = (ListView) findViewById(R.id.messageView);
        messageLV.setAdapter(new messageAdapter(this, messages));
        
        MessageListener ml = new MessageListener() {
			public void processMessage(Chat chat, Message mess) {
				messages.add(mess);
				((BaseAdapter)messageLV.getAdapter()).notifyDataSetChanged();
			}
		};
        
        ChatManager chatMan = MainActivity.conn.getChatManager();
        final Chat chat = chatMan.createChat(contact, ml);
        
        Toast.makeText(this, "chatting with "+ chat.getParticipant() , Toast.LENGTH_SHORT).show();
        
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

        Button sendButton = (Button)findViewById(R.id.sendButton);
        //Log.i("debug..",sendButton.toString());
        sendButton.setOnClickListener(ocl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
