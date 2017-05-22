
package com.jfixby.bluemesa;

import java.io.DataInputStream;
import java.io.IOException;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.math.IntegerMath;
import com.jfixby.scarabei.api.sys.Sys;

public class GasSensorMessageReader {

	private final String url;
	private DataInputStream is;
	private final String deviceID;

	public GasSensorMessageReader (final GasSensorMessageReaderSpecs specs) {
		this.url = specs.url;
		this.deviceID = specs.deviceID;
	}

	public void open (final BTConnectionOpener opener) throws IOException {
		Debug.checkTrue("is closed", this.is == null);
		this.is = opener.open(this.url);
		;

	}

	public static String STRING_Message = "~Message№";
	public static String STRING_FromABCboard = "~FromABCboard";
	public static String STRING_HUITA_IN = "~#####>";
	public static String STRING_HUITA_OUT = "<##\n";

	public GasSensorMessage read () throws IOException, GasSensorMessageReaderException {
		final GasSensorMessage msg = new GasSensorMessage();
		msg.deviceID = this.deviceID.toUpperCase();
		String b = "";
		boolean valveOpen = false;
		final List<Byte> bytes = Collections.newList();
		final Long globalCounter = null;
		while (!b.endsWith(STRING_HUITA_OUT)) {
			final byte bt = this.is.readByte();
			final char c = (char)bt;
			b = b + c;
			if (valveOpen) {
				bytes.add(bt);
			}
			if (b.contains(STRING_HUITA_IN)) {
				valveOpen = true;
			}
			if (globalCounter == null) {
				if (b.contains(STRING_FromABCboard)) {
					final int beginIndex = STRING_Message.length();
					final int endIndex = b.indexOf(STRING_FromABCboard);
					String num = b.substring(beginIndex, endIndex);
// L.d("num", num);
					num = removeNonDigits(num);
// L.d("num", num);
					msg.globalCounter = Integer.parseInt(num);
// Sys.exit();

				}
			}
		}
		bytes.splitAt(bytes.size() - STRING_HUITA_OUT.length());

		final byte[] data = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++) {
			data[i] = bytes.getElementAt(i);
		}

		msg.data = this.decode(data);
		msg.timestamp = Sys.SystemTime().currentTimeMillis();
// L.d("data read " + b.length(), b);
// L.d(" bytes", data);
		return msg;

	}

	public float formatResistance (final byte _A, final byte _B) {
		int _temp = 0;
		final float _r = 0;
		int e = 0;
		int b = 0;

		_temp = ((_A) << 8) + _B;
		b = _temp / 10; // get number rounded down;
		e = _temp - b * 10;

		return b / 1000f * IntegerMath.power(10, e);
	}

	public DataST decode (final byte[] bytes) throws GasSensorMessageReaderException {
		final DataST received_data = new DataST();
		long _cksm = 0;
		int i;

		// 1. basic array check
		for (i = 0; i < bytes.length; i++) {
			_cksm += bytes[i];
		}
		_cksm -= bytes[1];

		if ((bytes[0] < 1) || (bytes[0] > 15)) {
			throw new GasSensorMessageReaderException("wrong hardware id byte:" + bytes[0]);
		} // wrong hardware id byte
		if (bytes.length != 64) {
			throw new GasSensorMessageReaderException("array length failed:" + bytes.length);
		} // array length failed
		if ((byte)(_cksm & 0xFF) != bytes[1]) {
			throw new GasSensorMessageReaderException("checksum failed:" + _cksm);
		} // checksum failed

		// 2.getting general data
		received_data.temp_board = bytes[2] * 3.0f;
		received_data.temp = bytes[6] * 3.0f;
		received_data.RH = bytes[7] * 2.5f;

		// 3. heater state
		for (i = 0; i < 8; i++) {
			if (((1 * bytes[3] >> i) & 0x01) != 0) {
				received_data.heater_state[i] = 50;
			} else if (((bytes[5] >> i) & 1) != 0) {
				received_data.heater_state[i] = 150;
			} else if (((bytes[4] >> i) & 1) != 0) {
				received_data.heater_state[i] = 100;
			} else {
				received_data.heater_state[i] = 0;
			}
		}

		// 4. digipot settings
		for (i = 0; i < 8; i++) {
			received_data.heater_setting[i] = bytes[8 + i];
			received_data.reference_setting[i] = bytes[16 + i];
		}

		// 4. resistances
		for (i = 0; i < 8; i++) {

			received_data.R_x[i] = this.formatResistance(bytes[56 + i], bytes[48 + i]);
			received_data.H_r[i] = ((bytes[40 + i] << 8) + bytes[32 + i]) / 100f;
			received_data.V_h[i] = (bytes[24 + i] / 16f);
		}
		return received_data;
	}

	public static String removeNonDigits (final String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		return str.replaceAll("[^0-9]+", "");
	}

	private void skip (final int length) throws IOException {
// L.d("skip " + length + ":");
		for (int i = 0; i < length; i++) {
			final int c = this.is.read();
// L.d_appendChars((char)c);
		}
// L.d();
	}

}
