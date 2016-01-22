package database.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.types.User;

public interface INFCRepository {
	ArrayList<User> GetAllUsers() throws Exception;
	User GetMatchingUser(String username) throws Exception;
}
