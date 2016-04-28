package database.access;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

public class DBFactory {
private final String _connectionString;
private final String _un;
private final String _pw;

	public DBFactory(String connectionString, String username, String password)
	{
		_connectionString = connectionString;
		_un = username;
		_pw = password;
	}
	
	/*
	 * This class is required for any database calls from within the DBRepo because its responsible for the driver and the connection strings.
	 * 
	 * Input : call when Constructor has been made.
	 * Output : a Connection object with passed values.
	 */
	public IUnitOfWork CreateUnitOfWork() throws  SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(_connectionString,_un,_pw);				
		return new UnitOfWork(connection);
	}
}
