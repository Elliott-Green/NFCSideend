package Test.Mocks;

import java.util.ArrayList;

import database.repository.INFCRepository;
import database.types.User;
import database.types.UserDoorAccess;

public class MockNFCRepository implements INFCRepository {

	private final ArrayList<User> _allUsers;
	private final ArrayList<UserDoorAccess> _userDoorAccess;

	public MockNFCRepository(ArrayList<User> allUsers,ArrayList<UserDoorAccess> uda) {
		// 
		_allUsers = allUsers;
		_userDoorAccess = uda;
	}

	@Override
	public ArrayList<User> GetAllUsers() throws Exception {
		return _allUsers;
	}

	@Override
	public void logUserAccess(int userID, int doorID) throws Exception {
		// TODO Auto-generated method stub
		//cant do needs DB access - not live

	}

	@Override
	public ArrayList<UserDoorAccess> getUserDoorAccess(int userID, int doorID) throws Exception {
		return _userDoorAccess;
	}

	@Override
	public void createNewUser(String username, String lastname, String hashedKey) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public User getUserFromHash(String hashedKey) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createNewUserRole(int userID, int roleID) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
