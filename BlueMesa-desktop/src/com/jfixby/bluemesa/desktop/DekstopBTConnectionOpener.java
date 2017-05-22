
package com.jfixby.bluemesa.desktop;

import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.io.Connector;

import com.jfixby.bluemesa.BTConnectionOpener;

public class DekstopBTConnectionOpener implements BTConnectionOpener {

	@Override
	public DataInputStream open (final String url) throws IOException {
		return Connector.openDataInputStream(url);
	}

}
