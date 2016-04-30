package database.access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


public interface IUnitOfWork {
	 ResultSet RunStatement(String sql) throws SQLException;	
	 void RunStoredProcedure(String sprocName, HashMap<String, DBParam> params) throws SQLException, Exception;
	 public void RunStringStoredProcedure(String username, String lastname, String hashedKey);
	 void Done() throws SQLException;
}
