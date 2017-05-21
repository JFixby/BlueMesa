
package com.jfixby.bluemesa;

import java.io.DataInputStream;
import java.io.IOException;

public interface BTConnectionOpener {

	DataInputStream open (String url) throws IOException;

}
