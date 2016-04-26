package Logic.Test;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.mysql.fabric.xmlrpc.base.Data;

import Security.IEncryption;
import Test.Mocks.MockClock;
import Test.Mocks.MockEncryption;
import Test.Mocks.MockNFCRepository;
import Test.Mocks.MockTwoWaySerialComm;
import arduino.DoorResult;
import arduino.ITwoWaySerialComm;
import database.repository.INFCRepository;
import database.types.User;
import database.types.UserDoorAccess;
import main.IClock;
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
}
