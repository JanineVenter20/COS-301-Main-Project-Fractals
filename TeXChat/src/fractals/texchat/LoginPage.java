package fractals.texchat;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import fractals.texchat.R.id;
import android.content.Context;
import android.content.Intent;

public class LoginPage extends Activity {
	
	Context c = this;
	CheckBox RememberMe;
	boolean remember = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        
        Button Login = (Button)findViewById(id.loginButton);
        Button Cancel = (Button)findViewById(id.cancelButton);
        Button Register = (Button)findViewById(id.register_button);
        RememberMe = (CheckBox) findViewById(R.id.rememberBox);
        
        Login.setOnClickListener(ocl);
        Cancel.setOnClickListener(ocl2);
        Register.setOnClickListener(ocl3);
       
        //clear table for first try
        //MainActivity.dbHandler.clearRememberMe();
        
        //check if user should be remembered
		ArrayList<String> loggedIn = new ArrayList<String>(); 
		System.out.println(MainActivity.dbHandler);
		loggedIn = MainActivity.dbHandler.getLoggedInUser();
		
		if(loggedIn.isEmpty()){ System.out.println("No rememberme information available"); }
		else
		{	
			if(loggedIn.get(2).equals("true"))
			{
				remember = true;
			}
			else
			{
				remember = false;
				EditText userE = (EditText)findViewById(id.usernameEdit);
				EditText passE = (EditText)findViewById(id.passwordEdit);
				userE.setText(loggedIn.get(0));
				passE.setText(loggedIn.get(1));
			}
		}
		if (loggedIn.size() > 1) {
			MainActivity.username = loggedIn.get(0);
			MainActivity.password = loggedIn.get(1);
		}
		
		//if true - should be remembered - do click automatically
		if(remember)
		{
			//log the user in automatically and set the check box to checked
			
			RememberMe.setChecked(true);
			Login.performClick();
		}
		//if false allow normal login
    }
    
    OnClickListener ocl = new OnClickListener() {
		
		public void onClick(View v) {
					
			if(!remember)
			{
				//let the user log in normally

				EditText userE = (EditText)findViewById(id.usernameEdit);
				EditText passE = (EditText)findViewById(id.passwordEdit);
				
				MainActivity.username = userE.getText().toString();
				MainActivity.password = passE.getText().toString();
				
				MainActivity.username = MainActivity.username.replace("'", "''");
				MainActivity.password = MainActivity.password.replace("'", "''");
				//if remember me is ticked save details in the table
				if(RememberMe.isChecked())
				{
					MainActivity.dbHandler.addRememberMe(MainActivity.username, MainActivity.password, true); 
				}
				else
				{
					MainActivity.dbHandler.addRememberMe(MainActivity.username, MainActivity.password, false);
				}
			} 	 
			
			setResult(1);
			finish();
		}
	};
	
	OnClickListener ocl2 = new OnClickListener() {
		
		public void onClick(View v) {
			setResult(0);
			finish();
		}
	};
	
	OnClickListener ocl3 = new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent = new Intent(c, RegisterActivity.class);
			startActivity(intent);
		}
	};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
		setResult(0);
		finish();
		super.onDestroy();
    }
}
