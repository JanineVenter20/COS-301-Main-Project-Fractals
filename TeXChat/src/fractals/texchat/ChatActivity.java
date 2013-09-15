package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
	ListView messageLV;
	Activity c = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	MainActivity.messages = new ArrayList<Message>();
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle extras = this.getIntent().getExtras();
        
        contact = extras.getString("contact");
        ArrayList<MessageDetail> messagesGotFromDB = MainActivity.dbHandler.selectMessages(contact, 10);
        Message mess;
        for (int i = messagesGotFromDB.size()-1; i >= 0 ; i--) {
        	mess = new Message();
        	mess.addBody("", messagesGotFromDB.get(i).getMessageBody());
        	if (messagesGotFromDB.get(i).getSentReceived()) {
        		mess.setFrom(MainActivity.username);
        	} else {
        		mess.setFrom(contact.substring(0, contact.indexOf("@")));
        	}
        	MainActivity.messages.add(mess);
        }
        
       
        messageLV = (ListView) findViewById(R.id.messageView);
        MainActivity.mad = new messageAdapter(this, MainActivity.messages);
        messageLV.setAdapter(MainActivity.mad);
        
		//System.out.println(" !!!! "+contact);
        
        final Chat chat = MainActivity.activeChat;
        
        Toast.makeText(this, "chatting with " + contact , Toast.LENGTH_SHORT).show();
        
        OnClickListener ocl = new OnClickListener() {
			public void onClick(View v) {
				try {
					EditText et = (EditText)findViewById(id.messageInput);
					Message ms = new Message();
					ms.setBody(et.getText().toString());
					et.setText("");
					ms.setTo(contact);
					chat.sendMessage(ms);
					MainActivity.messages.add(ms);
					String s = ms.getBody().replace("'", "''");
					MainActivity.dbHandler.addToMessages(new Packet(ms.getTo(), s , true));
					MainActivity.mad.notifyDataSetChanged();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		};
		
        
        Button sendButton = (Button)findViewById(R.id.sendButton);
        
        final Button fxButton = (Button)findViewById(id.fxButton);
        fxButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent fxIntent = new Intent(c, TexActivity.class);
				c.startActivityForResult(fxIntent, 1);
				
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (resultCode == RESULT_OK) {
    		EditText ed = (EditText)findViewById(id.messageInput);
        	ed.append("$"+data.getExtras().getString("expression")+"$");
    	}
    }


}
