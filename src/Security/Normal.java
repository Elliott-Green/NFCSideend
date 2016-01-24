package Security;

import database.repository.NFCRepository;

public class Normal 
{
	/*
	 * go through all cards, till match
	 * 
	 */
	public void noSecurityCheck(String key) throws Exception
	{
		NFCRepository dbRepo = new NFCRepository("jdbc:mysql://51.255.42.59:3306/NFC" , "jroot"  , "javapassword");

		System.out.println(dbRepo.isValidKey(key));
		
		
	
	}
	
	
}
