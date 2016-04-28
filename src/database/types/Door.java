package database.types;

public class Door 
{	
	private final int _doorID;
	private final String _doorName;
	private final int _buildingID;
	
	public Door(int doorID, String doorName , int bid)
	{
		_doorID = doorID;
		_doorName = doorName;
		_buildingID = bid;
	}


	public int get_buildingID() {
		return _buildingID;
	}


	public int get_doorID() {
		return _doorID;
	}


	public String get_doorName() {
		return _doorName;
	}
}
