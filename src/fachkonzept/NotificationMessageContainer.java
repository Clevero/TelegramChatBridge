package fachkonzept;

import java.util.ArrayList;

import org.telegram.telegrambots.api.objects.User;

import dataAccess.NotificationMessageDAO;
import server.HelperClass;

public class NotificationMessageContainer {

	private static NotificationMessageContainer instance;
	private ArrayList<NotificationMessage> messages;
	
	private NotificationMessageContainer(){
		this.messages = new ArrayList<>();
	}//end NotificationMessageontainer()
	
	public static NotificationMessageContainer getInstance(){
		if(NotificationMessageContainer.instance == null){
			NotificationMessageContainer.instance = new NotificationMessageContainer();
		}//end if()
		return NotificationMessageContainer.instance;
	}//end getInstance()
	
	public String getMessage(User user, String type){

		String messageFromContainer = this.getMessageByType(type);
		if(messageFromContainer != null){
			return this.parseToEndString(user, this.getMessageByType(type));
		}//end if()
		
			String message = NotificationMessageDAO.getInstance().getMessage(type);
			if(message != null){
				NotificationMessage newNotificationMessage = new NotificationMessage(type, message);
				this.messages.add(newNotificationMessage);
				return this.parseToEndString(user, newNotificationMessage.message);
			}//end if()
			return null;
	}//end getMessage()
	
	private String getMessageByType(String type){
		for(int i = 0; i < this.messages.size(); i++){
			if(this.messages.get(i).type.equals(type)){
				return this.messages.get(i).message;
			}//end if()
		}//end for()
		return null;
	}//end getmessageByType()
	
	public static String parseToEndString(User user, String message){
		message = HelperClass.includeUsername(user.getFirstName(), message);
		return message;
	}//end parseToEndString()
	
	private void remove(String type){
		for(int i = 0; i < this.messages.size(); i++){
			if(this.messages.get(i).type.equals(type)){
				this.messages.remove(i);
			}//end if()
		}//end for()
	}//end remove()
	
	public boolean setMessage(String type, String message){
		if(NotificationMessageDAO.getInstance().saveMessage(type, message)){
			this.remove(type);
			this.messages.add(new NotificationMessage(type, message));
			return true;
		}//end if()
		return false;
	}//end setMessage()
	
	private class NotificationMessage{
		
		private String type;
		private String message;
		
		public NotificationMessage(String type, String message){
			this.message = message;
			this.type = type;
		}//end NotificationMessage()
		
	}//end private class
	
}//end class
