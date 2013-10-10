package fractals.texchat;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import fractals.texchat.R.id;

public class RegisterActivity extends Activity {

	Button register, cancel;
	Context c = this;
	EditText username, displayName, pass, confirmPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
	    register = (Button)findViewById(id.register);
	    cancel = (Button)findViewById(id.cancel);
		
	    register.setOnClickListener(onc1);
	    cancel.setOnClickListener(onc2);
	    
		//get the values
		username = (EditText)findViewById(id.name_edit);
		displayName = (EditText)findViewById(id.display_name_edit);
		pass = (EditText)findViewById(id.pass_edit);
		confirmPass = (EditText)findViewById(id.confirm_pass_edit);
		
		username.requestFocus();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
    OnClickListener onc1 = new OnClickListener() {
    	public void onClick(View v)
    	{
    		//create a user account on the server
    		String getUsername = "";
    		String getDisplayName = ""; 
    		String getPass = ""; 
    		String getConfirmPass = "";
    		
    		
    		boolean valid = true;
    		if(isEmpty(username) || isEmpty(displayName) || isEmpty(pass) || isEmpty(confirmPass))
    		{
				Toast.makeText(c, "Please ensure the form is complete", Toast.LENGTH_LONG).show();
				valid = false;
    		}
    		else
    		{
   	    		valid = true;
    			getUsername = username.getText().toString();
	    		getDisplayName = displayName.getText().toString();
	    		getPass = pass.getText().toString();
	    		getConfirmPass = confirmPass.getText().toString();
	    		
	    		if(!getPass.equals(getConfirmPass))
	    		{
	    			valid = false;
					Toast.makeText(c, "Your password fields do not match", Toast.LENGTH_LONG).show();
					pass.setText("");
					confirmPass.setText("");
					pass.requestFocus();
	    		}
	    		else
	    		{
	    			valid = true;
	    		}
    		}
    		boolean registered = false;
    		if(valid)
    		{
    	        try 
    	        {
    	        	if (!MainActivity.conn.isConnected());
    	        		new MainActivity.ConnTask();
    	        	
    	        	while (!MainActivity.conn.isConnected()) {}
    	        	
    	        	Map<String, String> attributes = new HashMap<String, String>();
    	        	attributes.put("username", getDisplayName.trim());
    	        	attributes.put("password", getPass.trim());
    	        	attributes.put("email", "email@email.com");
    	        	attributes.put("name", getUsername.trim());
        	        
    	        	AccountManager accountManager = new AccountManager(MainActivity.conn);
        	        accountManager.createAccount(getDisplayName.trim(), getPass.trim(), attributes); 
    	        	
    	        	Toast.makeText(c, "Your registration was successful", Toast.LENGTH_LONG).show();
    	        	registered = true;
    	        } 
    	        catch (XMPPException e) 
    	        {
    	        	Toast.makeText(c, "Registration failed?", Toast.LENGTH_LONG).show();
    	        	System.out.println(e.getMessage());
    	        	registered = false;
    	        } 
    		}
    		else
    		{
				//should never get here though
    			//Toast.makeText(c, "Invalid input", Toast.LENGTH_LONG).show();
    		}
    		if(registered)
    		{
    			//go to contacts page
        		Intent intent = new Intent(c, MainActivity.class);
        		startActivity(intent);
    		}
    		else
    		{
    			//do nothing
    		}
    	}
    };
    
    OnClickListener onc2 = new OnClickListener() {
    	public void onClick(View v)
    	{
    		//go back to the login page
    		Intent intent = new Intent(c, LoginPage.class);
    		startActivity(intent);
    	}
    };
    
    private boolean isEmpty(EditText etText) 
    {
        if (etText.getText().toString().trim().length() > 0) 
        {
        	return false;
        } 
        else 
        {
            return true;
        }
    }
}
