package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import fractals.texchat.R.id;

public class ChatActivity extends Activity {

	String contact;
	ArrayList<Message> messages;
	ListView messageLV;
	messageAdapter mad;
	Context c = this;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = this.getIntent().getExtras();
        
        final Activity ac = this;
        
        contact = extras.getString("contact");
        
        messages = new ArrayList<Message>();
        messageLV = (ListView) findViewById(R.id.messageView);
        mad = new messageAdapter(this, messages);
        messageLV.setAdapter(mad);
        
        MessageListener ml = new MessageListener() {
			public void processMessage(Chat chat, Message mess) {
				messages.add(mess);
				ac.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mad.notifyDataSetChanged();
						
					}
				});
			}
		};
        
        ChatManager chatMan = MainActivity.conn.getChatManager();
        final Chat chat = chatMan.createChat(contact, ml);
        
        Toast.makeText(this, "chatting with " + contact , Toast.LENGTH_SHORT).show();
        
        OnClickListener ocl = new OnClickListener() {
			public void onClick(View v) {
				try {
					EditText et = (EditText)findViewById(id.messageInput);
					Message ms = new Message();
					ms.setBody(et.getText().toString());
					ms.setTo(contact);
					chat.sendMessage(ms);
					messages.add(ms);
					mad.notifyDataSetChanged();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		};
		
        
        Button sendButton = (Button)findViewById(R.id.sendButton);
        Log.i("debug..",sendButton.toString());
        sendButton.setOnClickListener(ocl);
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
