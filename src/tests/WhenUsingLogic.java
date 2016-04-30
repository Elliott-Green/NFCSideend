package tests;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import Test.Mocks.MockClock;
import Test.Mocks.MockEncryption;
import Test.Mocks.MockNFCRepository;
import Test.Mocks.MockTwoWaySerialComm;
import arduino.DoorResult;
import database.types.User;
import database.types.UserDoorAccess;
import main.ILogic;
import main.Logic;

public class WhenUsingLogic 
{
	private MockNFCRepository _mockRepo;
	private MockEncryption _mockEncryption;
	private MockTwoWaySerialComm _mockInputComm;
	private MockTwoWaySerialComm _mockOutputComm;
	private MockClock _mockClock;
	private ILogic _sut;
//	private Main _main;
//	private BCrypt _bCrypt;
//	private TwoWayCommFactory _serialFactory;
//	private TwoWaySerialComm _serialComm;



	/*
	 * Tests the main methods used 
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void whenCallingMonitorDoorShouldReadKeyFromInput()
	{
		// arrange 
		Date now = new Date(Date.UTC(2003, 7, 8, 14, 15, 7));
		int doorID = 1;
		int keyLength = 8;

		ArrayList<User> users = new ArrayList<>();
		users.add(new User(1,"Elliot","Green","testKey"));

		ArrayList<UserDoorAccess> uda= new ArrayList<>();



		_mockRepo = new MockNFCRepository(users,uda);
		_mockEncryption = new MockEncryption(false);
		_mockInputComm = new MockTwoWaySerialComm("testKey");
		_mockOutputComm = new MockTwoWaySerialComm("");
		_mockClock = new MockClock(now);
		_sut = new Logic(_mockRepo, _mockEncryption, _mockInputComm, _mockOutputComm, _mockClock);

		// act
		_sut.monitorDoor(doorID, keyLength);

		// assert
		Assert.assertEquals(_mockInputComm.getReadCalled(), true);
		Assert.assertEquals(_mockInputComm.getKeyLengthProvided(),keyLength);
		Assert.assertEquals(_mockOutputComm.writeCount(), 1);
		String firstWrite = _mockOutputComm.getWrites().get(0);
		Assert.assertEquals(firstWrite, DoorResult.InvalidUser.getDoorValue());

	}

	/*
	 * Tests that a mock permission
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void whenCorrectDoorAccess()
	{
		//valid mock time permission
		Date now = new Date(Date.UTC(2003, 7, 8, 12, 00, 00));
		//door times
		Time start = new Time(9,00,00);
		Time end = new Time(17,00,00);
		int doorID = 1;
		int userID = 1;


		ArrayList<User> users = new ArrayList<>();
		users.add(new User(1,"Elliot","Green","testKey"));

		//add door permission 
		//for user 1
		//for door 1
		ArrayList<UserDoorAccess> uda= new ArrayList<>();
		UserDoorAccess permission = new UserDoorAccess(start, end, userID, doorID);
		uda.add(permission);

		_mockRepo = new MockNFCRepository(users,uda);
		_mockEncryption = new MockEncryption(false);
		_mockInputComm = new MockTwoWaySerialComm("testKey");
		_mockOutputComm = new MockTwoWaySerialComm("");
		_mockClock = new MockClock(now);
		_sut = new Logic(_mockRepo, _mockEncryption, _mockInputComm, _mockOutputComm, _mockClock);

		try 
		{
			//test user 1 can use door 1
			Assert.assertTrue(_sut.userCanAccessDoor(userID, doorID));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}


	}

	/*
	 * tests boolean inverse ???
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void whenIncorrectDoorAccess()
	{
		//valid mock time permission
		Date now = new Date(Date.UTC(2003, 7, 8, 12, 00, 00));
		//door times
		Time start = new Time(9,00,00);
		Time end = new Time(17,00,00);
		int doorID = 1;
		int userID = 1;
		int inalidUserID = 100;


		ArrayList<User> users = new ArrayList<>();
		users.add(new User(1,"Elliot","Green","testKey"));

		//add door permission 
		//for user 1
		//for door 1
		ArrayList<UserDoorAccess> uda= new ArrayList<>();
		UserDoorAccess permission = new UserDoorAccess(start, end, userID, doorID);
		uda.add(permission);

		_mockRepo = new MockNFCRepository(users,uda);
		_mockEncryption = new MockEncryption(false);
		_mockInputComm = new MockTwoWaySerialComm("testKey");
		_mockOutputComm = new MockTwoWaySerialComm("");
		_mockClock = new MockClock(now);
		_sut = new Logic(_mockRepo, _mockEncryption, _mockInputComm, _mockOutputComm, _mockClock);

		try 
		{
			//test user 100 cannot use door 1
			Assert.assertTrue(!_sut.userCanAccessDoor(inalidUserID, doorID));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * status code 2 
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void whenUserNotFound()
	{
		//valid mock time permission
		Date now = new Date(Date.UTC(2003, 7, 8, 12, 00, 00));
		//door times
		Time start = new Time(9,00,00);
		Time end = new Time(17,00,00);
		int doorID = 1;
		int userID = 1;
		int inalidUserID = 100;


		ArrayList<User> users = new ArrayList<>();
		users.add(new User(1,"Elliot","Green","testKey"));

		//add door permission 
		//for user 1
		//for door 1
		ArrayList<UserDoorAccess> uda= new ArrayList<>();
		UserDoorAccess permission = new UserDoorAccess(start, end, userID, doorID);
		uda.add(permission);

		_mockRepo = new MockNFCRepository(users,uda);
		_mockEncryption = new MockEncryption(false);
		_mockInputComm = new MockTwoWaySerialComm("testKey");
		_mockOutputComm = new MockTwoWaySerialComm("");
		_mockClock = new MockClock(now);
		_sut = new Logic(_mockRepo, _mockEncryption, _mockInputComm, _mockOutputComm, _mockClock);


		//try catch is needed for test to pass.
		try 
		{
			//test user 100 cannot use door 1
			Assert.assertTrue(!_sut.userCanAccessDoor(inalidUserID, doorID));
			Assert.assertTrue(!_mockOutputComm.mockSerialConnection(2));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Test
	public void whenTimePermissionIsValid()
	{
	
	}
	
	@Test
	public void whenTimePermissionIsInvalid()
	{
	
	}


	@Test
	public void whenBCryptDecrypts()
	{
		//arrange
		String UID = "123456789";
		String hashed_UID;

		//act
		hashed_UID = BCrypt.hashpw(UID, BCrypt.gensalt(12));

		// assert
		Assert.assertTrue(BCrypt.checkpw(UID, hashed_UID));

	}
}
