package com.example.my_sql_app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

public class MainActivity extends Activity {
	
		//MY PRIVATE MEMBERS
		Button sendAlice;		// id = sendbutton1
		EditText messageAlice; 	// id = messagebox1
		Button sendBob;			// id = sendbutton2
		EditText messageBob; 	// id = messagebox2
		Button view;			// id = view
		Button delete;			// id = delete
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
    		//NOT MY STUFF
	    	super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
		
	        //GET TEXT - NOT NEEDED ANYMORE
	        sendAlice = (Button)findViewById(R.id.sendbutton1);
	        messageAlice = (EditText)findViewById(R.id.messagebox1); 
	        sendBob = (Button)findViewById(R.id.sendbutton2);
	        messageBob = (EditText)findViewById(R.id.messagebox2);
	        view = (Button)findViewById(R.id.view);
	        delete = (Button)findViewById(R.id.delete);
	       
	        //CREATE THE DBHANDLER - ***********************************************
	        final DatabaseHandler dbHandler = new DatabaseHandler(this);
	        
	        //SEND TO BOB BUTTON
	        sendAlice.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					//CREATE A PACKET TO SEND (CONTAINING USER & MESSAGE)
	        		final Packet sendPacket = new Packet("Bob", "[sent message text]", true); //To Bob
	        		dbHandler.addToMessages(sendPacket);
	        		
			    }
			});
	        
	        //SEND TO ALICE BUTTON
	        sendBob.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					//CREATE A PACKET FOR RECEIVE (CONTAINING USER & MESSAGE)
					final Packet receivedPacket = new Packet("Bob", "[received message text]", false); //Reply from Bob
					dbHandler.addToMessages(receivedPacket);	
					
				}
			});  
	        
	        //VIEW BUTTON
	        view.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					//SELECT SENT MESSAGES FROM 'USER' - CURRENTLY BOB
					dbHandler.selectMessages("Bob", 10);
					
				}
			});
	        
	        //DELETE USER CHAT BUTTON
	        delete.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {

				
					//TO TEST THE REMEMBER ME TABLE
					boolean result = dbHandler.addRememberMe("Chella", "chella");
					if(result)
					{
						Log.i("INFORMATION", "REM [TRUE]");
					}
					else
					{
						Log.i("INFORMATION", "REM [FALSE]");						
					}
				
					/*	
				    //TO TEST THE DELETE CHAT WITH USER
					boolean result = dbHandler.deleteMessages("Bob");
					if(result)
					{
						Log.i("INFORMATION", "DELETE [TRUE]");
					}
					else
					{
						Log.i("INFORMATION", "DELETE [FALSE]");						
					}
					*/
				}
			});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    		//NOT MY STUFF
	    	getMenuInflater().inflate(R.menu.activity_main, menu);
	        return true;
        
    }
}
