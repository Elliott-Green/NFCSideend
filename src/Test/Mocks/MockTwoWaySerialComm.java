package Test.Mocks;

import java.io.IOException;
import java.util.ArrayList;

import arduino.ITwoWaySerialComm;

public class MockTwoWaySerialComm implements ITwoWaySerialComm {
	
	private final ArrayList<String> _writes;
	private final String _readValue;
	private boolean _readCalled;
	private int _keyLengthProvided;
	
	public MockTwoWaySerialComm(String readValue) 
	{
		_writes = new ArrayList<>();
		_readValue = readValue;
		_keyLengthProvided = -1;
		
	}

	@Override
	public void write(String send) throws IOException 
	{
		_writes.add(send);
		
	}

	@Override
	public String read(int chars) 
	{
		_keyLengthProvided = chars;
		_readCalled = true;
		return _readValue;
	}

	public int writeCount()
	{
		return _writes.size();
	}
	
	public ArrayList<String> getWrites()
	{
		return _writes;
	}
	public int getKeyLengthProvided()
	{
		return _keyLengthProvided;
	}
	public boolean getReadCalled()
	{
		return _readCalled;
	}
	
}
