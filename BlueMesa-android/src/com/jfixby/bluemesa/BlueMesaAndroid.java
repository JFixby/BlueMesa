
package com.jfixby.bluemesa;

import java.io.IOException;

import com.jfixby.bluemesa.sqs.MessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransportSpecs;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;

public class BlueMesaAndroid {

// private static final String DEVICE_ID = "98D331B2B6D3";
	private final AndroidApplication app;
	private final String deviceId;

	public BlueMesaAndroid (final AndroidApplication app, final String deviceId) {
		this.deviceId = deviceId;
		this.app = app;
	}

	public void run () {
		final Thread t = new Thread() {
			@Override
			public void run () {
				while (true) {
					try {
						BlueMesaAndroid.this.test();
					} catch (final IOException e) {
						e.printStackTrace();
						Sys.sleep(10000);
					}
				}

			}

		};
		t.start();
	}

	private void test () throws IOException {

		final String url = "btspp://" + this.deviceId + ":1;authenticate=false;encrypt=false;master=false";
		L.d("BT start", url);
		final GasSensorMessageReaderSpecs specs = new GasSensorMessageReaderSpecs();
		specs.url = (url);
		specs.deviceID = this.deviceId;

		final MessageTransportSpecs t_specs = new MessageTransportSpecs();
		final MessageTransport transport = new AnroidMessageTransport(t_specs);

		final GasSensorMessageReader reader = new GasSensorMessageReader(specs);
		reader.open(new AndroidBTConnectionOpener(this.app, this.deviceId));

		while (true) {
			GasSensorMessage message;
			try {
				message = reader.read();
				message.print();
				transport.send(message);
			} catch (final GasSensorMessageReaderException e) {
				e.printStackTrace();
			}
		}
	}
}
