package fractals.texchat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import fractals.texchat.R.id;

public class TexActivity extends Activity {

	
	
//Janine-----------------------------------------------------------------------------	
	private SlidingDrawer equationDrawer; 
//END---------------------------------------------------------------------------------	
	
	
	
	static Mimetex mimet = new Mimetex();
	EditText ed;
	
	private Button viewb;
	private Button okButton;

//Janine ********************************************************************************//
	//getting the current position of text field and buttons
	/*private float edTransX;
	private float edTransY;
	
	private float viewBTransX;
	private float viewBTransY;
	
	private float okBTransX;
	private float okBTransY;*/	

	private ImageView preview;
//END ********************************************************************************//
	
/*Janine ----------------------------------------------------------------------------*/	
	private Button slideButton;
	private ImageButton eq1;
	private ImageButton eq2;
	private ImageButton eq3;
	private ImageButton eq4;
	private ImageButton eq5;
	private ImageButton eq6;
	private ImageButton eq7;
	private ImageButton eq8;
	private ImageButton eq9;
	private ImageButton eq10;
	private ImageButton eq11;
	private ImageButton eq12;
	private ImageButton eq13;
	private ImageButton eq14;
	private ImageButton eq15;
	private ImageButton eq16;
	private ImageButton eq17;
	private ImageButton eq18;
	private ImageButton eq19;
	private ImageButton eq20;
	private ImageButton eq21;
	private ImageButton eq22;
	private ImageButton eq23;
	private ImageButton eq24;
	private ImageButton eq25;
	private ImageButton eq26;
	private ImageButton eq27;
	private ImageButton eq28;
	private ImageButton eq29;
	private ImageButton eq30;
	private ImageButton eq31;
	private ImageButton eq32;
	private ImageButton eq33;
	private ImageButton eq34;
	private ImageButton eq35;
	
	//New after last email-------------------------------------------------------------------------
	private ImageButton eq36;
	private ImageButton eq37;
	private ImageButton eq38;
	
	
	private RelativeLayout.LayoutParams edCurrentParams;
	private RelativeLayout.LayoutParams okBCurrentParams;
	private RelativeLayout.LayoutParams viewBCurrentParams;
	private RelativeLayout.LayoutParams previewParams;
	
//Janine **********************************************************************************//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tex);
		
		ed = (EditText)findViewById(id.expEdit);
		viewb = (Button)findViewById(id.viewButton);
		okButton = (Button)findViewById(id.okButton);
		
		edCurrentParams = (RelativeLayout.LayoutParams) ed.getLayoutParams();
		okBCurrentParams = (RelativeLayout.LayoutParams) okButton.getLayoutParams();
		viewBCurrentParams = (RelativeLayout.LayoutParams) viewb.getLayoutParams();
		
		
		viewb.setOnClickListener(viewOnclick);
		okButton.setOnClickListener(okOnclick);	
		
		ed.setSelection(ed.getText().length());
		
		equationDrawer = (SlidingDrawer) this.findViewById(R.id.slidingDrawer1);
		slideButton = (Button) this.findViewById(R.id.handle);
		preview = (ImageView) this.findViewById(id.imageView1);
		
		RelativeLayout.LayoutParams equationDrawerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 270);
		equationDrawerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		equationDrawer.setLayoutParams(equationDrawerParams);
		
		previewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		previewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		previewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		previewParams.setMargins(RelativeLayout.CENTER_HORIZONTAL, 200, 0, 0);
		
		//Getting the translation of the buttons at the start of the activity, before the drawer is opened
		//so that it can go back to it's original positions
		//moves preview image, where preview of equation will be shown
		//preview.setY(120);
		
		equationDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
				public void onDrawerOpened() {
					slideButton.setText("v");
					
					//set ed text field position
					//ed.setX(120);
					//ed.setY(180);
					
					RelativeLayout.LayoutParams edParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					edParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
					edParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
					ed.setLayoutParams(edParams);
					
					preview.setLayoutParams(previewParams);
					
					RelativeLayout.LayoutParams okParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					okParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
					okParams.addRule(RelativeLayout.RIGHT_OF, R.id.expEdit);
					okButton.setLayoutParams(okParams);
					
					RelativeLayout.LayoutParams viewBParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					viewBParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
					viewBParams.addRule(RelativeLayout.LEFT_OF, R.id.expEdit);
					viewb.setLayoutParams(viewBParams);

					//set add button position
					//okButton.setX(360);
					//okButton.setY(180);
			        /*LinearLayout.LayoutParams params2 = (LayoutParams)  okButton.getLayoutParams();
			        params2.leftMargin = 360;
			        params2.topMargin = 180;
			        ed.setLayoutParams(params2);*/
					
					//set view button position
					//viewb.setX(24);
					//viewb.setY(180);
					
					//preview.setY(80);
				}
		});
			
		equationDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				slideButton.setText("^");
				
				//When drawer closed
				ed.setLayoutParams(edCurrentParams);
				okButton.setLayoutParams(okBCurrentParams);
				viewb.setLayoutParams(viewBCurrentParams);
				preview.setLayoutParams(previewParams);
				
				//move down preview image poosition
				//preview.setY(300);
				
				//set ed text field position
				//ed.setX(120);
				//ed.setY(540);
				
				//set add button position
				//okButton.setX(360);
				//okButton.setY(531);
				
				//set view button position
				//viewb.setX(24);
				//viewb.setY(531);
				
			}
		});
