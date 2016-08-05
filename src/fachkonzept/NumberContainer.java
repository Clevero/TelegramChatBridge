package fachkonzept;

import java.util.ArrayList;

import dataAccess.NumberDao;

public class NumberContainer {

	private static NumberContainer instance;
	private ArrayList<TelegramBridgeUser> numbers;
	
	private NumberContainer(){
		this.loadAll();
	}//end NumberContainer()
	
	public static NumberContainer getInstance(){
		if(NumberContainer.instance == null){
			NumberContainer.instance = new NumberContainer();
		}//end if()
		return NumberContainer.instance;
	}//end getInstance()
	
	private int booleanToInt(boolean foo){
		if(foo){
			return 1;
		}//end if()
		return 0;
	}//end booleanToInt()
	
	public boolean remove(Long id){
		
		for(int i = 0; i < this.numbers.size(); i++){
			if(this.numbers.get(i).id.equals(id)){
				this.numbers.remove(i);
				return this.saveAll();
			}
		}//end for()
		return false;
		
	}//end remove()
	
	private boolean saveAll(){
		
		ArrayList<String[]> toDAO = new ArrayList<>();
		
		for(int i = 0; i < this.numbers.size(); i++){
			
			String[] data = new String[3];
			data[0] = this.numbers.get(i).id.toString();
			data[1] = this.booleanToInt(this.numbers.get(i).subscribed) + ""; 
			data[2] = this.booleanToInt(this.numbers.get(i).blocked) + "";
			toDAO.add(data);
		}//end for()
		//TODO: rückgabewert in aufruf verwerten
		return NumberDao.getInstance().saveAll(toDAO);
	}//end saveAll()
	
	public ArrayList<Long> getAllNumbers(){
		
		ArrayList<Long> nubers = new ArrayList<>();
		
		for(int i = 0; i < this.numbers.size(); i++){
			nubers.add(this.numbers.get(i).id);
		}//end for()
		return nubers;
	}//end getAllNumbers()
	
	private boolean loadAll(){

		ArrayList<String[]> fromDao = NumberDao.getInstance().getAll();
		
		if(fromDao != null){
		
			this.numbers = new ArrayList<>();
			
			for(int i = 0; i < fromDao.size(); i++){
				this.numbers.add(new TelegramBridgeUser(fromDao.get(i)));
			}//end for()
			return true;
		}//end if()
		return false;
		//TODO: rückgabewerte in aufruf verwerten
	}//end loadAll()
	
	/**
	 * Return the current status for the user
	 * @param id
	 * @return "-2" blocked User
	 * 			"-1" doesn't exist in DB
	 * 			"0" User and has subscribed
	 * 			"1" User and has unsubscribed
	 */
	public int getStatus(Long id){
		
		for(int i = 0; i < this.numbers.size(); i++){
			
			//wenn id stimmt
			if(this.numbers.get(i).id.equals(id)){
				
				//wenn nutzer geblockt
				if(this.numbers.get(i).blocked){
					return -2;
				}//end if()
				//wenn unsubscribed
				else if(!this.numbers.get(i).subscribed){
					return 0;
				}//end else if()
				//wenn subscribed
				else if(this.numbers.get(i).subscribed){
					return 1;
				}//end else if()
			}//end if()
			
		}//end for()
		
		//wenn nutzer nicht in db
		return -1;
		
	}//end getStatus()
	
	public void addUser(Long id){
		//TODO: implement
		String[] data = new String[3];
		data[0] = id.toString();
		data[1] = "1";
		data[2] = "0";
		this.numbers.add(new TelegramBridgeUser(data));
		this.saveAll();
	}//end addUser()
	
	public void blockUser(Long id){
		//suchen der id im container, verändern danach in DAO schreiben lassen
		for(int i = 0; i < this.numbers.size(); i++){
			if(this.numbers.get(i).id.equals(id)){
				this.numbers.get(i).blocked = true;
				break;
			}//end if()
		}//end for()
		this.saveAll();
	}//end blockUser()
	
	public boolean unblockUser(Long id){
		
		for(int i = 0; i < this.numbers.size(); i++){
			if(this.numbers.get(i).id.equals(id)){
				this.numbers.get(i).blocked = false;
				this.saveAll();
				return true;
			}//end if()
		}//end for()
		//TODO: rückgabewrt in aufruf verwerten
		return false;
	}//end unblockUser()
	
	
	private class TelegramBridgeUser{
		
		Long id;
		boolean subscribed;
		boolean blocked;
		
		public TelegramBridgeUser(String[] data){
			this.id = Long.parseLong(data[0]);
			this.subscribed = Boolean.parseBoolean(data[1]);
			this.blocked = this.intoToBoolean(Integer.parseInt(data[2]));
		}//end TelegramBridgeUser()
		
		private boolean intoToBoolean(int number){
			if(number == 1){
				return true;
			}//end if()
			return false;
		}//end intToBoolean()
		
		
	}//end private class
	
	
}//end class
