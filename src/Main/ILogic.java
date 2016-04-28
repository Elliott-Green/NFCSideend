package main;

import database.types.User;

public interface ILogic {
	void monitorDoor(int doorID, int keyLength);
	User getUserFromKey(String key) throws Exception;
	boolean userCanAccessDoor(int userID, int doorID) throws Exception;
	public User addUserToSystem(String username, String lastname, String UID, int roleID) throws Exception;
}