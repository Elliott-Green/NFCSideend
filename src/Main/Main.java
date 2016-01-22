package Main;

import arduino2.TwoWaySerialComm;
import arduino2.TwoWaySerialComm.SerialReader;


public class Main {

	public static void main(String[] args) 
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


