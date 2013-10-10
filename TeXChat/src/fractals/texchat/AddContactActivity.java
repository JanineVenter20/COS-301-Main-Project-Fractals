package fractals.texchat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_contact, menu);
		
		Button done = (Button)findViewById(R.id.addContactButton);
		
		final EditText un = (EditText)findViewById(R.id.editJid);
		final EditText nam = (EditText)findViewById(R.id.editNam);

		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("username", un.getText().toString());
				returnIntent.putExtra("alias", nam.getText().toString());
				setResult(2, returnIntent);
				finish();
			}
		});
		return true;
	}


}
