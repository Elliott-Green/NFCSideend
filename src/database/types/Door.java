package database.types;

import java.sql.Time;

public class Door 
{
	static int door;
	private final int _doorID;
	private final String _doorName;
	private final Time _doorTimeIn;
	private final Time _doorTimeOut;
	private final int _buildingID;
	
	public Door(int doorID, String doorName , Time doorTimeIn, Time doorTimeOut, int bid)
	{
		_doorID = doorID;
		_doorName = doorName;
		_doorTimeIn = doorTimeIn;
		_doorTimeOut = doorTimeOut;
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


	public Time get_doorTimeIn() {
		return _doorTimeIn;
	}


	public Time get_doorTimeOut() {
		return _doorTimeOut;
	}


	public static void setCurrentDoor(int doorID) 
	{
		 door = doorID;
	}
	
	public static int getCurrentDoor() 
	{
		return door;
	}
	
	

}
