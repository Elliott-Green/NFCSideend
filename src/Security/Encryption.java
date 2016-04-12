package Security;

import org.mindrot.jbcrypt.BCrypt;


public class Encryption implements IEncryption {

	/* (non-Javadoc)
	 * @see Security.IEncry#compareKeyToHash(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean compareKeyToHash(String key, String hash) 
	{	
			return BCrypt.checkpw(key, hash);				
	}
}
