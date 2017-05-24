
package com.jfixby.bluemesa.desktop;

import java.io.IOException;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jfixby.bluemesa.EntryPoint;
import com.jfixby.bluemesa.GasSensorMessage;
import com.jfixby.bluemesa.GasSensorMessageReader;
import com.jfixby.bluemesa.GasSensorMessageReaderSpecs;
import com.jfixby.bluemesa.sqs.BlureMesaMessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransportSpecs;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.aws.api.sqs.SQS;
import com.jfixby.scarabei.aws.desktop.sqs.DesktopSQS;
import com.jfixby.scarabei.gson.GoogleGson;

public class DesktopLauncher {
	public static void main (final String[] arg) throws IOException {

		ScarabeiDesktop.deploy();
		SQS.installComponent(new DesktopSQS());

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
		final String deviceID = "98D331B2B6D3";
		final EntryPoint ep = new EntryPoint();
		final MessageTransport transport = new BlureMesaMessageTransport(t_specs, ep);
		final String url = "btspp://" + deviceID + ":1;authenticate=false;encrypt=false;master=false";
// final InputStream java_stream =;
		final GasSensorMessageReaderSpecs specs = new GasSensorMessageReaderSpecs();
		final GasSensorMessageReader reader = new GasSensorMessageReader(specs);
		reader.open(new DekstopBTConnectionOpener(deviceID));

		final Thread t = new Thread() {
			@Override
			public void run () {
				while (true) {
					GasSensorMessage message;
					try {
						message = reader.read();
						message.print();
						transport.send(message);
					} catch (final Throwable e) {
						L.d(e.toString());
					}

				}
			}
		};

		t.start();

		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 1600;
		new LwjglApplication(ep, config);
	}
}
