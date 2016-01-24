package database.types;

public class User {

	private final int _userID;
	private final String _username;
	private final String _password;
	private final String _key;
	private final String _role;
	
	public User(int userID, String username, String password, String key, String role){
		_userID = userID;
		_username = username;
		_password = password;
		_key = key;	
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

	public String get_role() {
		return _role;
	}
	

		
}