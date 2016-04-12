package Main;

import java.util.Date;

public class Clock implements IClock {

	@Override
	public Date getNow() {
		return new Date();
	}

}
