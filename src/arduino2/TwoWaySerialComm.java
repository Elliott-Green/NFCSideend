package arduino2;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Security.Encry;
import Security.Normal;


public class TwoWaySerialComm
{
	public static OutputStream output;
	
	public TwoWaySerialComm()
	{
		super();
	}

	public void connect ( String portName ) throws Exception
	{
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.out.println("Error: Port is currently in use");
		}
		else
		{
			CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

			if ( commPort instanceof SerialPort )
			{
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				(new Thread(new SerialReader(in))).start();
				(new Thread(new SerialWriter(out))).start();

			}
			else
			{
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}     
	}





	/*
	 * 
	 */
	public class SerialReader implements Runnable 
	{


		private final InputStream in;
		public SerialReader (InputStream in)
		{
			this.in = in;
		}

		public void run ()
		{
			byte[] buffer = new byte[7];
			int len = 0;
			String key = null;
			try
			{
				StringBuilder sb = new StringBuilder();
				boolean hasData =false;

				while ( ( len = this.in.read(buffer)) > -1 )
				{			
					//read the whole string.
					if(len != 0)
					{
						hasData = true;
						String val = new String(buffer,0,len);		
						sb.append(val);
					}
					else
					{
						//when finished reading, check authentication
						if( hasData)
						{
							key = sb.toString();
							sb = new StringBuilder();
							Encry e = new Encry();
							Encry.chkKeyHash(key);
						}
						hasData = false;

					}
				}
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}   

		}

	}

	public static class SerialWriter implements Runnable 
	{
		OutputStream out;

		public SerialWriter ( OutputStream out )
		{
			this.out = out;
		}

		public void run ()
		{
			try
			{                
				int c = 3;

				while ( ( c = System.in.read()) > -1 )
				{
					this.out.write(c);
				}   

			}
			catch ( IOException e )
			{
				e.printStackTrace();
				System.exit(-1);
			}  

		}
	}

	public static synchronized void writeData(String data) 
	{
		System.out.println("Senting: " + data);
		try 
		{
			Thread a = new Thread(SerialWriter(output));
			output.write(data.getBytes());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("could not write to port");
		}
	}
}
