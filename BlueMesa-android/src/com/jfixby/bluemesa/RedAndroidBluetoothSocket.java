
package com.jfixby.bluemesa;

import java.io.IOException;
import java.io.InputStream;

import android.bluetooth.BluetoothSocket;

public class RedAndroidBluetoothSocket implements AndroidBluetoothSocket {

	private final BluetoothSocket socket;

	public RedAndroidBluetoothSocket (final BluetoothSocket createInsecureRfcommSocketToServiceRecord) {
		this.socket = createInsecureRfcommSocketToServiceRecord;
	}

	@Override
	public void connect () throws IOException {
		this.socket.connect();
	}

	@Override
	public InputStream getInputStream () throws IOException {
		return this.socket.getInputStream();
	}

}
