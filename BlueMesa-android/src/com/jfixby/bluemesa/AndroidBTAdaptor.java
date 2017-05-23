
package com.jfixby.bluemesa;

import com.jfixby.scarabei.api.collections.CollectionConverter;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class AndroidBTAdaptor implements AndroidBluetoothAdapter {

	@Override
	public Set<AndroidBluetoothDevice> getBondedDevices () {
		final BluetoothAdapter ad = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
		final java.util.Set<BluetoothDevice> devices = ad.getBondedDevices();
		final Set<BluetoothDevice> in = Collections.newSet(devices);
		final Set<AndroidBluetoothDevice> output = Collections.newSet();
		Collections.convertCollection(in, output, new CollectionConverter<BluetoothDevice, AndroidBluetoothDevice>() {

			@Override
			public AndroidBluetoothDevice convert (final BluetoothDevice input) {
				return new RedAndroidBluetoothDevice(input);
			}

		});
		return output;
	}

}
