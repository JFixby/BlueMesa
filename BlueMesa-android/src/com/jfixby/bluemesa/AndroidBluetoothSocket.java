
package com.jfixby.bluemesa;

import java.io.IOException;
import java.io.InputStream;

public interface AndroidBluetoothSocket {

	void connect () throws IOException;

	InputStream getInputStream () throws IOException;

}
