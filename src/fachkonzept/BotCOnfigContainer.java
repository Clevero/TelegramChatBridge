package fachkonzept;

import javax.management.RuntimeErrorException;

import dataAccess.BotConfigDAO;

public class BotCOnfigContainer {

	private static BotCOnfigContainer instance;
	private String BOT_TOKEN;
	private String BOT_USERNAME;
	private Long ADMIN_ID;
	
	private BotCOnfigContainer(){
		this.init();
	}//end BotConfigContainer()
	
	public static BotCOnfigContainer getInstance(){
		if(BotCOnfigContainer.instance == null){
			BotCOnfigContainer.instance = new BotCOnfigContainer();
		}//end if()
		return BotCOnfigContainer.instance;
	}//end getInstance()
	
	private void init(){
		String[] data = BotConfigDAO.getInstance().getLoginCredentials();
		if(data != null){
			this.ADMIN_ID = new Long(data[0]);
			this.BOT_TOKEN = data[1];
			return;
		}//end if()
		throw new RuntimeErrorException(null, "No TOKEN and ADMIN_ID found");
	}//end init()
	
	public String getToken(){
		return this.BOT_TOKEN;
	}//end getToken()
	
	public String getBotUsername(){
		return this.BOT_USERNAME;
	}//end getBotUsername()
	
	public Long getAdminId(){
		return this.ADMIN_ID;
	}//end getAdminId()
	
	public void setUsername(String username){
		this.BOT_USERNAME = username;
	}//end setUsername()
	
}//end class
