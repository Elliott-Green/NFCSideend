package database.types;

public class Section 
{
	private final int _sectionID;
	private final int _roleID;
	private final int _doorID;
	
	public Section(int sectionID, int roleID, int doorID)
	{
		_sectionID = sectionID;
		_roleID = roleID;
		_doorID = doorID;
	}

	public int get_sectionID() {
		return _sectionID;
	}

	public int get_roleID() {
		return _roleID;
	}

	public int get_doorID() {
		return _doorID;
	}
}
