package fractals.texchat;

import java.util.ArrayList;

public class SplitString {

	public ArrayList<String> split(String message) {
		ArrayList<String> ret = new ArrayList<String>();
		
		message = message.trim();
		String s;
		while (!(message.length() == 0)) {
			if (message.startsWith("$$")) {
				message = message.substring(2);
				if (message.contains("$$")) {
					s = "$$" + message.substring(0, message.indexOf("$$")+2);
					message = message.substring(message.indexOf("$$")+2);
				} else { 
					s = message;
					ret.add(s);
					break;
				}
			} else {
				if (message.contains("$$")) {
					s = message.substring(0, message.indexOf("$$"));
					message = message.substring(message.indexOf("$$"));
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
