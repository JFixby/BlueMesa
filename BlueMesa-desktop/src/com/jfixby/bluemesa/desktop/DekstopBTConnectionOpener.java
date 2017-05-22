
package com.jfixby.bluemesa.desktop;

import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.io.Connector;

import com.jfixby.bluemesa.BTConnectionOpener;

public class DekstopBTConnectionOpener implements BTConnectionOpener {

	private final String url;
	private final String deviceID;

	public DekstopBTConnectionOpener (final String DEVICE_ID) {

		this.deviceID = DEVICE_ID;
		this.url = "btspp://" + DEVICE_ID + ":1;authenticate=false;encrypt=false;master=false";

	}

	@Override
	public DataInputStream open () throws IOException {
		return Connector.openDataInputStream(this.url);
	}

	@Override
	public String getDeviceID () {
		return this.deviceID;
	}

}
