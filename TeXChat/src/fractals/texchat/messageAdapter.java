package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class messageAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<Message> messages;
	LayoutInflater layoutinflater;
	
	
	
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
		
		if (convertView == null) {
        	messageView = (LinearLayout)layoutinflater.inflate(R.layout.message_view, parent, false);
        } else {
           messageView = (LinearLayout) convertView;
        }
		
		TextView from = (TextView) messageView.findViewById(R.id.fromBox);
		TextView body = (TextView) messageView.findViewById(R.id.bodyBox);
		
		String fromS = messages.get(position).getFrom();
		String bodyS = messages.get(position).getBody();
		
		if (bodyS == null) {
			bodyS = "empty";
		}
		
		from.setText(fromS);
		body.setText(bodyS);
		
		return messageView;
	}

}
