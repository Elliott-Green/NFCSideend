package database.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UnitOfWork implements IUnitOfWork {

	private final Connection _conn;

	
	public UnitOfWork(Connection conn)
	{
		_conn = conn;
	}

	/*
	 * Very bad to handle raw SQL strings, within a closed enviroment I allow this practice.
	 * 
	 * Input : @String of SQL to be sent to database.
	 * Output : A ResultSet of that query.
	 */
	@Override
	public ResultSet RunStatement(String sql) throws SQLException 
	{
		if(_conn.isClosed())
		{
			throw new SQLException("Connection was already closed");
		}

		try
		{
			ResultSet rs = null;
			PreparedStatement statement = _conn.prepareStatement(sql);
			rs = statement.executeQuery();

			return rs;
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}
	
	/*
	 * A better way to deal with queries that are state changing (insert,update,delete)
	 * This method calls a SP on the database with the relevant types being changed into their SQL type counterpart.
	 * 
	 * Input: @String of the sprocs name, @HashMap of the String versions of data, and what data-type the db row is.
	 * Output : Two visual console prints. One to confirm the sproc name is valid, and another to confirm that the statement has been executed.
	 * 
	 */
	@Override
	public void RunStoredProcedure(String sprocName, HashMap<String, DBParam> params ) throws Exception {

		try{

			int paramSize = params.size();
			StringBuilder sb = new StringBuilder();
			sb.append("{CALL ")
			.append(sprocName)
			.append("(");

			for(int i=0; i<paramSize; i++)
			{
				sb.append("?");
				if(i < paramSize - 1)
				{
					sb.append(",");
				}

			}
			sb.append(")}");
			String call = sb.toString();
			PreparedStatement cs = _conn.prepareStatement(call);
			
			System.out.println("you called : " + call);

			int index = 1;

			for(DBParam param : params.values())
			{
				switch(param.getType())
				{

				case BIT:
					cs.setBoolean(index, param.getValue() == "1" ? true : false);
					break;
				case DATE:
					//toDO
					//cs.setDate(index, Date.parse(param.getValue()));
					break;
				case INTEGER:
					cs.setInt(index, Integer.parseInt(param.getValue()));
					break;
				case NVARCHAR:
					cs.setString(1,param.toString());
					break;
				case TIME:
					//toDO
					//cs.setTime(index, Time.parse(param.getValue()));
					break;
				default:
					throw new Exception("This database type not supported : " + param.getType().getName());
				}
				index++;
			}

			System.out.println("This is the SPROC call :" + sprocName);
			
			for(DBParam param : params.values())
			{
				System.out.println(param.getValue()+"  "+param.getValue().length());
			}



			cs.execute(); 
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		finally
		{
			Done();
		}

	}

	/*
	 * VPS doesn't close connections by default, this method closes the current connection.
	 * 
	 * Void : closes the connection
	 */
	@Override
	public void Done() throws SQLException 
	{
		_conn.close();
	}

}
