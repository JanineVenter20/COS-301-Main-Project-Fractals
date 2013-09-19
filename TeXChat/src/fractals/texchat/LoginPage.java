package fractals.texchat;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import fractals.texchat.R.id;
import android.content.Context;
import android.content.Intent;

public class LoginPage extends Activity {
	
	Context c = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        
        Button Login = (Button)findViewById(id.loginButton);
        Button Cancel = (Button)findViewById(id.cancelButton);
        Button Register = (Button)findViewById(id.register_button);
        Login.setOnClickListener(ocl);
        Cancel.setOnClickListener(ocl2);
        Register.setOnClickListener(ocl3);
        
    }
    
    OnClickListener ocl = new OnClickListener() {
		
		public void onClick(View v) {
			EditText userE = (EditText)findViewById(id.usernameEdit);
			EditText passE = (EditText)findViewById(id.passwordEdit);
			
			 MainActivity.username = userE.getText().toString();
			 MainActivity.password = passE.getText().toString();
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
		
		System.out.println("helloooo whooohhoooo");
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
