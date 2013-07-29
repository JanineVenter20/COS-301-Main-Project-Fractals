package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterEntry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.InputFilter.LengthFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class contactAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater layoutInflater;
	private ArrayList<RosterEntry> entries = new ArrayList<RosterEntry>(); 
	
	public contactAdapter(Context mContext, ArrayList<RosterEntry> list) {
		context = mContext;
		layoutInflater = (LayoutInflater) (mContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		entries = list;
	}
	
	@Override
	public int getCount() {
		return entries.size();
	}

	@Override
	public Object getItem(int position) {
		return entries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout itemView;
        if (convertView == null) {
        	itemView = (LinearLayout)layoutInflater.inflate(R.layout.contactlayout, parent, false);
        } else {
           itemView = (LinearLayout) convertView;
        }
        TextView contact = (TextView) itemView.findViewById(R.id.contactView);  
        TextView status = (TextView) itemView.findViewById(R.id.statusView);
               
        String contactS = entries.get(position).getName().toString();
        final String userS = entries.get(position).getUser().toString();
        
        contact.setText(contactS);
        status.setText(userS);

        itemView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putString("contact", userS);
				Intent chatIntent = new Intent(context, ChatActivity.class);
				chatIntent.putExtras(b);
				context.startActivity(chatIntent);
			}
		});
        
        return itemView;
	}

}
