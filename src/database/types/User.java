package database.types;

public class User {

	private final int _userID;
	private final String _firstName;
	private final String _lastName;
	private final String _key;
	
	public User(int userID, String firstName, String lastName, String key)
	{
		_userID = userID;
		_firstName = firstName;
		_lastName = lastName;
		_key = key;	
	}

	public int get_userID() {
		return _userID;
	}

	public String get_firstName() {
		return _firstName;
	}

	public String get_lastName() {
		return _lastName;
	}

	public String get_key() {
		return _key;
	}
}
