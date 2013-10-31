package fractals.texchat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Packet {
	
	//PRIVATE MEMBERS
		private String user;
		private String contact;
		private String message;
		private boolean sent;
		private Boolean sent_received;
		private int uniqueKey;
		private static int counter = 0;
	
	//METHODS
		public Packet(String user, String contact, String message, Boolean sent, Boolean sent_rec)
		{
			this.user = user;
			this.contact = contact;
			this.message = message;
			this.sent = sent;
			this.sent_received = sent_rec;
			this.uniqueKey = AssignUniqueKey();
		}
		
		private int AssignUniqueKey()
		{
			counter = counter + 1;
			return this.uniqueKey = counter;
		}
		
		public String getUser()
		{
			return this.user;
		}
		
		public String getContact() 
		{
			return this.contact;
		}
		
		public String getMessage()
		{
			return this.message;
		}
		
		public Boolean getSent_Received()
		{
			return this.sent_received;
		}
		
		public Boolean getSent () {
			return this.sent;
		}
		
		public String getTimeStamp()
		{
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
			String formatDate = sdf.format(date);
			return formatDate;
		}
		
		public Integer getUniqueKey()
		{
			return this.uniqueKey;
		}
}
