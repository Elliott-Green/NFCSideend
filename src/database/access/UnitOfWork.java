package database.access;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;





public class UnitOfWork implements IUnitOfWork {
	
	private final Connection _conn;

	
	public UnitOfWork(Connection conn){
		_conn = conn;
	}

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

	
	


	@Override
	public void RunStoredProcedure(String sprocName, HashMap<String, DBParam> params ) throws Exception {
		
//		if(sprocName.equals(null)){
//			throw new Exception("Invalid sproc name");
//		}
//		if(!params.containsKey("userID")){
//			throw new Exception("Must supply userID");
//		}	
//		if(!params.containsKey("doorID")){
//			throw new Exception("Must supply doorID");
//		}
//		
//		int userID =  Integer.parseInt(params.get("userID"));
//		int doorID =  Integer.parseInt(params.get("doorID"));
		
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
		//debug
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
				//TODO
				//cs.setDate(index, Date.parse(param.getValue()));
				break;
			case INTEGER:
				cs.setInt(index, Integer.parseInt(param.getValue()));
				break;
			case NVARCHAR:
				cs.setString(index, param.getValue());
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
		
	
		
		
		cs.execute(); 
		
	}

	@Override
	public void Done() throws SQLException {
		// TODO Auto-generated method stub
		_conn.close();
	}

	@Override
	public ResultSet RunStoredFunction(String funcName, HashMap<String, DBParam> params)
			throws SQLException, Exception {
		// TODO Auto-generated method stub
		return null;
	}	

}
