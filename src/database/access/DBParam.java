package database.access;

import java.sql.JDBCType;

public class DBParam {
	
	private final String _value;
	private final JDBCType _type;
	
	public DBParam(String value, JDBCType type){
		_value = value;
		_type = type;
	}
	
	public JDBCType getType()
	{
		return _type;
	}
	
	public String getValue()
	{
		return _value;
	}
	
}
