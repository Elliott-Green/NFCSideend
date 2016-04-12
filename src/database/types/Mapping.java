package database.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class Mapping {
	/*
	 * Maps users into an Arraylist from the ResultSet.
	 * @Calls mapSingleUser
	 */
	public static ArrayList<User> mapUsers(ResultSet rs) throws SQLException 
	{
		ArrayList<User> results = new ArrayList<User>();

		while(rs.next())
		{
			results.add(mapSingleUser(rs));
		}

		return results;
	}

	public static User mapSingleUser(ResultSet rs) throws SQLException
	{
		
		Integer id = rs.getInt("_userID");
		String un =rs.getString("_username");
		String pw =rs.getString("_password");
		String key =rs.getString("_key");
		return new User(id,un,pw, key);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////
	
	/*
	 * Maps users into an Arraylist from the ResultSet.
	 * @Calls mapSingleUser
	 */
	public static ArrayList<Door> mapDoors(ResultSet rs) throws SQLException 
	{
		ArrayList<Door> results = new ArrayList<Door>();

		while(rs.next())
		{
			results.add(mapSingleDoor(rs));
		}

		return results;
	}
	
	public static Door mapSingleDoor(ResultSet rs) throws SQLException
	{
		if(rs.next())
		{
			int id = rs.getInt("_doorID");
			String name =rs.getString("_doorName");
			int bid =rs.getInt("_buildingID");
			return new Door(id,name,bid);
		}
		return null;

	}

	
	/*
	 * Maps users into an Arraylist from the ResultSet.
	 * @Calls mapSingleUser
	 */
	public static ArrayList<UserDoorAccess> mapUserDoorAccess(ResultSet rs) throws SQLException 
	{
		ArrayList<UserDoorAccess> results = new ArrayList<>();

		while(rs.next())
		{
			results.add(mapSingleUserDoorAccess(rs));
		}

		return results;
	}

	public static UserDoorAccess mapSingleUserDoorAccess(ResultSet rs) throws SQLException
	{
		Time startTime = rs.getTime("_startTime");
		Time endTime =rs.getTime("_endTime");
		int userID =rs.getInt("_userID");
		int doorID =rs.getInt("_doorID");
		return new UserDoorAccess(startTime, endTime, userID, doorID);
	}
	
}
