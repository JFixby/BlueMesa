
package com.jfixby.bluemesa;

import android.os.ParcelUuid;

public class RedAndroidParcelUuid implements AndroidParcelUuid {

	private final ParcelUuid x;

	public RedAndroidParcelUuid (final ParcelUuid x) {
		this.x = x;
	}

	@Override
	public String getString () {
		return this.x.toString();
	}

}
