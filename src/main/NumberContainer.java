package main;

import java.util.ArrayList;

/**
 * This class is responsible for holding all numbers and their status in memory
 * @author Marcel Caspar
 *
 */
public class NumberContainer {

	private static NumberContainer instance;
	private ArrayList<String[]> numbers;
	
	private NumberContainer(){
		this.numbers = new ArrayList<>();
	}//end NumberContainer()
	
	public static NumberContainer getInstance(){
		if(NumberContainer.instance == null){
			NumberContainer.instance = new NumberContainer();
		}//end if()
		return NumberContainer.instance;
	}//end getInstance()
	
	private synchronized void loadAll(){
		
	}//end loadAll()
	
	private synchronized void saveAll(){
		
	}//end saveAll()
	
	/**
	 * Returns the current status for the userId
	 * @param id UserId
	 * @return -1 if (user doesn't exist in database), 0 (normal) and 1 (blocked)
//	 */
//	public int getStatus(long id){
//		
//	}//end getStatus()
	
	/**
	 * Sets the status of the user to "blocked"
	 * @param id
	 */
	public void blockUser(long id){
		
	}//end blockUser()
	
	/**
	 * Resets the status of the user to "normal"
	 * @param id UserId
	 * @return Wether the status of the user could be resetted or not
	 */
//	public boolean unblockUser(long id){
//		
//	}//end unblockUser()
	
}//end class
