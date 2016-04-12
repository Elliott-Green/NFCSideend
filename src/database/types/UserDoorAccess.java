package database.types;

import java.sql.Time;

public class UserDoorAccess {

	private final Time _startTime;
	private final Time _endTime;
	private final int _userID;
	private final int _doorID;

	
	public UserDoorAccess(Time startTime, Time endTime, int userID, int doorID)
	{
		_startTime = startTime;
		_endTime = endTime;
		_userID = userID;
		_doorID = doorID;
	}

	public Time get_startTime() {
		return _startTime;
	}

	public Time get_endTime() {
		return _endTime;
	}

	public int get_userID() {
		return _userID;
	}

	public int get_doorID() {
		return _doorID;
	}
}
