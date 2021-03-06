
package com.jfixby.bluemesa;

import java.io.IOException;

import com.jfixby.bluemesa.sqs.BlureMesaMessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransport;
import com.jfixby.bluemesa.sqs.MessageTransportSpecs;
import com.jfixby.bluemesa.sqs.MessagesConsumer;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;

public class BlueMesaAndroid {

	private final AndroidApplication app;

	public BlueMesaAndroid (final AndroidApplication app) {
		this.app = app;

// L.d("View.DRAWING_CACHE_QUALITY_AUTO", View.DRAWING_CACHE_QUALITY_AUTO);
	}

	public void run (final MessagesConsumer ep) {
		final Thread t = new Thread() {
			@Override
			public void run () {
				while (true) {
					try {
						BlueMesaAndroid.this.test(ep);
					} catch (final Throwable e) {
						e.printStackTrace();
						L.e(e);
						Sys.sleep(10000);
					}
				}

			}

		};
		t.start();
	}

	private void test (final MessagesConsumer ep) throws IOException {
		L.d("connecting BT");
		while (!ep.isReady()) {
			Sys.sleep(100);
		}

		final GasSensorMessageReaderSpecs specs = new GasSensorMessageReaderSpecs();

		final MessageTransportSpecs t_specs = new MessageTransportSpecs();
		final MessageTransport transport = new BlureMesaMessageTransport(t_specs, ep);

		final GasSensorMessageReader reader = new GasSensorMessageReader(specs);
		final AndroidBTConnectionOpener opener = new AndroidBTConnectionOpener(this.app);
		final boolean success = reader.open(opener);

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
