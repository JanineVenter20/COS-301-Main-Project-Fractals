package fractals.texchat;

import java.io.ObjectOutputStream.PutField;

import fractals.texchat.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class TexActivity extends Activity {

	static Mimetex mimet = new Mimetex();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tex);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tex, menu);
		
		Button viewb = (Button)findViewById(id.viewButton);
		Button okButton = (Button)findViewById(id.okButton);
		viewb.setOnClickListener(viewOnclick);
		okButton.setOnClickListener(okOnclick);
		return true;
	}
	
	OnClickListener viewOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText ed= (EditText)findViewById(id.expEdit);
			Bitmap bmp = null;
			bmp = mimet.getLocalBitmap(ed.getText().toString());
			ImageView iv = (ImageView)findViewById(id.imageView1);
			iv.setImageBitmap(bmp);
		}
	};
	
	OnClickListener okOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent();
			EditText ed= (EditText)findViewById(id.expEdit);
			i.putExtra("expression", ed.getText().toString());
			setResult(RESULT_OK, i);
			finish();
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
		setResult(RESULT_CANCELED);
		finish();
	};

}
