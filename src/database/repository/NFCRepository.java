package database.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import Security.Logic;
import database.access.DBFactory;
import database.access.IUnitOfWork;
import database.types.Door;
import database.types.Section;
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

			
			//get the first and hopefully only record
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
	
	public void checkPermission(User u)
	{
		/*
		 * attempting factory pattern
		 */
		int roleID = u.get_role();
		switch(roleID)
		{
		case 1: Logic.seniorStaffPermission(); break;
		case 2: Logic.juniorStaffPermission(); break;
		case 3: Logic.studentPermission(); break;
		case 4: Logic.dayStaffPermission(); break;
		case 5: Logic.nightStaffPermission(); break;
			
		}
	}
	
	
	
	public boolean isValidDoorID(int doorID) 
	{
		try
		{
			IUnitOfWork uow = _dbFactory.CreateUnitOfWork();
			ResultSet rs =  uow.RunStatement("SELECT * FROM DOOR WHERE _doorID = " + doorID);
			Door Doors = mapDoors(rs);
			
			/*
			 * WRITE LOGIC TO SEE IF THERE IS NOT A DOOR, IF NOT THEN QUIT.
			 */
		
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
		
	}
	
	

	
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////
	//mappings
	/////////////////////////////////////////////////////////////////////
	
	/*
	 * Maps users into an Arraylist from the ResultSet.
	 * @Calls mapSingleUser
	 */
	private ArrayList<User> mapUsers(ResultSet rs) throws SQLException 
	{
		ArrayList<User> results = new ArrayList<User>();

		while(rs.next())
		{
			results.add(mapUser(rs));
		}

		return results;
	}

	private User mapUser(ResultSet rs) throws SQLException
	{
		
		Integer id = rs.getInt("_userID");
		String un =rs.getString("_username");
		String pw =rs.getString("_password");
		String key =rs.getString("_key");
		String salt = rs.getString("_salt");
		String hash = rs.getString("_hash");
		int role =rs.getInt("_roleID");
		return new User(id,un,pw, key,salt,hash, role);
	}
	
	
	
	///////////////////////////////////////////////////////////////////////
	
	/*
	 * Maps users into an Arraylist from the ResultSet.
	 * @Calls mapSingleUser
	 */
	private ArrayList<Door> mapDoor(ResultSet rs) throws SQLException 
	{
		ArrayList<Door> results = new ArrayList<Door>();

		while(rs.next())
		{
			results.add(mapDoors(rs));
		}

		return results;
	}
	private Door mapDoors(ResultSet rs) throws SQLException
	{
		if(rs.next())
		{
			int id = rs.getInt("_doorID");
			String name =rs.getString("_doorName");
			Time tin =rs.getTime("_doorTimeIn");
			Time tout =rs.getTime("_doorTimeOut");
			int bid =rs.getInt("_buildingID");
			return new Door(id,name,tin,tout,bid);
		}
		return null;

	}

///////////////////////////////////////////////////////////////////////
	private ArrayList<Section> mapSection(ResultSet rs) throws SQLException 
	{
		ArrayList<Section> results = new ArrayList<Section>();

		while(rs.next())
		{
			results.add(mapSections(rs));
		}

		return results;
	}
	private Section mapSections(ResultSet rs) throws SQLException
	{
		if(rs.next())
		{
			
			int sectionID = rs.getInt("_sectionID");
			int roleID = rs.getInt("_roleID");
			int doorID = rs.getInt("_doorID");
	
			return new Section(sectionID,roleID,doorID);
		}
		return null;

	}


	


	}	
