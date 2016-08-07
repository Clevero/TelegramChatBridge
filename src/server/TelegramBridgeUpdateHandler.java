package server;

import java.util.ArrayList;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.ForwardMessage;
import org.telegram.telegrambots.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.api.methods.send.SendLocation;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.api.methods.send.SendVoice;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.Voice;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


import config.BotConfig;
import fachkonzept.BotCOnfigContainer;
import fachkonzept.NotificationMessageContainer;
import fachkonzept.NumberContainer;

public class TelegramBridgeUpdateHandler extends TelegramLongPollingBot implements BotConfig {

	public TelegramBridgeUpdateHandler() {
		super();
		this.init();
	}//end TelegramBridgeUpdateHandler()
	
	private void init(){
		try {
			BotCOnfigContainer.getInstance().setUsername(getMe().getFirstName());
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//end catch()
	}//end init()
	
	@Override
	public String getBotUsername() {
		return BotCOnfigContainer.getInstance().getBotUsername();
	}//end getBotUsername()

	@Override
	public void onUpdateReceived(Update update) {
		
		// TODO Auto-generated method stub

		if(update.hasMessage()){
			Message message = update.getMessage();
			int status = NumberContainer.getInstance().getStatus(message.getChatId());
			switch(status){
			
			case -2:
				try {
					this.onBlocked(message.getFrom());
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case -1:
				if(!this.onStart(message)){
					//TODO: do some error handling
					NumberContainer.getInstance().addUser(message.getChatId());
				}//end if()
				break;
			default:
				
				//prüfen ob admin
				if(this.isAdmin(message.getChatId())){

					if(message.hasText() && message.getText().equals("/block")){
						this.onBlockUser(message.getReplyToMessage().getForwardFrom());
						//TODO: do some logging
					}//end if()
					else if(message.hasText() && message.getText().equals("/unblock")){
						this.onUnBlockUser(message.getReplyToMessage().getForwardFrom());
						//TODO: do some logging
					}//end else if()
					else if(message.hasText() && message.getText().startsWith("/sendBroadcast") || message.hasText() && message.getText().startsWith("/sb")){
							
							this.sendBroadcast(message.getText().substring(message.getText().indexOf(" ")));
							
							
					}//end else if()
					else if(message.hasText() && message.getText().equals("/list")){
						//wir sollen eine Liste mit den ganzen usern erstellen
						
						ArrayList<Long> numbers = NumberContainer.getInstance().getAllNumbers();
						ArrayList<String> users = new ArrayList<>();
						
						for(int i = 0; i < numbers.size(); i++){
							try {
								Chat chat = getChat(new GetChat().setChatId(numbers.get(i).toString()));
								users.add(this.getSummaryOfUser(chat.getUserName(), chat.getFirstName(), chat.getLastName()));
							} catch (TelegramApiException e) {
								// TODO: do some error handling
								e.printStackTrace();
							}//end catch()
							
						}//end for()
						
						String messageText = "You have '" + users.size() + "' users (incl. you)" + '\n';
						if(users.size() > 1){
							messageText += "Here is a list of your users:" + '\n';
							for(int i = 0; i < users.size(); i++){
								messageText += users.get(i) + '\n';
							}//end for()
						}//end if()
						SendMessage sendMessageRequest = new SendMessage();
						sendMessageRequest.setText(messageText);
						sendMessageRequest.setChatId(message.getChatId().toString());
						try {
							sendMessage(sendMessageRequest);
						} catch (TelegramApiException e) {
							// TODO: do some error handling
							e.printStackTrace();
						}//end catch()
						
					}//end else if()
					else if(message.getText().startsWith("/set")){
						String[] data = message.getText().split(" ");
						
						//data[0]-> /set
						//data[1]-> type
						//data[2-n]-> message
						
						if(data.length < 2){
							SendMessage sendMessagerequest = new SendMessage();
							sendMessagerequest.setText("Invalid arguments. Available types are [start | block | blocked | stop | unblock]");
							sendMessagerequest.setChatId(message.getChatId().toString());
							try {
								sendMessage(sendMessagerequest);
							} catch (TelegramApiException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						String messageText = "";
						
						for(int i = 2; i < data.length; i++){
							
							messageText += data[i];
							if(i < data.length-1){
								messageText +=" ";
							}
							
						}//end for()
						
						SendMessage sendMessagerequest = new SendMessage();
						sendMessagerequest.setChatId(message.getChatId().toString());
						
						if(NotificationMessageContainer.getInstance().setMessage(data[1], messageText)){
							sendMessagerequest.setText("NotificationMessage successfully set [" +  data[1] + "]");
						}
						else{
							sendMessagerequest.setText("Invalid arguments. Available types are [start | block | blocked | stop | unblock]");
						}
						try {
							sendMessage(sendMessagerequest);
						} catch (TelegramApiException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						
						if(message.hasText()){
							
							
							SendMessage sendObjectRequest = new SendMessage();
							sendObjectRequest.enableMarkdown(true);
							sendObjectRequest.setChatId(message.getReplyToMessage().getForwardFrom().getId().toString());
							sendObjectRequest.setText(message.getText());
							try {
								sendMessage(sendObjectRequest);
							} catch (TelegramApiException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						else if(message.hasLocation()){
							SendLocation sendObjectRequest = new SendLocation();
							sendObjectRequest.setChatId(message.getReplyToMessage().getForwardFrom().getId().toString());
							sendObjectRequest.setLatitude(new Float(message.getLocation().getLatitude()));
							try {
								sendLocation(sendObjectRequest);
							} catch (TelegramApiException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else if(message.getPhoto() != null){
							ArrayList<PhotoSize> sizes = (ArrayList<PhotoSize>) message.getPhoto();
							
							SendPhoto sendObjectRequest = new SendPhoto();
							sendObjectRequest.setChatId(message.getReplyToMessage().getForwardFrom().getId().toString());
							sendObjectRequest.setPhoto(sizes.get(0).getFileId());
							
							try {
								sendPhoto(sendObjectRequest);
							} catch (TelegramApiException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}else if(message.getVoice() != null){
							Voice voice = message.getVoice();
							
							SendVoice sendObjectRequest = new SendVoice();
							sendObjectRequest.setChatId(message.getReplyToMessage().getForwardFrom().getId().toString());
							sendObjectRequest.setVoice(voice.getFileId());
							try {
								sendVoice(sendObjectRequest);
							} catch (TelegramApiException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else if(message.getVideo() != null){
							SendVideo sendVideoRequest = new SendVideo();
							sendVideoRequest.setChatId(message.getReplyToMessage().getForwardFrom().getId().toString());
							sendVideoRequest.setVideo(message.getVideo().getFileId());
							try {
								sendVideo(sendVideoRequest);
							} catch (TelegramApiException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						
					}
				}
						//it's not a admin message
						else{
							
								if(message.getText() != null && message.getText().equals("/stop")){
									this.onStop(message);
								}
								else{
								ForwardMessage forwardMessageRequest = new ForwardMessage();
								forwardMessageRequest.setFromChatId(message.getFrom().getId().toString());
								forwardMessageRequest.setChatId(BotCOnfigContainer.getInstance().getAdminId() + "");
								forwardMessageRequest.setMessageId(message.getMessageId());
								try {
									forwardMessage(forwardMessageRequest);
								} catch (TelegramApiException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
						}
						}
								
							}//end else
						
						}
						
						
						
			
		
	

	@Override
	public String getBotToken() {
		return BotCOnfigContainer.getInstance().getToken();
	}//end getBotToken()
	
	private String getSummaryOfUser(String username, String firstName, String lastName){
		StringBuilder summaryBuilder = new StringBuilder();
		
		summaryBuilder.append(firstName);
		if(lastName != null){
			summaryBuilder.append(" " + lastName);
		}//end if()
		
		if(username != null){
			summaryBuilder.append(", aka @" + username);
		}//end if()
		return summaryBuilder.toString();
	}//end getSummaryOfuser()
	
	private boolean onStart(Message message){
		//TODO: nummer zum container addeden
		SendMessage sendMessageRequest = new SendMessage();
		sendMessageRequest.enableMarkdown(true);
		sendMessageRequest.setText(NotificationMessageContainer.getInstance().getMessage(message.getFrom(), "start"));
		sendMessageRequest.setChatId(message.getChatId().toString());
		sendMessageRequest.setReplayToMessageId(message.getMessageId());
		try {
			sendMessage(sendMessageRequest);
		} catch (TelegramApiException e) {
			return false;
		}//end catch()
		NumberContainer.getInstance().addUser(message.getChatId());
		return true;
	}//end onStart()
	
	private boolean onStop(Message message){
		SendMessage sendMessageRequest = new SendMessage();
		sendMessageRequest.enableMarkdown(true);
		sendMessageRequest.setChatId(message.getChatId().toString());
		
		sendMessageRequest.setText(NotificationMessageContainer.getInstance().getMessage(message.getFrom(), "stop"));
		
		try {
			sendMessage(sendMessageRequest);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return NumberContainer.getInstance().remove(message.getChatId());
	}//end onStop()
	
	private void sendBroadcast(String message){
		ArrayList<Long> numbers = NumberContainer.getInstance().getAllNumbers();
		SendMessage sendMessageRequest = new SendMessage();
		sendMessageRequest.enableMarkdown(true);
		sendMessageRequest.setText(message);
		for(int i = 0; i < numbers.size(); i++){
			sendMessageRequest.setChatId(numbers.get(i).toString());
			
			try {
				GetChat test = new GetChat();
				test.setChatId(numbers.get(i).toString());
				Chat chat = getChat(test);
				sendMessageRequest.setText(HelperClass.includeUsername(chat.getFirstName(), message));
				sendMessage(sendMessageRequest);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//end for()
	}//end sendBroadcast()
	
	private void onUnsubscribe(Long id){
		
	}//end onUnscribscribe()
	
	private void onBlockUser(User user){
		NumberContainer.getInstance().blockUser(new Long(user.getId()));
		SendMessage sendMessageRequest = new SendMessage();
		sendMessageRequest.enableMarkdown(true);
		sendMessageRequest.setText(NotificationMessageContainer.getInstance().getMessage(user, "block"));
		sendMessageRequest.setChatId(new Long(user.getId()).toString());
		
		try {
			sendMessage(sendMessageRequest);
		} catch (TelegramApiException e) {
			//TODO: do some error handling
			e.printStackTrace();
		}
	}//end onBlockUser()
	
	private void onUnBlockUser(User user){
		NumberContainer.getInstance().unblockUser(user.getId().longValue());
		SendMessage sendMessageRequest = new SendMessage();
		sendMessageRequest.enableMarkdown(true);
		sendMessageRequest.setChatId(user.getId().toString());
		sendMessageRequest.setText(NotificationMessageContainer.getInstance().getMessage(user, "unblock"));
		
		try {
			sendMessage(sendMessageRequest);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//end onUnblockUser()
	
	private void onBlocked(User user) throws TelegramApiException{
		SendMessage sendMessageRequest = new SendMessage();
		sendMessageRequest.enableMarkdown(true);
		sendMessageRequest.setChatId(user.getId().toString());
		sendMessageRequest.setText(NotificationMessageContainer.getInstance().getMessage(user, "blocked"));
		sendMessage(sendMessageRequest);
	}//end onBlocked()
	
	private boolean isAdmin(Long id){
		return id.equals(BotCOnfigContainer.getInstance().getAdminId());
	}//end isAdmin()

}//end class
