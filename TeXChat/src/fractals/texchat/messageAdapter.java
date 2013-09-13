package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		
		messageView = (LinearLayout)layoutinflater.inflate(R.layout.message_view, parent, false);
       
		
		TextView from = new TextView(context);
		messageView.addView(from);
		TextView body = new TextView(context);
		ImageView bodyI = new ImageView(context);
		String fromS = messages.get(position).getFrom().toString();
		if (messages.get(position).getBody() == null || messages.get(position).getBody().equals("")) {
			body.setText("Empty");
			messageView.addView(body);
		} else {
			ArrayList<String> s = splitter.split(messages.get(position).getBody());
			for (String str : s) {
				if (str.contains("$")) {
					bodyI.setImageBitmap(mt.getLocalBitmap(str));
					messageView.addView(bodyI);
				} else {
					body.setText(str);
					messageView.addView(body);
				}
			}
			
		}
		
		from.setText(fromS);
		
		return messageView;
	}

}
