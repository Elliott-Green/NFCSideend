package arduino;

import java.io.IOException;

public interface ITwoWaySerialComm {

	void write(String send) throws IOException;
	String read(int chars);

}