
package com.jfixby.bluemesa;

import java.io.IOException;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		this.initialize(new EntryPoint(), config);

		final Thread t = new Thread() {
			@Override
			public void run () {
				try {
					AndroidLauncher.this.test();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

		};
		t.start();
	}

	private void test () throws IOException {
		final String url = "btspp://98D331B2B6D3:1;authenticate=false;encrypt=false;master=false";
		final GasSensorMessageReaderSpecs specs = new GasSensorMessageReaderSpecs();
		specs.url = (url);
		final GasSensorMessageReader reader = new GasSensorMessageReader(specs);
		reader.open();

		while (true) {
			final GasSensorMessage message = reader.read();
			message.print();
		}
	}
}
