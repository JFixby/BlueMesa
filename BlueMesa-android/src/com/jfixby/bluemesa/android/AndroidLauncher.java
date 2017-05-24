
package com.jfixby.bluemesa.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.jfixby.bluemesa.*;
import com.jfixby.scarabei.android.api.AndroidComponent;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.gson.GoogleGson;
import com.jfixby.scarabei.red.android.ScarabeiAndroid;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication implements com.jfixby.bluemesa.AndroidApplication {

	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		final com.jfixby.bluemesa.EntryPoint e = new com.jfixby.bluemesa.EntryPoint();
		this.initialize(e, config);

		final AndroidComponent x = new AndroidComponentX();

		ScarabeiAndroid.deploy(x);
		Json.installComponent(new GoogleGson());
		L.d("ScarabeiAndroid deployed!");
		final BlueMesaAndroid bm = new BlueMesaAndroid(this);

		bm.run(e);

	}

	@Override
	protected void onPause() {
		super.onPause();
//		Sys.exit();
	}

	@Override
	public void requestBT () {
		final Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		this.startActivityForResult(turnOn, 0);
	}

	@Override
	public AndroidBluetoothAdapter getAdaptor () {
		return new AndroidBTAdaptor();
	}

}
