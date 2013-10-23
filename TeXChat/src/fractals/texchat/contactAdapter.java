package fractals.texchat;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		LinearLayout itemView = null;
		itemView = (LinearLayout)layoutInflater.inflate(R.layout.contactlayout, parent, false);
		itemView.setBackgroundResource(R.drawable.user);
		if (entries.get(position) != null) {
			String contactS = entries.get(position).getName();
	        final String userS = entries.get(position).getUser();
	        Presence p = MainActivity.roster.getPresence(userS);
	        
	        String statusS;
	        
	        if (contactS == null || contactS.equals("")) 
	        	contactS = userS;
	        if (p.toString().contains("unavailable")) {
	        	statusS = p.toString();
	        	itemView.setBackgroundResource(R.drawable.user);
	        }
	        else {
	        	statusS = p.toString();
	        	itemView.setBackgroundResource(R.drawable.user_green);
	        }
	        
	        final String namae = contactS;
	        //itemView.setBackgroundColor(Color.rgb(182, 230, 240));
	        //itemView.setPadding(5, 5, 5, 5);
	        try {
	        if (entries.get(position).getType().toString().equals("none") ||
	        		entries.get(position).getType().toString().equals("from")) {
	        	LinearLayout ll = new LinearLayout(context);
	        	TextView name = new TextView(context);
	        	name.setText(contactS);
	        	itemView.addView(name);
	        	Button b = new Button(context);
	        	b.setText("Accept");
	        	b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Presence p = new Presence(Type.subscribe);
						p.setTo(userS);
						MainActivity.conn.sendPacket(p);
					}
				});
	        	
	        	ll.addView(b);
	        	Button b2 = new Button(context);
	        	b2.setText("Reject");
	        	b2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							MainActivity.roster.removeEntry(entries.get(position));
						} catch (XMPPException e) {
							System.out.println(e.getMessage());
							e.printStackTrace();
						}
					}
				});
	        	ll.addView(b2);
	        	itemView.addView(ll);
	        	return itemView;
	        	
	        } else {
	        	
	        TextView contact = new TextView(context);
	        TextView status = new TextView(context);
	         
	        contact.setText(contactS);
	        status.setText(statusS);
	        itemView.addView(contact);
	        
	        itemView.addView(status);
	        
	       
	        
	        itemView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent chatIntent = new Intent(context, ChatActivity.class);
					chatIntent.putExtra("name", namae);
					chatIntent.putExtra("contact", userS);
					MainActivity.activeChat = MainActivity.cm.createChat(userS, MainActivity.ml);
					context.startActivity(chatIntent);
				}
			});
	        
	        if (entries.get(position).getType().toString().equals("to")) {
	        	status.setText("(request pending)");
	        	status.setGravity(Gravity.CENTER);
	        }
        }
		}catch(IndexOutOfBoundsException e){}
        }
		return itemView;
	}

}
