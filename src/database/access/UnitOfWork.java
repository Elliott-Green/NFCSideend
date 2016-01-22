package database.access;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class UnitOfWork implements IUnitOfWork {

	private final Connection _conn;
	
	public UnitOfWork(Connection conn){
		_conn = conn;
	}

	@Override
	public ResultSet RunStatement(String sql) throws SQLException {
	
		if(_conn.isClosed()){
			throw new SQLException("Connection was already closed");
		}
		
		try{
			ResultSet rs = null;
			
			PreparedStatement statement = _conn.prepareStatement(sql);
			
			rs = statement.executeQuery();
			return rs;
		}
		catch(SQLException ex){
			ex.printStackTrace();
			throw ex;
		}
			}

	

	@Override
	public void Done() throws SQLException {		
		_conn.close();
		System.out.println("Connection closed");
	}	
}
