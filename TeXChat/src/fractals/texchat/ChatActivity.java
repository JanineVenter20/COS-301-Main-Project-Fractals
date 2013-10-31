package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fractals.texchat.DatabaseHandler.MessageDetail;
import fractals.texchat.R.id;

public class ChatActivity extends Activity {

	String contact;
	ListView messageLV;
	Activity c = this;
	TextView nc;
	public static String displayName = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*************************************************************************************/
		final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		/*****************************************/
        setContentView(R.layout.activity_chat);
		/*****************************************/
    	
    	MainActivity.messages = new ArrayList<Message>();
    	
        Bundle extras = this.getIntent().getExtras();
        
        contact = extras.getString("contact");
     	MainActivity.them = extras.getString("name");
        ArrayList<MessageDetail> messagesGotFromDB = MainActivity.dbHandler.selectMessages(MainActivity.username, contact, 10);
        
        nc = (TextView)findViewById(R.id.no_messages);
        nc.setVisibility(View.GONE);
        
        if(messagesGotFromDB.isEmpty()) {
        	nc.setVisibility(View.VISIBLE); } 
        else { 
        	nc.setVisibility(View.GONE); }
        
        Message mess;
        for (int i = messagesGotFromDB.size()-1; i >= 0 ; i--) {
        	mess = new Message();
        	mess.addBody("", messagesGotFromDB.get(i).getMessageBody());
        	if (messagesGotFromDB.get(i).getSentReceived()) {
        		mess.setFrom(MainActivity.username);
        	} else {
        		mess.setFrom(contact);
        	}
        	MainActivity.messages.add(mess);
        }
        
        String contactR = MainActivity.them.substring(1);
        String contactF = MainActivity.them.substring(0, 1);
        String contactDisplay = "  " + contactF.toUpperCase() + contactR;
        displayName = contactDisplay;
		if(customTitleSupported){
			System.out.println("CUSTOM SUPPORTED!!!");
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		}
		
		final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
    	if (myTitleText != null) {
        		myTitleText.setText(contactDisplay);
        		myTitleText.setTextColor(Color.WHITE);
    	}
       
        messageLV = (ListView) findViewById(R.id.messageView);
        MainActivity.mad = new messageAdapter(this, MainActivity.messages);
        messageLV.setAdapter(MainActivity.mad);
        
        final Chat chat = MainActivity.activeChat;
        
        Toast.makeText(this, "chatting with " + contact , Toast.LENGTH_SHORT).show();
        
        OnClickListener ocl = new OnClickListener() {
			public void onClick(View v) {
				
				ArrayList<MessageDetail> tmp = MainActivity.dbHandler.selectMessages(MainActivity.username, contact, 10);
				if(tmp.isEmpty()){nc.setVisibility(View.GONE);}
				try {
					EditText et = (EditText)findViewById(id.messageInput);
					Message ms = new Message();
					ms.setBody(et.getText().toString());
					et.setText("");
					ms.setTo(contact);
					String s = ms.getBody().replace("'", "''");
					if (!MainActivity.conn.isConnected()) { 
						MainActivity.dbHandler.addToMessages(new Packet(MainActivity.username, contact , s, false , true));
						MainActivity.messages.add(ms);
						MainActivity.mad.notifyDataSetChanged();
					} else {
						chat.sendMessage(ms);
						MainActivity.messages.add(ms);
						MainActivity.dbHandler.addToMessages(new Packet(MainActivity.username, contact , s, true  , true));
						MainActivity.mad.notifyDataSetChanged();
					}
				} catch (XMPPException e) {
					c.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(c, "Oops, something went wrong", Toast.LENGTH_LONG).show();
						}
					});
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_chat, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (resultCode == RESULT_OK) {
    		EditText ed = (EditText)findViewById(id.messageInput);
        	ed.append("$$"+data.getExtras().getString("expression")+"$$");
    	}
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) { 
    	case R.id.action_settings :
    		startActivity(new Intent(this, SettingsActivity.class));
    		break;
    	case R.id.action_delete_all:
			//delete all info from the database for a specific user - contact
			boolean result = MainActivity.dbHandler.deleteMessages(MainActivity.username, contact);
			if(result == true) {
				finish();
				startActivity(getIntent());			
			}
			break;
	    default:
			break;
    	}
    	return true;
    	
    }


}
