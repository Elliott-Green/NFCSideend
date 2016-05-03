package main;

import Security.Encryption;
import Security.IEncryption;
import arduino.ITwoWaySerialComm;
import arduino.TwoWayCommFactory;
import arduino.TwoWaySerialComm;
import database.repository.INFCRepository;
import database.repository.NFCRepository;

/*
 * This class can be subject to:
 * System not reading card lengths fully. -> (reset the system)
 * When resetting the system, make sure the terminal window has been properly closed. This can cause the connection to hang. (reset the system / close java.exe)
 * This system will require two Arduinos running the code provided on GitHub, and videos of the system can also be found there.
 * The MySQL code and SP's will also be available on GitHub, as well as the Arduinos .h / .cpp files provided by the linksprite NFC shield
 */
public class Main 
{
	//System constant variables
	final static int doorID = 24;
	final static int keyLength = 8;

	public static void main(String[] args) throws Exception 
	{

		//Bootstrap application dependencies
		final INFCRepository repo = new NFCRepository("jdbc:mysql://51.255.42.59:3306/NFC" , "jroot"  , "javapassword");		
		final IEncryption encryption = new Encryption();
		//Create 'Door LED' Serial object
		final ITwoWaySerialComm outputComm =  new TwoWaySerialComm(TwoWayCommFactory.getSerialPort("COM3"));
		//Create 'NFC reader' Serial Object
		final ITwoWaySerialComm inputComm =  new TwoWaySerialComm(TwoWayCommFactory.getSerialPort("COM10"));			
		final IClock realTime = new Clock();
		//Create main logic
		final ILogic logic = new Logic(repo, encryption,inputComm,outputComm,realTime);


		//Door system.
		//Monitor a given door forever.
		while(true)
		{
			logic.monitorDoor(doorID, keyLength);
			Thread.sleep(500);
		}	
		
		//Initiation System
		//Monitor no door, add new users to the system.
//		while(true)
//		{
//			logic.monitorDoorInitiation(keyLength);
//			Thread.sleep(500);
//		}


	}
}



