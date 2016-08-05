package dataAccess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BotConfigDAO {

	
	private static BotConfigDAO instance;
	private final  String LOGIN_CREDENTIALS_PATH = "/.telegram-bridge/login.secret";
	private BotConfigDAO(){
		
	}//end BotConfigDAO()
	
	public static BotConfigDAO getInstance(){
		if(BotConfigDAO.instance == null){
			BotConfigDAO.instance = new BotConfigDAO();
		}//end if()
		return BotConfigDAO.instance;
	}//end getInstance()
	
	public String[] getLoginCredentials(){
		
		String[] data = new String[2];
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("user.home") + this.LOGIN_CREDENTIALS_PATH));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
		String c = "";
		

		try {
			for(int i = 0; i < 2 && (c = reader.readLine()) != null; i++){
				
				data[i] = c;
				
			}//end for()
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}//end for()
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return data;
	}//end getLoginCredentials()
	
}//end class
