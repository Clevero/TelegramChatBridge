package server;

public class HelperClass {

	public static String includeUsername(String username, String message){
		message = message.replaceAll("!user", username);
		return message;
	}//end includeUsername()
	
}//end class
