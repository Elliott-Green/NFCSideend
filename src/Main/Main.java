package Main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import arduino2.TwoWaySerialComm;
import arduino2.TwoWaySerialComm.SerialWriter;
import database.access.DBFactory;
import database.repository.NFCRepository;
import database.types.Door;
import database.types.User;





public class Main 
{
	static Scanner s = new Scanner(System.in);
	int doorID;
	
	
	public static void main(String[] args) throws Exception 
	{
		System.out.println("NFC Door System V0.3 - Enter a DoorID to simulate");
		int doorID = s.nextInt();
		Door.setCurrentDoor(doorID);
		
		
		/*
		 * These lines are needed, if done statically, can really mess the DB factory up...
		 */
		NFCRepository db = new NFCRepository("jdbc:mysql://51.255.42.59:3306/NFC" , "jroot"  , "javapassword");
		Thread.sleep(200);
		boolean b = db.isValidDoorID(doorID);
		if(b)
		{
		Door.setCurrentDoor(doorID);
		
		}
		
		try
        {
            (new TwoWaySerialComm()).connect("COM10");
            
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
		
		
		TwoWaySerialComm.writeData("2");
		
		
		
		

	}
	
	
	
	
}


