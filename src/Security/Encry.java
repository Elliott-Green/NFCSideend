package Security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.swing.DefaultBoundedRangeModel;

import Main.Main;
import database.access.DBFactory;
import database.repository.NFCRepository;
import database.types.User;

public class Encry 
{
	static NFCRepository dbRepo = new NFCRepository("jdbc:mysql://51.255.42.59:3306/NFC" , "jroot"  , "javapassword");

	/*
	 * Read a Salt/Key String and output a SHA2 hashed digest.
	 * @input: String -  SaltedPassword
	 * @output: String - HashedPassword
	 */
	
	public static void chkKeyHash(String key) throws Exception
	{
		//is valid user
		
		boolean a = dbRepo.isValidKey(key);
		if (a == true) 
		{
			User u = dbRepo.GetMatchingUser(key);
			String salt = u.get_salt();
			//debug System.out.println("users salt = " + salt);
			String hash = stringToSHA2(key, salt);
			//debug System.out.println("users hash = " + hash);
			String DBhash = u.get_hash();
			
			if(hash.equals(DBhash))
			{
				System.out.println("Bingo baby");
			}
		}
		
		

		
		
		
		//check door permissions
		//update security tables
		//turn LED on
	}
	public static String stringToSHA2(String key, String salt)
	{
		String hashedKey = null;
		String concatKeySalt = key+salt;
		//debug 
		System.out.println("users concat string = " + concatKeySalt);
		
		 try 
		 {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(concatKeySalt.getBytes("UTF-8"));
			
			byte[] hashedDigest = md.digest();
			BigInteger bigInt = new BigInteger(1, hashedDigest);
			hashedKey = bigInt.toString(16);
	        while ( hashedKey.length() < 32 ) 
	        {
	        	hashedKey = "0"+hashedKey;
	        }
			
			
		 } 
		 catch (NoSuchAlgorithmException | UnsupportedEncodingException e) 
		 {
			e.printStackTrace();
		 }
		return hashedKey;
		 
	}
	
}
