package main;

import java.io.IOException;

import com.mysql.jdbc.log.Log;

import Security.Encryption;
import Security.IEncryption;
import arduino.ITwoWaySerialComm;
import arduino.InvalidPortTypeException;
import arduino.TwoWayCommFactory;
import arduino.TwoWaySerialComm;
import database.repository.INFCRepository;
import database.repository.NFCRepository;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

public class runInit implements Runnable 
{


	public runInit() throws IOException, UnsupportedCommOperationException, PortInUseException, NoSuchPortException, InvalidPortTypeException
	{
		//Bootstrap application dependencies
		final INFCRepository repo = new NFCRepository("jdbc:mysql://51.255.42.59:3306/NFC" , "jroot"  , "javapassword");		
		final IEncryption encryption = new Encryption();
		//Create 'Door LED' Serial object
		final ITwoWaySerialComm inputComm =  new TwoWaySerialComm(TwoWayCommFactory.getSerialPort("COM7"));			
		final ITwoWaySerialComm outputComm =  new TwoWaySerialComm(TwoWayCommFactory.getSerialPort("COM3"));
		final IClock realTime = new Clock();
		final ILogic logic = new Logic(repo, encryption,inputComm,outputComm,realTime);	
	}

	public void run() 
	{
		System.out.println("Hello from a runInit!");
	}

	public static void main(String args[]) throws IOException, UnsupportedCommOperationException, PortInUseException, NoSuchPortException, InvalidPortTypeException 
	{
		(new Thread(new runInit())).start();
	}

}