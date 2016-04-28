package main;

import java.util.Date;

import Security.Encryption;
import Security.IEncryption;
import Test.Mocks.MockClock;
import arduino.ITwoWaySerialComm;
import arduino.TwoWayCommFactory;
import arduino.TwoWaySerialComm;
import database.repository.INFCRepository;
import database.repository.NFCRepository;

public class Main 
{
	
	
	final static int doorID = 8;
	final static int keyLength = 8;
	
	public static void main(String[] args) throws Exception 
	{
		
		// Bootstrap application dependencies
		final INFCRepository repo = new NFCRepository("jdbc:mysql://51.255.42.59:3306/NFC" , "jroot"  , "javapassword");		
		final IEncryption encryption = new Encryption();		
		final ITwoWaySerialComm outputComm =  new TwoWaySerialComm(TwoWayCommFactory.getSerialPort("COM3"));
		final ITwoWaySerialComm inputComm =  new TwoWaySerialComm(TwoWayCommFactory.getSerialPort("COM10"));			
		final IClock realTime = new Clock();
		
		final MockClock fakeTime = new MockClock(new Date(Date.UTC(2012, 12, 1, 4, 0, 1)));
		
		// create main logic
		final ILogic logic = new Logic(repo, encryption,inputComm,outputComm,realTime);
		
		// monitor a given door forever
		while(true)
		{
			//fakeTime.setSeed(new Date(Date.UTC(2015, 12, 1, 4, 0, 1)));
			logic.monitorDoor(doorID, keyLength);
			Thread.sleep(500);
		}		
	}
}



