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

public class NFCRepository implements INFCRepository{

	private final DBFactory _dbFactory;	

	public NFCRepository(String connectionString, String username, String password)
	{
		_dbFactory = new DBFactory(connectionString, username, password);
	}	



	//	public boolean addNewUserToSystem(String username, String password, String key, int role) throws ClassNotFoundException, SQLException
	//	{
	//		PreparedStatement ps = null;
	//		Connection conn = DriverManager.getConnection("jdbc:mysql://51.255.42.59:3306/NFC" , "jroot"  , "javapassword");
	//		String updateQuery = "INSERT INTO USERS(_username, _password, _key, _roleID) VALUES (?,?,?,?)";
	//		ps = (PreparedStatement) conn.prepareStatement(updateQuery);
	//		ps.setString(1, username);
	//		ps.setString(2, password);
	//		ps.setString(3, key);
	//		ps.setInt(4, role);
	//		
	//		ps.execute();
	//			
	//		System.out.println("You have been added to the db by method - NFCREPO.addNewUserToSystem.");
	//		return true;
	//
	//	}

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
			uow.RunStoredProcedure("sp_NFC_insert", params);
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

	//gets office permissions too (reiteration)
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
			throw new Exception("Unable to query database", ex);
		}	
		finally
		{
			if(uow != null)uow.Done();
		}
	}
	
}	
