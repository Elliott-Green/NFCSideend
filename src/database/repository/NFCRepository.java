package database.repository;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import database.access.DBFactory;
import database.access.DBParam;
import database.access.IUnitOfWork;
import database.types.Mapping;
import database.types.User;
import database.types.UserDoorAccess;

/*
 * This class is created in Main.main and its constructor takes the parameters needed for creating a connection, and passes it to the new DBFactory object.
 * The object can then be used to make relevant queries which would be returned by this class. Effectively making queries calls on the iow (Connection) is considered best OO practice.
 */
public class NFCRepository implements INFCRepository{

	private final DBFactory _dbFactory;	

	public NFCRepository(String connectionString, String username, String password)
	{
		_dbFactory = new DBFactory(connectionString, username, password);
	}	



	/*
	 * A SELECT * method from the database. Used within the Parallel Stream test to test if a users hash can be matched on any user on the database.
	 * 
	 * Output: an ArrayList of type user with all the users on the database.
	 */
	@Override
	public ArrayList<User> GetAllUsers() throws Exception 
	{
		IUnitOfWork uow = null;
		
		try
		{
			uow = _dbFactory.CreateUnitOfWork();
			ResultSet rs =   uow.RunStatement("SELECT * FROM USERS");
			return Mapping.mapUsers(rs);
		}
		catch(ClassNotFoundException ex)
		{
			throw new Exception("Unable to query database", ex);
		}	
		finally
		{
			if(uow != null)uow.Done();
		}
	}
	
	/*
	 * This method is used within Logic to determine if a users hash can be dehashed to a value on the database.
	 * 
	 * Input : hashed UID
	 * Output: User object
	 */
	@Override
	public User getUserFromHash(String hashedKey) throws Exception 
	{
		IUnitOfWork uow = null;
		
		try
		{
			uow = _dbFactory.CreateUnitOfWork();
			ResultSet rs =   uow.RunStatement("SELECT * FROM USERS WHERE _key LIKE " + hashedKey);
			return Mapping.mapSingleUser(rs);
		}
		catch(ClassNotFoundException ex)
		{
			throw new Exception("Unable to query database", ex);
		}	
		finally
		{
			if(uow != null)uow.Done();
		}
	}

	/*
	 * This method calls a SP on the database which inputs 4 parameters in the 'NFC.DETAILS' table 
	 * (userID, doorID, DATABASE.time, DATABASE.date)
	 * 
	 * Void : inserts a row into database table
	 */
	@Override
	public void logUserAccess(int userID, int doorID) throws Exception 
	{
		HashMap<String, DBParam> params = new HashMap<>();
		params.put("userID" , new DBParam(String.valueOf(userID), JDBCType.INTEGER));
		params.put("doorID" , new DBParam(String.valueOf(doorID), JDBCType.INTEGER));
		IUnitOfWork uow = null;
		
		try
		{
			uow = _dbFactory.CreateUnitOfWork();	
			uow.RunStoredProcedure("sp_NFC_insertDetails", params);
		} 
		catch(ClassNotFoundException ex)
		{
			throw new Exception("Unable to query database log", ex);
		}	
		finally
		{
			if(uow != null)uow.Done();
		}
	}
	
	/*
	 * This method adds a new user using the system. It gets called in Logic.addUserToSystem and gets called with the method below
	 * 
	 * Input : user variables and hashed UID
	 * Void : adds new user
	 * 
	 */
	
	@Override
	public void createNewUser(String username, String lastname, String hashedKey) throws Exception 
	{
		IUnitOfWork uow = null;
		
		try
		{
			uow = _dbFactory.CreateUnitOfWork();	
			uow.runAddUserStoredProcedure(username,lastname,hashedKey);
		} 
		catch(ClassNotFoundException ex)
		{
			throw new Exception("Unable to query database log (initiation)", ex);
		}	
		finally
		{
			if(uow != null)uow.Done();
		}
		
	}
	
