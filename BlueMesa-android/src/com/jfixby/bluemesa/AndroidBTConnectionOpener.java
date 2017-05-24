
package com.jfixby.bluemesa;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.log.L;

public class AndroidBTConnectionOpener implements BTConnectionOpener {
	private final AndroidApplication app;

	public AndroidBTConnectionOpener (final AndroidApplication androidLauncher) {
		this.app = androidLauncher;
		this.adaptor = androidLauncher.getAdaptor();

	}

	private final AndroidBluetoothAdapter adaptor;
	private String deviceId;

	@Override
	public DataInputStream open () throws IOException {
		{
// final Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
// this.app.startActivityForResult(turnOn, 0);
			this.app.requestBT();
		}

		final Set<AndroidBluetoothDevice> pairedDevices = (this.adaptor.getBondedDevices());
		pairedDevices.print("pairedDevices");
		if (pairedDevices.size() == 0) {
			L.d("Failed to find devices");
			return null;
		}

		final AndroidBluetoothDevice device = pairedDevices.getLast();
		L.d("device detected", device);

		this.deviceId = device.getDeviceID().toUpperCase();

		final String url = "btspp://" + this.deviceId + ":1;authenticate=false;encrypt=false;master=false";
		L.d("BT start", url);

		final List<AndroidParcelUuid> uids = Collections.newList(device.getUuids());
		uids.print("uids");

		final String uidStr = uids.getLast().getString();
		L.d("uidStr", uidStr);
		final UUID uuid = UUID.fromString(uidStr);
		L.d("uuid", uuid);
// L.d("uuid", uuid);
// 01-01 05:20:18.510: I/System.out(12753): (0) 00001101-0000-1000-8000-00805f9b34fb

		// Sys.exit();
// device.createInsecureRfcommSocketToServiceRecord(uuid)

		final AndroidBluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
		socket.connect();
		L.d("socket", socket);
// device.

		final InputStream is = socket.getInputStream();
		L.d("is", is);
		final DataInputStream dis = new DataInputStream(is);
		L.d("dis", dis);
		return dis;

	}

	@Override
	public String getDeviceID () {
		return this.deviceId;
	}

// private final UUID SERIAL_PORT = new UUID(0x1101);

}
