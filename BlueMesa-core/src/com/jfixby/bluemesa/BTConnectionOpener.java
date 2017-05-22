
package com.jfixby.bluemesa;

import java.io.DataInputStream;
import java.io.IOException;

public interface BTConnectionOpener {

	DataInputStream open () throws IOException;

	String getDeviceID ();

}
