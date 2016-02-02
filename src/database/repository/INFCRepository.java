package database.repository;


import java.util.ArrayList;
import database.types.User;

public interface INFCRepository {
	ArrayList<User> GetAllUsers() throws Exception;
	User GetMatchingUser(String username) throws Exception;
	User getKeys() throws Exception;
	boolean isValidKey(String key) throws Exception;

}