//END ********************************************************************************************//
		
		eq1 = (ImageButton) findViewById(id.button1);
		eq2 = (ImageButton) findViewById(id.button2);
		eq3 = (ImageButton) findViewById(id.button3);
		eq4 = (ImageButton) findViewById(id.button4);
		eq5 = (ImageButton) findViewById(id.button5);
		eq6 = (ImageButton) findViewById(id.button6);
		eq7 = (ImageButton) findViewById(id.button7);
		eq8 = (ImageButton) findViewById(id.button8);
		eq9 = (ImageButton) findViewById(id.button9);
		eq10 = (ImageButton) findViewById(id.button10);
		eq11 = (ImageButton) findViewById(id.button11);
		eq12 = (ImageButton) findViewById(id.button12);
		eq13 = (ImageButton) findViewById(id.button13);
		eq14 = (ImageButton) findViewById(id.button14);
		eq15 = (ImageButton) findViewById(id.button15);
		eq16 = (ImageButton) findViewById(id.button16);
		eq17 = (ImageButton) findViewById(id.button17);
		eq18 = (ImageButton) findViewById(id.button18);
		eq19 = (ImageButton) findViewById(id.button19);
		eq20 = (ImageButton) findViewById(id.button20);
		eq21 = (ImageButton) findViewById(id.button21);
		eq22 = (ImageButton) findViewById(id.button22);
		eq23 = (ImageButton) findViewById(id.button23);
		eq24 = (ImageButton) findViewById(id.button24);
		eq25 = (ImageButton) findViewById(id.button25);
		eq26 = (ImageButton) findViewById(id.button26);
		eq27 = (ImageButton) findViewById(id.button27);
		eq28 = (ImageButton) findViewById(id.button28);
		eq29 = (ImageButton) findViewById(id.button29);
		eq30 = (ImageButton) findViewById(id.button30);
		eq31 = (ImageButton) findViewById(id.button31);
		eq32 = (ImageButton) findViewById(id.button32);
		eq33 = (ImageButton) findViewById(id.button33);
		eq34 = (ImageButton) findViewById(id.button34);
		eq35 = (ImageButton) findViewById(id.button35);
		
		//new after last email ------------------------------------------------------
		eq36 = (ImageButton) findViewById(id.button36);
		eq37 = (ImageButton) findViewById(id.button37);
		eq38 = (ImageButton) findViewById(id.button38);
		
		eq1.setOnClickListener(equationSliderOnClick);
		eq2.setOnClickListener(equationSliderOnClick);
		eq3.setOnClickListener(equationSliderOnClick);
		eq4.setOnClickListener(equationSliderOnClick);
		eq5.setOnClickListener(equationSliderOnClick);
		eq6.setOnClickListener(equationSliderOnClick);
		eq7.setOnClickListener(equationSliderOnClick);
		eq8.setOnClickListener(equationSliderOnClick);
		eq9.setOnClickListener(equationSliderOnClick);
		eq10.setOnClickListener(equationSliderOnClick);
		eq11.setOnClickListener(equationSliderOnClick);
		eq12.setOnClickListener(equationSliderOnClick);
		eq13.setOnClickListener(equationSliderOnClick);
		eq14.setOnClickListener(equationSliderOnClick);
		eq15.setOnClickListener(equationSliderOnClick);
		eq16.setOnClickListener(equationSliderOnClick);
		eq17.setOnClickListener(equationSliderOnClick);
		eq18.setOnClickListener(equationSliderOnClick);
		eq19.setOnClickListener(equationSliderOnClick);
		eq20.setOnClickListener(equationSliderOnClick);
		eq21.setOnClickListener(equationSliderOnClick);
		eq22.setOnClickListener(equationSliderOnClick);
		eq23.setOnClickListener(equationSliderOnClick);
		eq24.setOnClickListener(equationSliderOnClick);
		eq25.setOnClickListener(equationSliderOnClick);
		eq26.setOnClickListener(equationSliderOnClick);
		eq27.setOnClickListener(equationSliderOnClick);
		eq28.setOnClickListener(equationSliderOnClick);
		eq29.setOnClickListener(equationSliderOnClick);
		eq30.setOnClickListener(equationSliderOnClick);
		eq31.setOnClickListener(equationSliderOnClick);
		eq32.setOnClickListener(equationSliderOnClick);
		eq33.setOnClickListener(equationSliderOnClick);
		eq34.setOnClickListener(equationSliderOnClick);
		eq35.setOnClickListener(equationSliderOnClick);
		
		//new after last email -----------------------------------------------------------
		eq36.setOnClickListener(equationSliderOnClick);
		eq37.setOnClickListener(equationSliderOnClick);
		eq38.setOnClickListener(equationSliderOnClick);
		
		ed.setOnClickListener(editBoxOnClick);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tex, menu);	

		return true;
	}
	
	OnClickListener viewOnclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ed= (EditText)findViewById(id.expEdit);
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
			ed= (EditText)findViewById(id.expEdit);
			i.putExtra("expression", ed.getText().toString());
			setResult(RESULT_OK, i);

			finish();
		}
	};
	
