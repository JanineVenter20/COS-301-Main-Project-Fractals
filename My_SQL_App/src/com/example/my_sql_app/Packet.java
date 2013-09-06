package com.example.my_sql_app;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Packet {
	
		//PRIVATE MEMBERS
		private String user;
		private String message;
		private Boolean sent_received;
		private int uniqueKey;
		private static int counter = 0;
	
		//METHODS
		public Packet(String u, String m, Boolean s_r)
		{
			this.user = u;
			this.message = m;
			this.sent_received = s_r;
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
		
		public String getMessage()
		{
			return this.message;
		}
		
		public Boolean getSent_Received()
		{
			return this.sent_received;
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
