package main;


import org.mindrot.jbcrypt.BCrypt;

import Security.Encryption;
import Security.IEncryption;
import arduino.ITwoWaySerialComm;
import arduino.TwoWayCommFactory;
import arduino.TwoWaySerialComm;
import database.repository.INFCRepository;
import database.repository.NFCRepository;
import database.types.User;

public class Main 
{
	//Hard coded doorID value
	final static int doorID = 1;
	//Hard coded keyLength (current project uses 8 chars of UID)
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



		//Monitor a given door forever
		while(true)
		{
			logic.monitorDoor(doorID, keyLength);
			Thread.sleep(500);
		}	

//		while(true)
//		{
//			logic.monitorDoorInitiation(keyLength);
//		}


	}
}



