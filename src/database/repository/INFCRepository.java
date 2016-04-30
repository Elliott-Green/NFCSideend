package database.repository;


import java.util.ArrayList;

import database.types.User;
import database.types.UserDoorAccess;

public interface INFCRepository {
	ArrayList<User> GetAllUsers() throws Exception;
	void logUserAccess(int userID, int doorID) throws Exception;
	ArrayList<UserDoorAccess> getUserDoorAccess(int userID,int doorID) throws Exception;
	void createNewUser(String username, String lastname, String hashedKey) throws Exception;
	void createNewUserRole(int userID, int roleID) throws Exception;
	User getUserFromHash(String hashedKey) throws Exception;

}
