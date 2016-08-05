package dataAccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class NotificationMessageDAO {

	private static NotificationMessageDAO instance;
	private final String MESSAGE_PATH = "/.telegram-bridge/";
	
	private NotificationMessageDAO(){
		
	}//end NotificationMessageDAO()
	
	public static NotificationMessageDAO getInstance(){
		if(NotificationMessageDAO.instance == null){
			NotificationMessageDAO.instance = new NotificationMessageDAO();
		}//end if()
		return NotificationMessageDAO.instance;
	}//end getInstance()
	
	public String getMessage(String type){
		
		//versuchen die datei zu öffnen
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("user.home") + this.MESSAGE_PATH + type + ".message"));
		} catch (FileNotFoundException e) {
			//TODO: do some error handling
			e.printStackTrace();
			return null;
		}
		
		String c = "";
		String output = "";
		try {
			while((c = reader.readLine()) != null){
				output += c;
			}//end while()
		} catch (IOException e) {
			//TODO: do some error handling
			e.printStackTrace();
			return null;
		}//end while()
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
		
	}//end getMessage()
	
	public boolean saveMessage(String type, String message){
		BufferedWriter writer;
		
		try {
			writer = new BufferedWriter(new FileWriter(type + ".message"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		try {
			writer.write(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		try {
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
		
	}//end saveMessage()
	
}//end class