//Janine *********************************************************************************//
	OnClickListener editBoxOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			equationDrawer.close();
			//set ed text field position
			/*ed.setX(120);
			ed.setY(180);
			
			//set add button position
			okButton.setX(360);
			okButton.setY(180);
			
			//set view button position
			viewb.setX(24);
			viewb.setY(180);
			
			preview.setY(80);*/
			
			RelativeLayout.LayoutParams edParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			edParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			edParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			ed.setLayoutParams(edParams);
			
			preview.setLayoutParams(previewParams);
			
			RelativeLayout.LayoutParams okParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			okParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			okParams.addRule(RelativeLayout.RIGHT_OF, R.id.expEdit);
			okButton.setLayoutParams(okParams);
			
			RelativeLayout.LayoutParams viewBParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			viewBParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			viewBParams.addRule(RelativeLayout.LEFT_OF, R.id.expEdit);
			viewb.setLayoutParams(viewBParams);
		}
	};
//END *************************************************************************************//

	OnClickListener equationSliderOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//ed = (EditText) findViewById(id.expEdit);
			
			switch(v.getId()) {
				case R.id.button1: {
					String equationFinal = "{" + eq1.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;
				}
				case R.id.button2: {
					String equationFinal = "{" + eq2.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button3: {
					String equationFinal = "{" + eq3.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button4: {
					String equationFinal = "{" + eq4.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button5: {
					String equationFinal = "{" + eq5.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button6: {
					String equationFinal = "{" + eq6.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button7: {
					String equationFinal = "{" + eq7.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button8: {
					String equationFinal = "{" + eq8.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button9: {
					String equationFinal = "{" + eq9.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button10: {
					String equationFinal = "{" + eq10.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button11: {
					String equationFinal = "{" + eq11.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button12: {
					String equationFinal = "{" + eq12.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button13: {
					String equationFinal = "{" + eq13.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button14: {
					String equationFinal = "{" + eq14.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button15: {
					String equationFinal = "{" + eq15.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button16: {
					String equationFinal = "{" + eq16.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button17: {
					String equationFinal = "{" + eq17.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button18: {
					String equationFinal = "{" + eq18.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button19: {
					String equationFinal = "{" + eq19.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button20: {
					String equationFinal = "{" + eq20.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button21: {
					String equationFinal = "{" + eq21.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button22: {
					String equationFinal = "{" + eq22.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button23: {
					String equationFinal = "{" + eq23.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button24: {
					String equationFinal = "{" + eq24.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button25: {
					String equationFinal = "{" + eq25.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button26: {
					String equationFinal = "{" + eq26.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button27: {
					String equationFinal = "{" + eq27.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button28: {
					String equationFinal = "{" + eq28.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button29: {
					String equationFinal = "{" + eq29.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button30: {
					String equationFinal = "{" + eq30.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button31: {
					String equationFinal = "{" + eq31.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button32: {
					String equationFinal = "{" + eq32.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button33: {
					String equationFinal = "{" + eq33.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button34: {
					String equationFinal = "{" + eq34.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button35: {
					String equationFinal = "{" + eq35.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				
				//new after last email---------------------------------------------------------------------
				case R.id.button36: {
					String equationFinal = "{" + eq36.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button37: {
					String equationFinal = "{" + eq37.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				case R.id.button38: {
					String equationFinal = "{" + eq38.getContentDescription().toString() + "}";
					ed.setText(ed.getText().toString() + equationFinal);
					ed.setSelection(ed.getText().length());
					break;	
				}
				default: {
					break;
				}
			}
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
		setResult(RESULT_CANCELED);
		finish();
	};

}