	/*
	 * This below creates a new users role, based on the user just created by the method above.
	 * This method needs to be called in order to give permissions to a user.
	 * 
	 * Input : userID from just newly created user, and their role ID.
	 * 
	 */
	@Override
	public void createNewUserRole(int userID, int roleID) throws Exception 
	{
		HashMap<String, DBParam> params = new HashMap<>();
		params.put("roleID" , new DBParam(String.valueOf(roleID), JDBCType.INTEGER));
		params.put("userID" , new DBParam(String.valueOf(userID), JDBCType.INTEGER));
		IUnitOfWork uow = null;
		
		try
		{
			uow = _dbFactory.CreateUnitOfWork();	
			uow.RunStoredProcedure("sp_NFC_insertUsersRole", params);
		} 
		catch(Exception ex)
		{
			throw new Exception("Unable to query database log", ex);
		}	
		finally
		{
			if(uow != null)uow.Done();
		}
	}
	
	

	/*
	 * See dissertation(Database queries)
	 * 
	 * This method finds users permissions on a certain door using a SELECT statement.
	 * The query gets all permissions on current users role (Database logic) and also checks if an office is being used (union).
	 * 
	 * Input: userID, doorID
	 * Output : An ArrayList of permission type containing two dates
	 * 		  : Empty array if no permissions
	 */
	@Override
	public ArrayList<UserDoorAccess> getUserDoorAccess(int userID, int doorID) throws Exception {
		
		IUnitOfWork uow = null;
		try
		{
			uow = _dbFactory.CreateUnitOfWork();
			String sql = "SELECT  AP._startTime, "+
						"AP._endTime, "+
						"U._userID, "+
				        "D._doorID "+
						"FROM USERS AS U "+
						"INNER JOIN USERSROLE AS UR "+
						"ON U._userID = UR._userID "+
						"INNER JOIN ROLE AS R "+
						"ON R._roleID = UR._roleID "+
						"INNER JOIN ROLEDOORGROUP AS RDG "+
						"ON RDG._roleID = R._roleID  "+
						"INNER JOIN DOORGROUP AS DG "+
						"ON DG._doorGroupID = RDG._doorGroupID "+
						"INNER JOIN DOORGROUPDOOR AS DGD "+
						"ON DGD._doorGroupID = DG._doorGroupID "+
						"INNER JOIN DOOR AS D "+
						"ON D._doorID = DGD._doorID "+
						"INNER JOIN DOORAVAILABLEPERIOD AS DAP "+
						"ON DAP._doorGroupID = DGD._doorGroupID "+
						"INNER JOIN AVAILABLEPERIOD AS AP "+
						"ON AP._availableID = DAP._availableID "+
						"WHERE U._userID = "+ String.valueOf(userID) +
						" AND D._doorID = "+ String.valueOf(doorID) +
						" UNION "+
						"SELECT AP._startTime, "+
						"AP._endTime, "+
						"U._userID, "+
						"D._doorID "+	
						"FROM OFFICE AS O  " +     
						"INNER JOIN DOOR AS D "+
						"ON O._doorID = D._doorID "+
						"INNER JOIN USERS AS U "+
						"ON U._userID = O._userID "+
						"LEFT JOIN DOORGROUPDOOR AS DGD "+
						"ON D._doorID = DGD._doorID "+
						"LEFT JOIN DOORGROUP AS DG "+
						"ON DG._doorGroupID = DGD._doorGroupID "+
						"LEFT JOIN DOORAVAILABLEPERIOD AS DAP "+
						"ON DAP._doorGroupID = DGD._doorGroupID "+
						"LEFT JOIN AVAILABLEPERIOD AS AP "+
						"ON AP._availableID = DAP._availableID "+
						"WHERE U._userID = "+ String.valueOf(userID) +
						" AND D._doorID = "+ String.valueOf(doorID) +";";	
			ResultSet rs = uow.RunStatement(sql);			
			return Mapping.mapUserDoorAccess(rs);
		} 
		catch(ClassNotFoundException ex)
		{
			throw new Exception("Unable to query database access", ex);
		}	
		finally
		{
			if(uow != null)uow.Done();
		}
	}
}	
