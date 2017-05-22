
package com.jfixby.bluemesa;

import java.io.IOException;
import java.util.UUID;

import com.jfixby.scarabei.api.collections.Collection;

public interface AndroidBluetoothDevice {

	Collection<AndroidParcelUuid> getUuids ();

	AndroidBluetoothSocket createInsecureRfcommSocketToServiceRecord (UUID uuid) throws IOException;

}
