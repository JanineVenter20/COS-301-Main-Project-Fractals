package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import fractals.texchat.DatabaseHandler.MessageDetail;
import fractals.texchat.R.id;

public class ChatActivity extends Activity {

	String contact;
	ArrayList<Message> messages;
	ListView messageLV;
	messageAdapter mad;
	Context c = this;
	Mimetex mt = new Mimetex();
//
//	static {
//		System.loadLibrary("mimetex");
//	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	messages = new ArrayList<Message>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = this.getIntent().getExtras();
        
        final Activity ac = this;
        contact = extras.getString("contact");
        ArrayList<MessageDetail> messagesGotFromDB = MainActivity.dbHandler.selectMessages(contact);
        Message mess;
        for (int i = 0; i < messagesGotFromDB.size(); i++) {
        	mess = new Message();
        	mess.addBody("", messagesGotFromDB.get(i).getMessageBody());
        	if (messagesGotFromDB.get(i).getSentReceived()) {
        		mess.setFrom("you");
        	} else {
        		mess.setFrom(contact);
        	}
        	messages.add(mess);
        }
        
       
        messageLV = (ListView) findViewById(R.id.messageView);
        mad = new messageAdapter(this, messages);
        messageLV.setAdapter(mad);
        MessageListener ml = new MessageListener() {
			public void processMessage(Chat chat, Message mess) {
				messages.add(mess);
				System.out.println(mess.getFrom());
				MainActivity.dbHandler.addToMessages(new Packet(contact,mess.getBody(), false));
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
					MainActivity.dbHandler.addToMessages(new Packet(ms.getTo(), ms.getBody(), true));
					mad.notifyDataSetChanged();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		};
		
        
        Button sendButton = (Button)findViewById(R.id.sendButton);
        
        Button fxButton = (Button)findViewById(id.fxButton);
        fxButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent fxIntent = new Intent(c, TexActivity.class);
				c.startActivity(fxIntent);
				
			}
		});
        sendButton.setOnClickListener(ocl);
        

    }
    
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
