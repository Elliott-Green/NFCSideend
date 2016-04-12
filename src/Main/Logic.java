package Main;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Security.IEncryption;
import arduino.DoorResult;
import arduino.ITwoWaySerialComm;
import database.repository.INFCRepository;
import database.types.User;
import database.types.UserDoorAccess;

public class Logic implements ILogic {

	private final INFCRepository _repo;
	private final IEncryption _encryption;
	private final ITwoWaySerialComm _inputComm;
	private final ITwoWaySerialComm _outputComm;
	private final IClock _clock;


	public Logic(INFCRepository repo, IEncryption encryption, ITwoWaySerialComm inputComm, ITwoWaySerialComm outputComm, IClock clock)
	{
		_repo = repo;
		_encryption = encryption;
		_inputComm = inputComm;
		_outputComm = outputComm;
		_clock = clock;
	}


	@Override
	public void monitorDoor(int doorID, int keyLength) {		
		String key = _inputComm.read(keyLength);

		try {
			User u = getUserFromKey(key);
			//if not a valid user, write to serial
			if(u != null) {				
				if(userCanAccessDoor(u.get_userID(), doorID))
				{
					writeDoorResult(DoorResult.OpenDoor);
				}
				else
				{
					writeDoorResult(DoorResult.NoAccess);
				}
			}
			else{			
			writeDoorResult(DoorResult.InvalidUser);
			}			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}	

	@Override
	public boolean userCanAccessDoor(int userID, int doorID) throws Exception
	{
		Date d = _clock.getNow();
		long now = d.getTime();
		_repo.logUserAccess(userID, doorID);
		ArrayList<UserDoorAccess> access = _repo.getUserDoorAccess(userID, doorID);

		if(access.isEmpty()) return false;

		for(UserDoorAccess a : access)
		{
			final UserDoorAccess uda = a;

			if(uda.get_startTime().getHours() == 0 && uda.get_startTime().getMinutes() == 0 && uda.get_endTime().getHours() == 0 && uda.get_endTime().getMinutes() == 0 || uda.get_startTime().getTime() <= now && uda.get_endTime().getTime() >= now)
			{			
				return true;
			}
		}
		return false;
	}

	//horrible if lots of user table rows
	@Override
	public User getUserFromKey(String key) throws Exception{
		ArrayList<User> users;
		users = _repo.GetAllUsers();

		Predicate<? super User> predicate = new Predicate<User>() {
			@Override
			public boolean test(User u) {
				return _encryption.compareKeyToHash(key, u.get_key());
			}
		};
		
		List<User> filtered = users.parallelStream().filter(predicate).collect(Collectors.toList());
		if(filtered.size() < 1) return null;
		return filtered.get(0);
	}
	
	private void writeDoorResult(DoorResult result) throws IOException{
		_outputComm.write(result.getDoorValue());
	}
}
