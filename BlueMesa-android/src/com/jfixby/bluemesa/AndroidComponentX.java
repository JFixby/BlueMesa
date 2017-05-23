package com.jfixby.bluemesa;

import com.jfixby.scarabei.android.api.AndroidAppVersion;
import com.jfixby.scarabei.android.api.AndroidComponent;
import com.jfixby.scarabei.android.api.camera.AndroidCameraSetup;
import com.jfixby.scarabei.api.display.DisplayMetrics;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.sys.SystemInfo;

public class AndroidComponentX implements AndroidComponent {

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
	public double densityIndependentPixels2Pixels (float dip) {
		return 0;
	}

}
