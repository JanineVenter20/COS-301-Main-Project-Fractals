package fractals.texchat;

import fractals.texchat.R.id;
import android.os.Bundle;
import android.app.Activity;
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
		
		viewb.setOnClickListener(ocl);
		
		
		return true;
	}
	
	OnClickListener ocl = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText ed= (EditText)findViewById(id.expEdit);
			Bitmap bmp = null;
			bmp = mimet.getLocalBitmap(ed.getText().toString());
			ImageView iv = (ImageView)findViewById(id.imageView1);
			iv.setImageBitmap(bmp);
		}
	};

}
