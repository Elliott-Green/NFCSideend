package database.repository;


import java.util.ArrayList;

import database.types.User;
import database.types.UserDoorAccess;

public interface INFCRepository {
	ArrayList<User> GetAllUsers() throws Exception;
	void logUserAccess(int userID, int doorID) throws Exception;
	ArrayList<UserDoorAccess> getUserDoorAccess(int userID,int doorID) throws Exception;
}
