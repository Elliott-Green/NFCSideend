package Security;

import org.mindrot.jbcrypt.BCrypt;


/*
 * This class uses the main functions of bCrypt wrapped in my own Class.
 */
public class Encryption implements IEncryption {
	
	/*
	 * used dynamically over all users to determine if a user can be matched
	 * 
	 * Input : user input key, hashed db records
	 * Output : boolean if user matches db row
	 */
	@Override
	public boolean compareKeyToHash(String key, String hash) 
	{	
			return BCrypt.checkpw(key, hash);				
	}

	/*
	 * Turns a new key into a bCrypt hash. Used within the initation point.
	 * 
	 * Input : UID string.
	 * Output : bCrypt Hash(UID)
	 */
	@Override
	public String convertKeyToHash(String key) 
	{
		return BCrypt.hashpw(key, BCrypt.gensalt(12));
	}
}
