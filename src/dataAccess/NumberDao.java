package dataAccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NumberDao {

	private static NumberDao instance;
	private BufferedWriter writer;
	private BufferedReader reader;
	private final String DELIMITER = ";";
	private final String NUMBER_PATH = "/.telegram-bridge/numbers.txt";
	
	private NumberDao(){
		
	}//end NumberDao()
	
	public static NumberDao getInstance(){
		if(NumberDao.instance == null){
			NumberDao.instance = new NumberDao();
		}//end if()
		return NumberDao.instance;
	}//end getInstance()
	
	public ArrayList<String[]> getAll(){
		this.openReader();
		
		ArrayList<String[]> numbers = new ArrayList<>();
		
		String c;
		
		try {
			while((c = this.reader.readLine()) != null){
				String[] data = c.split(this.DELIMITER);
				numbers.add(data);
				
			}//end while()
		} catch (IOException e) {
			//TODO: do some error handling
			return null;
		}//end catch()
		
		this.closeReader();
		return numbers;
	}//end getAll()
	
	/**
	 * Saves all Numbers to disk
	 * @param numbers
	 * @throws IOException 
	 */
	public boolean saveAll(ArrayList<String[]> numbers){
		
		/*
		 * {number};{subscribed};{blocked}
		 */
		
		if(this.openWriter()){
		
			for(int i = 0; i < numbers.size(); i++){
				
				try {
					this.writer.write(numbers.get(i)[0] + ";" + numbers.get(i)[1] + ";" + numbers.get(i)[2]);
					this.writer.newLine();
				} catch (IOException e) {
					//TODO: do some error handling
					return false;
				}//end catch()
				
			}//end for()
			this.closeWriter();
			return true;
			
		}//end if()
		return false;
		
		
	}//end saveAll()
	
	private boolean openReader(){
		try {
			this.reader = new BufferedReader(new FileReader(System.getProperty("user.home") + this.NUMBER_PATH));
		} catch (FileNotFoundException e) {
			try {
				new File(System.getProperty("user.home") + this.NUMBER_PATH).createNewFile();
				return this.openReader();
			} catch (IOException e1) {
				return false;
			}//end catch()
		}//end catch()
		return true;
	}//end openReader()
	
	private boolean closeReader(){
		try {
			this.reader.close();
		} catch (IOException e) {
			//TODO: do some error handling
			return false;
		}//end catch()
		return true;
	}//end closeReader()
	
	private boolean openWriter(){
		try {
			this.writer = new BufferedWriter(new FileWriter("numbers.txt"));
		} catch (IOException e) {
			//TODO: so some error handling
			return false;
		}//end catch()
		return true;
	}//end openWriter()
	
	private boolean closeWriter(){
		try {
			this.writer.close();
		} catch (IOException e) {
			//TODO: do some error handling
			return false;
		}//end catch()
		return true;
	}//end closeWriter()
	
}//end class
