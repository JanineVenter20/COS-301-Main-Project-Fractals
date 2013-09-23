package fractals.texchat;

import org.jivesoftware.smack.XMPPException;

import fractals.texchat.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
		
		Button done = (Button)findViewById(id.addContactButton);
		
		final EditText un = (EditText)findViewById(id.editJid);
		final EditText nam = (EditText)findViewById(id.editJid);

		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					MainActivity.roster.createEntry(un.getText().toString(), nam.getText().toString(), null);
				} catch (XMPPException e) {
					Toast.makeText(This, "Could not add the user "+un.getText(), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		});
		return true;
	}


}
