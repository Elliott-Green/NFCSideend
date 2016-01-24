package database.repository;


import java.util.ArrayList;

import database.types.Key;
import database.types.User;

public interface INFCRepository {
	ArrayList<User> GetAllUsers() throws Exception;
	User GetMatchingUser(String username) throws Exception;
	User getKeys() throws Exception;
	Boolean isValidKey(String key) throws Exception;

}
