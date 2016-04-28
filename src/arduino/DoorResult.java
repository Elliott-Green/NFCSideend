package arduino;

/*
 * This class contains the enums of result types.
 * Originally sending down raw bytes (1),(2)... 
 * I decided this was a better way to deal with more (future) result types
 */
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
