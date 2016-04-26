package database.types;

public class UsersRole {
	
	private final int _roleID;
	private final int _userID;
	
	private UsersRole(int roleID, int userID) 
	{
		_roleID = roleID;
		_userID = userID;
	}

	public int get_roleID() {
		return _roleID;
	}

	public int get_userID() {
		return _userID;
	}

}
