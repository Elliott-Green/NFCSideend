package Test.Mocks;

import java.util.ArrayList;

import database.repository.INFCRepository;
import database.types.User;
import database.types.UserDoorAccess;

public class MockNFCRepository implements INFCRepository {

	private final ArrayList<User> _allUsers;
	private final ArrayList<UserDoorAccess> _userDoorAccess;

	public MockNFCRepository(ArrayList<User> allUsers,ArrayList<UserDoorAccess> userDoorAccess) {
		// 
		_allUsers = allUsers;
		_userDoorAccess = userDoorAccess;
	}

	@Override
	public ArrayList<User> GetAllUsers() throws Exception {
		// TODO Auto-generated method stub
		return _allUsers;
	}

	@Override
	public void logUserAccess(int userID, int doorID) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<UserDoorAccess> getUserDoorAccess(int userID, int doorID) throws Exception {
		// TODO Auto-generated method stub
		return _userDoorAccess;
	}

}
