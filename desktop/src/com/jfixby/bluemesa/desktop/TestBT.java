
package com.jfixby.bluemesa.desktop;

import java.io.IOException;

import com.jfixby.bluemesa.GasSensorMessage;
import com.jfixby.bluemesa.GasSensorMessageReader;
import com.jfixby.bluemesa.GasSensorMessageReaderSpecs;
import com.jfixby.bluemesa.MessageTransport;
import com.jfixby.bluemesa.MessageTransportSpecs;
import com.jfixby.scarabei.amazon.aws.RedAWS;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.aws.api.AWS;
import com.jfixby.scarabei.gson.GoogleGson;

public class TestBT {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		AWS.installComponent(new RedAWS());
		Json.installComponent(new GoogleGson());
// final BTSpecs specs = ScarabeiBlueTooth.newBTSpecs();
// final BT bt = ScarabeiBlueTooth.newBT(specs);

// final RemoteDeviceDiscovery disco = new RemoteDeviceDiscovery();
// final Collection<BTDeviceInfo> list = disco.getDevices();
// list.print("list");

// final ServicesSearch search = new ServicesSearch();
// final Map<String, Map<String, String>> devices = search.getBluetoothDevices();
// devices.print("devices");
		final String DEVICE_ID = "98D331B2B6D3";
		final MessageTransportSpecs t_specs = new MessageTransportSpecs();
		t_specs.deviceID = DEVICE_ID;
		final MessageTransport transport = new MessageTransport(t_specs);
		final String url = "btspp://" + DEVICE_ID + ":1;authenticate=false;encrypt=false;master=false";
// final InputStream java_stream =;
		final GasSensorMessageReaderSpecs specs = new GasSensorMessageReaderSpecs();
		specs.url = (url);
		final GasSensorMessageReader reader = new GasSensorMessageReader(specs);
		reader.open(new DekstopBTConnectionOpener());

		while (true) {
			final GasSensorMessage message = reader.read();
			message.print();
			transport.send(message);

			Sys.exit();
		}
// is.close();
// final BluetoothAdapter bta = new BluetoothAdapter();

// BluetoothDevice connect_device = mBluetoothAdapter.getRemoteDevice(address);
//
// try {
// BluetoothSocket socket = connect_device.createRfcommSocketToServiceRecord(my_UUID);
// socket.connect();
// } catch (IOException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }

	}

}
