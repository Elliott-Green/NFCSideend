package main;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

import Security.IEncryption;
import arduino.DoorResult;
import arduino.ITwoWaySerialComm;
import database.repository.INFCRepository;
import database.types.User;
import database.types.UserDoorAccess;

/*
 * This is the main class compromised of the communication classes and the other functionality classes. This is called once in main, if called
 */
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

	/*
	 * This is the main method called from Main.main to deal with door logic.
	 * 
	 * Input : door ID , key length
	 * void : Write LED result based on logic from queries.
	 */
	@Override
	public void monitorDoor(int doorID, int keyLength) 
	{		
		String key = _inputComm.read(keyLength);
		try 
		{
			User u = getUserFromKey(key);
			if(u != null) 
			{				
				if(userCanAccessDoor(u.get_userID(), doorID))
				{
					writeDoorResult(DoorResult.OpenDoor);
				}
				else
				{
					writeDoorResult(DoorResult.NoAccess);
				}
			}
			else
			{			
			writeDoorResult(DoorResult.InvalidUser);
			}			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}	
	
	/*
	 * This is the initiation points entry method. This method takes the input from the user and tries to call the insert statements.
	 * 
	 * Input : the key length to read (usually 8 for NFCa)
	 * Void : attempts to add user to the system / possible throws exceptions
	 */
	@Override
	public void monitorDoorInitiation(int keyLength) throws Exception 
	{		
		String UID = _inputComm.read(keyLength);

		Scanner in = new Scanner(System.in);
		System.out.println("Enter your first name");
		String firstname = in.nextLine();
		System.out.println("Enter your last name / password");
		String lastname = in.nextLine();
		System.out.println("Enter your role ID 1-7");
		int roleID = in.nextInt();
		System.out.println();
		in.close();
		
		addUserToSystem(firstname, lastname, UID, roleID);

	}	

	/*
	 * This method determines the permission a user has on a door by calling getUserDoorAccess which calls the Union SQL query to return the permissions
	 * Also this method uses the RXTX communication to send a byte to the arduino to signal the LED's.
	 * Input : User, obtained from scanning input
	 * Output : boolean if user can access door
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean userCanAccessDoor(int userID, int doorID) throws Exception
	{
		Date d = _clock.getNow();
		long now = d.getTime();

		ArrayList<UserDoorAccess> access = _repo.getUserDoorAccess(userID, doorID);

		if(access.isEmpty()) return false;

		for(UserDoorAccess a : access)
		{
			final UserDoorAccess uda = a;
	
			//office permissions without time.
			if(uda.get_doorID() == doorID && uda.get_userID() == userID)
			{
				_repo.logUserAccess(userID, doorID);
				return true;
			}
			//door permissions with time.
			if(uda.get_startTime().getHours() == 0 && uda.get_startTime().getMinutes() == 0 && uda.get_endTime().getHours() == 0 && uda.get_endTime().getMinutes() == 0 || uda.get_startTime().getTime() <= now && uda.get_endTime().getTime() >= now)
			{		
				_repo.logUserAccess(userID, doorID);
				return true;
			}
			
			
		}
		return false;
	}

	/*
	 * This method uses a predicate to test the keys obtained from getAllUsersand a parallel stream to speed up the time to calculate.
	 * Input : hashed UID.
	 * Output : user of UID.
	 */
	@Override
	public User getUserFromKey(String hashedKey) throws Exception
	{
		ArrayList<User> users;
		users = _repo.GetAllUsers();

		Predicate<? super User> predicate = new Predicate<User>() {
			@Override
			public boolean test(User u) {
				return _encryption.compareKeyToHash(hashedKey, u.get_key());
			}
		};
		
		List<User> filtered = users.parallelStream().filter(predicate).collect(Collectors.toList());
		if(filtered.size() < 1) return null;
		return filtered.get(0);
	}
	
	private void writeDoorResult(DoorResult result) throws IOException{
		_outputComm.write(result.getDoorValue());
	}


	/*
	 * This method is called from the initiation point.
	 * 
	 * Input : user variables and role ID.
	 * Output : new User object (so we can call getUserFromKey on newly created user to get UserID which then is used to update the UserRole table).
	 */
	@Override
	public User addUserToSystem(String username, String lastname, String UID, int roleID) throws Exception 
	{
		String hashedKey = BCrypt.hashpw(UID, BCrypt.gensalt(12));
		
		if(getUserFromKey(UID) == null)
		{
		_repo.createNewUser(username, lastname, hashedKey);
		
		if(hashedKey.length() != 60) { System.out.println("BCrypt hashed incorrectly"); return null; }
		User newUser = getUserFromKey(UID);
		
		
		System.out.println(newUser.get_firstName());
		System.out.print(newUser.get_lastName());
		System.out.print(newUser.get_key());
		System.out.print(newUser.get_userID());
		System.out.println("you were added");
		
		addUserRoleToSystem( roleID, newUser.get_userID());
		return newUser;
		}
		else
		{
			System.out.println("you are already a user");
			return null;
		}
	}
	
	/*
	 * This method is also called with the initiation point to add a users role. 
	 * It does a basic check on door permissions to check if the permissions were added.
	 * 
	 * Input : userID,  roleID.
	 * Void : adds user role to system.
	 */
	@Override
	public void addUserRoleToSystem(int userID, int roleID) throws Exception 
	{
		_repo.createNewUserRole(roleID,userID);
		if(userCanAccessDoor(userID, 1)) System.out.println("your permissions were added");
		else System.out.println("Something went wrong");
		
	}
}
