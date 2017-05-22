
package com.jfixby.bluemesa;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.collections.Set;

public class AndroidBTConnectionOpener implements BTConnectionOpener {
	private final AndroidApplication app;
	private final String deviceId;

	public AndroidBTConnectionOpener (final AndroidApplication androidLauncher, final String deviceId) {
		this.deviceId = deviceId;
		this.app = androidLauncher;
		this.adaptor = androidLauncher.getAdaptor();

	}

	private final MesaBluetoothAdapter adaptor;

	@Override
	public DataInputStream open (final String url) throws IOException {
		{
// final Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
// this.app.startActivityForResult(turnOn, 0);
			this.app.requestBT();
		}

		final Set<BluetoothDevice> pairedDevices = (this.adaptor.getBondedDevices());
// pairedDevices.print("pairedDevices");
		if (pairedDevices.size() == 0) {
			return null;
		}

		final BluetoothDevice device = pairedDevices.getLast();
// L.d("device detected", device);

		final List<ParcelUuid> uids = Collections.newList(device.getUuids());
// uids.print("uids");

		final UUID uuid = UUID.fromString(uids.getLast().toString());
		// 01-01 05:20:18.510: I/System.out(12753): (0) 00001101-0000-1000-8000-00805f9b34fb

		// Sys.exit();
// device.createInsecureRfcommSocketToServiceRecord(uuid)

		final BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
		socket.connect();
// L.d("socket", socket);
// device.

		final InputStream is = socket.getInputStream();
// L.d("is", is);
		final DataInputStream dis = new DataInputStream(is);
// L.d("dis", dis);
		return dis;

	}

// private final UUID SERIAL_PORT = new UUID(0x1101);

}
