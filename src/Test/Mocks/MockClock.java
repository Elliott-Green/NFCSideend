package Test.Mocks;

import java.util.Date;

import main.IClock;

public class MockClock implements IClock {
	private Date _seed;

	public MockClock(Date seed){
		_seed = seed;
	}

	@Override
	public Date getNow() {
		return _seed;
	}

	public void setSeed(Date newDate){
		_seed = newDate;
	}

}
