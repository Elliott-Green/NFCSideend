package database.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.access.DBFactory;
import database.access.IUnitOfWork;
import database.types.User;




public class NFCRepository implements INFCRepository{

	private final DBFactory _dbFactory;
	
	public NFCRepository(String connectionString, String username, String password){
		_dbFactory = new DBFactory(connectionString, username, password);
	}	

	@Override
	public ArrayList<User> GetAllUsers() throws Exception {
		//try and get a result set of all the users
		IUnitOfWork uow = null;
		try
		{
			uow = _dbFactory.CreateUnitOfWork();
			
			ResultSet rs =   uow.RunStatement("SELECT * FROM USERS");
		
			return mapUsers(rs);
			
		}
		catch(ClassNotFoundException ex){
			throw new Exception("Unable to query database", ex);
		}	
		finally{
			uow.Done();
		}
	}

	@Override
	public User GetMatchingUser(String username) throws Exception {
		//try and get a result set of all the users
		try
		{
			IUnitOfWork uow = _dbFactory.CreateUnitOfWork();
		
			ResultSet rs =  uow.RunStatement("SELECT * FROM USERS where _username = '"+username+"'");
			
			ArrayList<User> users = mapUsers(rs);
			
			if (users.isEmpty()) throw new Exception("User '"+username+"' not found");
			return users.get(0);
		}
		catch(ClassNotFoundException ex){
			throw new Exception("Unable to query database", ex);
		}
			
		}

	
	
	
	
	
		private ArrayList<User> mapUsers(ResultSet rs) throws SQLException {
			ArrayList<User> results = new ArrayList<User>();
			
			while(rs.next()){
				results.add(mapSingleUser(rs));
			}
			
			return results;
		}
		
		
		
		
		
		private User mapSingleUser(ResultSet rs) throws SQLException{
			Integer id =rs.getInt("_userID");
			String un =rs.getString("_username");
			String pw =rs.getString("_password");
			return new User(id,un,pw);
		}
	}	
