package fractals.texchat;

import fractals.texchat.R.id;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        
        Button Login = (Button)findViewById(id.loginButton);
        Login.setOnClickListener(ocl);
        
    }
    
    OnClickListener ocl = new OnClickListener() {
		
		public void onClick(View v) {
			EditText userE = (EditText)findViewById(id.usernameEdit);
			EditText passE = (EditText)findViewById(id.passwordEdit);
			
			 XMPPClient.username = userE.getText().toString();
			 XMPPClient.password = passE.getText().toString();
			 finish();
		}
	};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login_page, menu);
        return true;
    }

}
