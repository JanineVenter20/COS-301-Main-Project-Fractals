package fractals.texchat;

import java.util.ArrayList;

public class SplitString {

	public ArrayList<String> split(String message) {
		ArrayList<String> ret = new ArrayList<String>();
		
		message = message.trim();
		String s;
		while (!(message.length() == 0)) {
			if (message.charAt(0) == '$') {
				message = message.substring(1);
				if (message.contains("$")) {
					s = "$" + message.substring(0, message.indexOf("$")+1);
					message = message.substring(message.indexOf("$")+1);
				} else { 
					s = message;
					ret.add(s);
					break;
				}
			} else {
				if (message.contains("$")) {
					s = message.substring(0, message.indexOf("$"));
					message = message.substring(message.indexOf("$"));
				} else  {
					s = message;
					ret.add(s);
					break;
				}
			}
			ret.add(s);
		}
		return ret;
	}
	
}
