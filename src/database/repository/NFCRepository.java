package database.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import database.access.DBFactory;
import database.access.IUnitOfWork;
import database.types.Door;
import database.types.User;


public class NFCRepository implements INFCRepository{

	private final DBFactory _dbFactory;

	public NFCRepository(String connectionString, String username, String password)
	{
		_dbFactory = new DBFactory(connectionString, username, password);
	}	


	@Override
	public ArrayList<User> GetAllUsers() throws Exception 
	{
		//try and get a result set of all the users
		IUnitOfWork uow = null;
		try
		{
			uow = _dbFactory.CreateUnitOfWork();

			ResultSet rs =   uow.RunStatement("SELECT * FROM USERS");
			return mapUsers(rs);

		}
		catch(ClassNotFoundException ex)
		{
			throw new Exception("Unable to query database", ex);
		}	
		finally
		{
			uow.Done();
		}
	}

	@Override
	public User GetMatchingUser(String key) throws Exception 
	{
		//try and get a result set of all the users
		try
		{
			IUnitOfWork uow = _dbFactory.CreateUnitOfWork();

			ResultSet rs =  uow.RunStatement("SELECT * FROM USERS where _key LIKE \"%"+ key +"%\"");

			ArrayList<User> users = mapUsers(rs);

			if (users.isEmpty()) throw new Exception("User not found");
			return users.get(0);
		}
		catch(ClassNotFoundException ex)
		{
			throw new Exception("Unable to query database", ex);
		}

	}

	@Override
	public boolean isValidKey(String key) throws Exception 
	{
		//try and get a result set of all the users
		try
		{
			IUnitOfWork uow = _dbFactory.CreateUnitOfWork();

			ResultSet rs =  uow.RunStatement("SELECT * FROM USERS where _key LIKE \"%"+ key +"%\"");
			ArrayList<User> users = mapUsers(rs);

			if(key.equals(users.get(0).get_key()))
			{
				System.out.println("Welcome " + users.get(0).get_username() +" "+ users.get(0).get_password());
				//log door entry
				return true;
			}
			else
			{

			}
		}
		catch(ClassNotFoundException ex)
		{
			throw new Exception("Unable to query database", ex);
		}

		return false;


	}


	/*
	 * Maps users into an Arraylist from the ResultSet.
	 * @Calls mapSingleUser
	 */
	private ArrayList<User> mapUsers(ResultSet rs) throws SQLException 
	{
		ArrayList<User> results = new ArrayList<User>();

		while(rs.next())
		{
			results.add(mapSingleUser(rs));
		}

		return results;
	}

	private User mapSingleUser(ResultSet rs) throws SQLException
	{
		
		Integer id = rs.getInt("_userID");
		String un =rs.getString("_username");
		String pw =rs.getString("_password");
		String key =rs.getString("_key");
		String salt = rs.getString("_salt");
		String hash = rs.getString("_hash");
		String role =rs.getString("_roleID");
		return new User(id,un,pw, key,salt,hash, role);
	}
	
	
	private Door mapDoors(ResultSet rs) throws SQLException
	{
		if(rs.next()){
			int id = rs.getInt("_doorID");
			String name =rs.getString("_doorName");
			Time tin =rs.getTime("_doorTimeIn");
			Time tout =rs.getTime("_doorTimeOut");
			int bid =rs.getInt("_buildingID");
			return new Door(id,name,tin,tout,bid);
		}
		return null;

	}
	
	


	@Override
	public User getKeys() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	//write a method you have key -> you need the salt

	public String getUsersSalt(String key)
	{
		//try and get a result set of all the users
		try
		{
			IUnitOfWork uow = _dbFactory.CreateUnitOfWork();
			ResultSet rs =  uow.RunStatement("SELECT * FROM USERS where _key LIKE \"%"+ key +"%\"");
			ArrayList<User> users = mapUsers(rs);
			String salt = users.get(0).get_salt();
			return salt;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
			return null;
	}
	
	public String getUsersHash(String key)
	{
		//try and get a result set of all the users
		try
		{
			IUnitOfWork uow = _dbFactory.CreateUnitOfWork();
			ResultSet rs =  uow.RunStatement("SELECT * FROM USERS where _key LIKE \"%"+ key +"%\"");
			ArrayList<User> users = mapUsers(rs);
			String salt = users.get(0).get_salt();
			return salt;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
			return null;
	}
	
	
	public void getDoorTime(int doorID)
	{
		//try and get a result set of all the users
		try
		{
			IUnitOfWork uow = _dbFactory.CreateUnitOfWork();
			ResultSet rs =  uow.RunStatement("SELECT * FROM DOOR where _doorID = " + doorID);
			Door Doors = mapDoors(rs);
			Time in = Doors.get_doorTimeIn();
			Time out = Doors.get_doorTimeOut();
			
			System.out.println("THE test of door time in was " + in );
			System.out.println("THE test of door time out was " + out );
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
	}


	


	}	
