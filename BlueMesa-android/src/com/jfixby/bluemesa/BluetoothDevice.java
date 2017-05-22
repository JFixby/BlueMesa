
package com.jfixby.bluemesa;

import java.util.UUID;

import com.jfixby.scarabei.api.collections.Collection;

public interface BluetoothDevice {

	Collection<ParcelUuid> getUuids ();

	BluetoothSocket createInsecureRfcommSocketToServiceRecord (UUID uuid);

}
