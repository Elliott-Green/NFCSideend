package arduino;

public enum DoorResult {
	OpenDoor("1"),
	InvalidUser("2"),
	NoAccess("3");
	
	private final String _commValue;
	
	DoorResult(String value){
		_commValue = value;
		
	}
	
	public String getDoorValue(){
		return _commValue;
	}
	
	
	
}
