
package com.jfixby.bluemesa;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.log.L;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

public class AndroidBTConnectionOpener implements BTConnectionOpener {
	private final AndroidApplication app;
	private final String deviceId;

	public AndroidBTConnectionOpener (final AndroidApplication androidLauncher, final String deviceId) {
		this.deviceId = deviceId;
		this.app = androidLauncher;

	}

	private BluetoothAdapter adaptor;

	@Override
	public DataInputStream open (final String url) throws IOException {
		this.adaptor = BluetoothAdapter.getDefaultAdapter();
		final Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		this.app.startActivityForResult(turnOn, 0);
		final Set<BluetoothDevice> pairedDevices = Collections.newSet(this.adaptor.getBondedDevices());
		pairedDevices.print("pairedDevices");
		if (pairedDevices.size() == 0) {
			return null;
		}

		final BluetoothDevice device = pairedDevices.getLast();
		L.d("device detected", device);
		final UUID uuid = UUID.randomUUID();
		// Sys.exit();
// device.createInsecureRfcommSocketToServiceRecord(uuid)
		final BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
		socket.connect();
		L.d("socket", socket);
// device.

		final InputStream is = socket.getInputStream();
		L.d("is", is);
		final DataInputStream dis = new DataInputStream(is);
		L.d("dis", dis);
		return dis;

	}

// private final UUID SERIAL_PORT = new UUID(0x1101);

}
