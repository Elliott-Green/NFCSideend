package database.types;

public class User {

	private final int _userID;
	private final int _keyID;
	private final String _key;
	
	public User(int userID, int keyID, String key){
		_userID = userID;
		_keyID = keyID;
		_key = key;		
	}
	
	public int getUserID(){
		return _userID;
	}
	
	public int getKeyID(){
		return _keyID;
	}

	public String getKey() {
		return _key;
	}
		
}
