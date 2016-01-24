package Main;

import java.sql.ResultSet;
import java.util.ArrayList;

import arduino2.TwoWaySerialComm;
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
		
		
		
		
	}
}


