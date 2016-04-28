package arduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.SerialPort;

public class TwoWaySerialComm implements ITwoWaySerialComm
{
	private final SerialPort _port; 

	public TwoWaySerialComm(SerialPort port) throws IOException
	{
		_port = port;
	}

	
	/*
	 * This method uses the output COM port to write the result of the door being accessed to the Arduino 
	 * 
	 * Void : writes a string, serially, to the Arduino.
	 */
	@Override
	public void write(String send) throws IOException 
	{
		OutputStream out = _port.getOutputStream();
		out.write(send.getBytes());
		out.flush();
	}
	
	/*
	 * This method uses the input COM port to read the UID value being passed by the Arduino
	 * 
	 * Input : int of chars to be read (see const defined in main).
	 * Output : The read string.
	 */
	@Override
	public String read(int chars) 
	{
		try
		{
			InputStream in = _port.getInputStream();
			StringBuilder sb = new StringBuilder();
			boolean hasData = false;

			byte[] buffer = new byte[8];
			int len = 0;
			String val = null;
			
			//while buffer has data
			while (!hasData && ( len = in.read(buffer)) > -1 )
			{	
				//read from the buffer into String val and append to StringBuffer sb
				if(len != 0)
				{						
					val = new String(buffer,0,len);	
					sb.append(val);
				}
				//if correct read
				if( sb.length() == chars)
				{	
					System.out.println(sb.toString());
					hasData = true;
					buffer = new byte[8];
					String result = sb.toString();
					in.close();
					sb.setLength(0);
					return result;
				}	
			}

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		
		//else return empty
		return "";   
	}

}





