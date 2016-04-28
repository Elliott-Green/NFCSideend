package Security;

import org.mindrot.jbcrypt.BCrypt;


public class Encryption implements IEncryption {

	
	@Override
	public boolean compareKeyToHash(String key, String hash) 
	{	
			return BCrypt.checkpw(key, hash);				
	}

	@Override
	public String convertKeyToHash(String key) 
	{
		return BCrypt.hashpw(key, BCrypt.gensalt(12));
	}
}
