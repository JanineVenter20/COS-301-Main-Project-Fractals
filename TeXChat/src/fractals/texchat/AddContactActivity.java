package fractals.texchat;

import fractals.texchat.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddContactActivity extends Activity {

	Context This = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		
		Button done = (Button)findViewById(id.addContactButon);
		
		final EditText un = (EditText)findViewById(id.editJid);
		final EditText nam = (EditText)findViewById(id.editNam);
		System.out.println("b4 OK CLICKED!");
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("OK CLICKED!");
				Intent returnIntent = new Intent();
				returnIntent.putExtra("username", un.getText().toString());
				returnIntent.putExtra("alias", nam.getText().toString());
				setResult(2, returnIntent);
				
				finish();
			}
		});
	}


}
