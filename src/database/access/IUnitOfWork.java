package database.access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


public interface IUnitOfWork {
	 ResultSet RunStatement(String sql) throws SQLException;	
	 void Done() throws SQLException;
	 void RunStoredProcedure(String sprocName, HashMap<String, DBParam> params) throws SQLException, Exception;
	 ResultSet RunStoredFunction(String funcName, HashMap<String, DBParam> params) throws SQLException, Exception;
}
