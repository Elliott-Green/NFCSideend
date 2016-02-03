package database.types;

public class User {

	private final int _userID;
	private final String _username;
	private final String _password;
	private final String _key;
	private final String _salt;
	private final String _hash;
	private final int _role;
	
	public User(int userID, String username, String password, String key, String salt,
				String hash, int role)
	{
		_userID = userID;
		_username = username;
		_password = password;
		_key = key;	
		_salt = salt;
		_hash = hash;
		_role = role;
	}

	public int get_userID() {
		return _userID;
	}

	public String get_username() {
		return _username;
	}

	public String get_password() {
		return _password;
	}

	public String get_key() {
		return _key;
	}
	
	public String get_salt() {
		return _salt;
	}
	
	public String get_hash() {
		return _hash;
	}

	public int get_role() {
		return _role;
	}
	
	

		
}
