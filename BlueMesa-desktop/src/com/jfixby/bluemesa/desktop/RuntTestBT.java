
package com.jfixby.bluemesa.desktop;

import java.io.IOException;

import com.jfixby.bluemesa.GasSensorMessage;
import com.jfixby.bluemesa.GasSensorMessageReader;
import com.jfixby.bluemesa.GasSensorMessageReaderException;
import com.jfixby.bluemesa.GasSensorMessageReaderSpecs;
import com.jfixby.bluemesa.sqs.BlureMesaMessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransportSpecs;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.gson.GoogleGson;

public class RuntTestBT {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();

		Json.installComponent(new GoogleGson());
// final BTSpecs specs = ScarabeiBlueTooth.newBTSpecs();
// final BT bt = ScarabeiBlueTooth.newBT(specs);

// final RemoteDeviceDiscovery disco = new RemoteDeviceDiscovery();
// final Collection<BTDeviceInfo> list = disco.getDevices();
// list.print("list");

// final ServicesSearch search = new ServicesSearch();
// final Map<String, Map<String, String>> devices = search.getBluetoothDevices();
// devices.print("devices");

		final MessageTransportSpecs t_specs = new MessageTransportSpecs();
		final MessageTransport transport = new BlureMesaMessageTransport(t_specs, null);

// final InputStream java_stream =;
		final GasSensorMessageReaderSpecs specs = new GasSensorMessageReaderSpecs();
		final GasSensorMessageReader reader = new GasSensorMessageReader(specs);
		final String deviceID = "98D331B2B6D3";
		reader.open(new DekstopBTConnectionOpener(deviceID));

		while (true) {
			GasSensorMessage message;
			try {
				message = reader.read();
				message.print();
				transport.send(message);
			} catch (final GasSensorMessageReaderException e) {
				L.d(e.toString());
			}

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
