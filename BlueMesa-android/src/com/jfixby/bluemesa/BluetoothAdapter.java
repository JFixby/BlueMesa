
package com.jfixby.bluemesa;

import com.jfixby.scarabei.api.collections.Collection;

public abstract class BluetoothAdapter {

	public static BluetoothAdapter getDefaultAdapter () {
		return null;
	}

	public Collection<BluetoothDevice> getBondedDevices () {
		return null;
	}

}
