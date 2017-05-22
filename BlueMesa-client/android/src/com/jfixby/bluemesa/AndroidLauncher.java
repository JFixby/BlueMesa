
package com.jfixby.bluemesa;

import java.io.IOException;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.jfixby.scarabei.android.api.AndroidAppVersion;
import com.jfixby.scarabei.android.api.AndroidComponent;
import com.jfixby.scarabei.android.api.camera.AndroidCameraSetup;
import com.jfixby.scarabei.api.display.DisplayMetrics;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.api.sys.SystemInfo;
import com.jfixby.scarabei.red.android.ScarabeiAndroid;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication implements AndroidComponent {
	private static final String DEVICE_ID = "98D331B2B6D3";
	private BluetoothAdapter adaptor;

	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		this.initialize(new EntryPoint(), config);
		ScarabeiAndroid.deploy(this);
		final Thread t = new Thread() {
			@Override
			public void run () {
				while (true) {
					try {
						AndroidLauncher.this.test();
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

		final String url = "btspp://" + DEVICE_ID + ":1;authenticate=false;encrypt=false;master=false";
		L.d("BT start", url);
		final GasSensorMessageReaderSpecs specs = new GasSensorMessageReaderSpecs();
		specs.url = (url);
		final GasSensorMessageReader reader = new GasSensorMessageReader(specs);
		reader.open(new AndroidBTConnectionOpener(this, DEVICE_ID));

		while (true) {
			GasSensorMessage message;
			try {
				message = reader.read();
				message.print();
			} catch (final GasSensorMessageReaderException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public long getMaxHeapSize () {
		return 0;
	}

	@Override
	public long getRecommendedHeapSize () {
		return 0;
	}

	@Override
	public String getApplicationPrivateDirPathString () {
		return null;
	}

	@Override
	public AndroidCameraSetup getCameraSetup () {
		return null;
	}

	@Override
	public File getPrivateFolder () {
		return null;
	}

	@Override
	public File getCacheFolder () {
		return null;
	}

	@Override
	public DisplayMetrics getDisplayMetrics () {
		return null;
	}

	@Override
	public String getBrand () {
		return null;
	}

	@Override
	public String getModel () {
		return null;
	}

	@Override
	public String getHost () {
		return null;
	}

	@Override
	public String getVersionRelease () {
		return null;
	}

	@Override
	public AndroidAppVersion getAppVersion () {
		return null;
	}

	@Override
	public SystemInfo getSystemInfo () {
		return null;
	}

	@Override
	public String getSerial () {
		return null;
	}

	@Override
	public String getFingerPrint () {
		return null;
	}

	@Override
	public String getManufacturer () {
		return null;
	}

	@Override
	public double densityIndependentPixels2Pixels (final float dip) {
		return 0;
	}
}
