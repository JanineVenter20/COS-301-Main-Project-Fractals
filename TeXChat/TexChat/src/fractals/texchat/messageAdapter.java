package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class messageAdapter extends BaseAdapter {
	
	ArrayList<Message> messages;
	private LayoutInflater layoutinflater;
	Context context;
	
	public messageAdapter(Context mcontext, ArrayList<Message> mess) {
		context = mcontext;
		layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		messages = mess;
		
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int pos) {
		return messages.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		LinearLayout messageView; 
		
		if (view == null)
			messageView = (LinearLayout) layoutinflater.inflate(R.layout.message_view, viewGroup);
		else 
			messageView = (LinearLayout)view;
		
		TextView from = (TextView)messageView.findViewById(R.id.FromBox);
		TextView body = (TextView)messageView.findViewById(R.id.messageView);
		
		String fromS = messages.get(position).getFrom();
		String bodyS = messages.get(position).getBody();
		
		from.setText(fromS);
		body.setText(bodyS);
		
		return messageView;
	}

}
