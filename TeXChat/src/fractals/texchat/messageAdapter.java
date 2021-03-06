package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class messageAdapter extends BaseAdapter {
 
 Mimetex mt = new Mimetex();
 Context context;
 ArrayList<Message> messages;
 LayoutInflater layoutinflater;
 SplitString splitter = new SplitString();
 
 public messageAdapter(Context mcontext, ArrayList<Message> mess) {
  messages = mess;
  context = mcontext;
  layoutinflater = (LayoutInflater)(context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 }


 @Override
 public int getCount() {
  return messages.size();
 }


 @Override
 public Object getItem(int arg0) {
  return messages.get(arg0);
 }


 @Override
 public long getItemId(int arg0) {
  return arg0;
 }


 @Override
 public View getView(int position, View convertView, ViewGroup parent) {
  LinearLayout messageView;
  int grav = Gravity.RIGHT;
  ScaleType sc = ScaleType.FIT_END;
  LinearLayout msv = (LinearLayout)layoutinflater.inflate(R.layout.message_view, parent, false);
  messageView = (LinearLayout)layoutinflater.inflate(R.layout.message_view, parent, false);
  
  messageView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
  
  TextView from = new TextView(context);
  
  from.setTextSize(14);
  TextView body;
  ImageView bodyI;
  if (messages.get(position).getFrom() == null) messages.get(position).setFrom(MainActivity.username);
  if (messages.get(position).getFrom().contains(MainActivity.username)) {
    messageView.setBackgroundResource(R.drawable.blue9patch);
    grav = Gravity.LEFT;
    sc = ScaleType.FIT_START;
    from.setGravity(grav);
  } else {
   messageView.setBackgroundResource(R.drawable.green9patch);
   grav = Gravity.RIGHT;
   sc = ScaleType.FIT_END;
   from.setGravity(grav);
  }
  
  messageView.addView(from);
  String fromS = messages.get(position).getFrom().toString();
  
  if (messages.get(position).getBody() == null || messages.get(position).getBody().equals("")) {
	  body = new TextView(context);
	  body.setText("Empty");
	  messageView.addView(body);
	  body.setGravity(grav);
  } else {
	  ArrayList<String> s = splitter.split(messages.get(position).getBody());
	  for (String str : s) {
		  if (str.contains("$")) {
			  bodyI = new ImageView(context);
			  bodyI.setImageBitmap(mt.getLocalBitmap(str));
			  bodyI.setScaleType(sc);
			  bodyI.setPadding(30, 0, 30, 0);
			  messageView.addView(bodyI);
		 } else {
			 body = new TextView(context);
			 body.setPadding(30, 0, 30, 0);
			 body.setText(str);
			 body.setGravity(grav);
			 messageView.addView(body);
		 }
  }
   
  }
  if (fromS.contains(MainActivity.username))
   from.setText(MainActivity.me);
  else from.setText(MainActivity.them);
  
  messageView.setPadding(20, 5, 20, 5);
  msv.setGravity(grav);
  msv.addView(messageView);
  
  return msv;
 }

}