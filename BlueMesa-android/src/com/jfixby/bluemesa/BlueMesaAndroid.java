
package com.jfixby.bluemesa;

import java.io.IOException;

import com.jfixby.bluemesa.sqs.MessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransportSpecs;
import com.jfixby.bluemesa.sqs.MessagesConsumer;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;

import android.view.View;

public class BlueMesaAndroid {

	private final AndroidApplication app;

	public BlueMesaAndroid (final AndroidApplication app) {
		this.app = app;

		L.d("View.DRAWING_CACHE_QUALITY_AUTO", View.DRAWING_CACHE_QUALITY_AUTO);
	}

	public void run (final MessagesConsumer ep) {
		final Thread t = new Thread() {
			@Override
			public void run () {
				while (true) {
					try {
						BlueMesaAndroid.this.test(ep);
					} catch (final IOException e) {
						L.e(e);
						Sys.sleep(10000);
					}
				}

			}

		};
		t.start();
	}

	private void test (final MessagesConsumer ep) throws IOException {

		final GasSensorMessageReaderSpecs specs = new GasSensorMessageReaderSpecs();

		final MessageTransportSpecs t_specs = new MessageTransportSpecs();
		final MessageTransport transport = new AnroidMessageTransport(t_specs, ep);
		Sys.sleep(20);
		final GasSensorMessageReader reader = new GasSensorMessageReader(specs);
		L.d("connecting BT");
		final boolean success = reader.open(new AndroidBTConnectionOpener(this.app));

		while (true) {
			if (!success) {
				L.e("No paired device detected");
				Sys.sleep(100000);
				Sys.exit();
				break;
			}
			GasSensorMessage message;
			try {
				message = reader.read();
				message.print();
				transport.send(message);
			} catch (final GasSensorMessageReaderException e) {
// e.printStackTrace();
				L.e(e);
			}
		}
	}
}
