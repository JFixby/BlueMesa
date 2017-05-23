
package com.jfixby.bluemesa;

import java.io.IOException;
import java.util.UUID;

import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.CollectionConverter;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

public class RedAndroidBluetoothDevice implements AndroidBluetoothDevice {

	private final BluetoothDevice device;

	public RedAndroidBluetoothDevice (final BluetoothDevice device) {
		this.device = device;
	}

	@Override
	public Collection<AndroidParcelUuid> getUuids () {
		final List<ParcelUuid> in = Collections.newList(this.device.getUuids());
		final List<AndroidParcelUuid> out = Collections.newList();
		Collections.convertCollection(in, out, new CollectionConverter<ParcelUuid, AndroidParcelUuid>() {
			@Override
			public AndroidParcelUuid convert (final ParcelUuid x) {
				return new RedAndroidParcelUuid(x);
			}
		});
		return out;
	}

	@Override
	public AndroidBluetoothSocket createInsecureRfcommSocketToServiceRecord (final UUID uuid) throws IOException {
		return new RedAndroidBluetoothSocket(this.device.createInsecureRfcommSocketToServiceRecord(uuid));
	}

	@Override
	public String  getDeviceID() {
		L.d("device found ",device);
//		Sys.exit();

		return
				device.getAddress();
	}

}
