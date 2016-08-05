package server;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

public class Bridge {

	public static void main(String[] args) throws TelegramApiException{
		TelegramBotsApi api = new TelegramBotsApi();
		api.registerBot(new TelegramBridgeUpdateHandler());
		
	}//end main()
	
}//end class
