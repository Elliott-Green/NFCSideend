package Security;

import database.repository.NFCRepository;
import database.types.User;

public class Normal 
{
	/*
	 * go through all cards, till match
	 * 
	 */
	public boolean noSecurityCheck(String key) throws Exception
	{
		NFCRepository dbRepo = new NFCRepository("jdbc:mysql://51.255.42.59:3306/NFC" , "jroot"  , "javapassword");
		boolean b = dbRepo.isValidKey(key);
		
		if(b == true) return true;
		else return false;
	}
	
	
}
