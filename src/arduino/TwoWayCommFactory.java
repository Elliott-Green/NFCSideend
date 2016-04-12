package arduino;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class TwoWayCommFactory {
	
	public static SerialPort getSerialPort(String commPort) throws UnsupportedCommOperationException, PortInUseException, NoSuchPortException, InvalidPortTypeException{

		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(commPort);
		CommPort port = portIdentifier.open("ElliotRules",2000);
		
		if ( port instanceof SerialPort )
		{
			SerialPort serialPort = (SerialPort) port;
			serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);			
			return serialPort;
		}
		else{
			 throw new InvalidPortTypeException();
		}
	}
}

