package Test.Mocks;

import Security.IEncryption;

public class MockEncryption implements IEncryption{

	private boolean _keysMatch;
	private boolean _keysMatchCalled;
	
	public MockEncryption(boolean keysMatch) 
	{
		_keysMatch = keysMatch;
		_keysMatchCalled = false;
	}
	
	
	@Override
	public boolean compareKeyToHash(String key, String hash) {
		_keysMatchCalled = true;
		return _keysMatch;
	}
	
	public boolean keysMatchedCalled()
	{
		return _keysMatchCalled;
		
	}
}
