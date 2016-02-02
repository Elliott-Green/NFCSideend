package Main;

import java.util.ArrayList;

import arduino2.TwoWaySerialComm;
import database.access.DBFactory;
import database.repository.NFCRepository;
import database.types.User;





public class Main {

	public static void main(String[] args) throws Exception 
	{
		
	
		try
        {
            (new TwoWaySerialComm()).connect("COM10");
            
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
		
		
		
		NFCRepository db = new NFCRepository("jdbc:mysql://51.255.42.59:3306/NFC" , "jroot"  , "javapassword");

		
	}
}


